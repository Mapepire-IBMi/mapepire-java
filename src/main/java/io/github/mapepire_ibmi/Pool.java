package io.github.mapepire_ibmi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.JobStatus;
import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;

class PoolOptions {
    private DaemonServer creds;
    private JDBCOptions opts;
    private int maxSize;
    private int startingSize;

    PoolOptions(DaemonServer creds, JDBCOptions opts, int maxSize, int startingSize) {
        this.creds = creds;
        this.opts = opts;
        this.maxSize = maxSize;
        this.startingSize = startingSize;
    }

    PoolOptions(DaemonServer creds, int maxSize, int startingSize) {
        this.creds = creds;
        this.opts = new JDBCOptions();
        this.maxSize = maxSize;
        this.startingSize = startingSize;
    }

    public DaemonServer getCreds() {
        return creds;
    }

    public JDBCOptions getOpts() {
        return opts;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getStartingSize() {
        return startingSize;
    }
}

class PoolAddOptions {
    /** An existing job to add to the pool */
    private SqlJob existingJob;

    /** Don't add to the pool */
    private boolean poolIgnore;

    public PoolAddOptions() {

    }

    public PoolAddOptions(SqlJob existingJob, boolean poolIgnore) {
        this.existingJob = existingJob;
        this.poolIgnore = poolIgnore;
    }

    public SqlJob getExistingJob() {
        return existingJob;
    }

    public void setExistingJob(SqlJob existingJob) {
        this.existingJob = existingJob;
    }

    public boolean isPoolIgnore() {
        return poolIgnore;
    }

    public void setPoolIgnore(boolean poolIgnore) {
        this.poolIgnore = poolIgnore;
    }
}

public class Pool {
    private List<SqlJob> jobs = new ArrayList<>();
    private PoolOptions options;
    private List<JobStatus> INVALID_STATES = new ArrayList<JobStatus>(
            Arrays.asList(JobStatus.Ended, JobStatus.NotStarted));

    Pool(PoolOptions options) {
        this.options = options;
    }

    public CompletableFuture<Void> init() {
        List<CompletableFuture<SqlJob>> futures = new ArrayList<>();
        for (int i = 0; i < options.getStartingSize(); i++) {
            futures.add(addJob());
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public boolean hasSpace() {
        int activeJobCount = this.jobs.stream()
                .filter(j -> !INVALID_STATES.contains(j.getStatus()))
                .collect(Collectors.toList())
                .size();
        return activeJobCount < options.getMaxSize();
    }

    public int getActiveJobCount() {
        return this.jobs.stream()
                .filter(j -> j.getStatus() == JobStatus.Busy || j.getStatus() == JobStatus.Ready)
                .collect(Collectors.toList())
                .size();
    }

    public void cleanup() {
        this.jobs.removeIf(j -> INVALID_STATES.contains(j.getStatus()));
    }

    public CompletableFuture<SqlJob> addJob() {
        PoolAddOptions options = new PoolAddOptions();
        return this.addJob(options);
    }

    // TODO: test cases with existingJob parameter
    public CompletableFuture<SqlJob> addJob(PoolAddOptions options) {
        if (options.getExistingJob() != null) {
            cleanup();
        }

        SqlJob newSqlJob = options.getExistingJob() != null ? options.getExistingJob()
                : new SqlJob(this.options.getOpts());

        if (options.isPoolIgnore() != true) {
            this.jobs.add(newSqlJob);
        }

        if (newSqlJob.getStatus() == JobStatus.NotStarted) {
            try {
                newSqlJob.connect(this.options.getCreds()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return CompletableFuture.completedFuture(newSqlJob);
    }

    public SqlJob getReadyJob() {
        return this.jobs
                .stream()
                .filter(j -> j.getStatus() == JobStatus.Ready)
                .findFirst()
                .orElse(null);
    }

    public int getReadyJobIndex() {
        return IntStream.range(0, this.jobs.size())
                .filter(j -> jobs.get(j).getStatus() == JobStatus.Ready)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Returns a job as fast as possible. It will either be a ready job
     * or the job with the least requests on the queue. Will spawn new jobs
     * if the pool is not full but all jobs are busy.
     */
    public SqlJob getJob() {
        SqlJob job = this.getReadyJob();
        if (job == null) {
            // This code finds a job that is busy, but has the least requests on the queue
            List<SqlJob> busyJobs = this.jobs
                    .stream()
                    .filter(j -> j.getStatus() == JobStatus.Busy)
                    .collect(Collectors.toList());
            busyJobs
                    .sort(Comparator.comparingInt(j -> j.getRunningCount()));
            SqlJob freeist = busyJobs.isEmpty() ? null : busyJobs.get(0);

            // If this job is busy, and the pool is not full, add a new job for later
            if (this.hasSpace() && freeist != null && freeist.getRunningCount() > 2) {
                this.addJob();
            }

            return freeist;
        }

        return job;
    }

    public CompletableFuture<SqlJob> waitForJob() {
        return this.waitForJob(false);
    }

    /**
     * Returns a ready job if one is available, otherwise it will add a new job.
     * If the pool is full, then it will find a job with the least requests on the
     * queue.
     */
    public CompletableFuture<SqlJob> waitForJob(boolean useNewJob) {
        SqlJob job = getReadyJob();

        if (job == null) {
            if (hasSpace() || useNewJob) {
                return addJob().thenApply(newJob -> {
                    return newJob;
                });
            } else {
                return CompletableFuture.completedFuture(this.getJob());
            }
        }

        return CompletableFuture.completedFuture(job);
    }

    // TODO: needs test cases
    /**
     * Returns a job that is ready to be used. If no jobs are ready, it will
     * create a new job and return that. Use `addJob` to add back to the pool.
     */
    public CompletableFuture<SqlJob> popJob() {
        int index = getReadyJobIndex();
        if (index > -1) {
            return CompletableFuture.completedFuture(jobs.remove(index));
        } else {
            // Add a new job asynchronously
            PoolAddOptions options = new PoolAddOptions();
            options.setPoolIgnore(true);
            return this.addJob(options);
        }
    }

    public <T> Query<T> query(String sql) {
        QueryOptions options = new QueryOptions();
        return this.query(sql, options);
    }

    public <T> Query<T> query(String sql, QueryOptions opts) {
        SqlJob job = this.getJob();
        return job.query(sql, opts);
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql) throws Exception {
        QueryOptions options = new QueryOptions();
        return this.execute(sql, options);
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) throws Exception {
        SqlJob job = this.getJob();
        return job.execute(sql, opts);
    }

    public void end() {
        this.jobs.forEach(j -> j.close());
    }
}
