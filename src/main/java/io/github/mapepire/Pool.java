package io.github.mapepire;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.github.mapepire.types.JobStatus;
import io.github.mapepire.types.PoolAddOptions;
import io.github.mapepire.types.PoolOptions;
import io.github.mapepire.types.QueryOptions;
import io.github.mapepire.types.QueryResult;

/**
 * Represents a connection pool for managing SQL jobs.
 */
public class Pool {
    /**
     * A list of SqlJob instances managed by the pool.
     */
    private List<SqlJob> jobs = new ArrayList<>();

    /**
     * The options for configuring the connection pool.
     */
    private PoolOptions options;

    /**
     * A list of invalid job states (ended or not started).
     */
    private List<JobStatus> INVALID_STATES = new ArrayList<JobStatus>(
            Arrays.asList(JobStatus.Ended, JobStatus.NotStarted));

    /**
     * Construct a new Pool instance.
     * @param options The options for configuring the connection pool.
     */
    Pool(PoolOptions options) {
        this.options = options;
    }

    /**
     * Initializes the pool by creating a number of SQL jobs defined by the starting
     * size.
     * @return A CompletableFuture that resolves when all jobs have been created.
     */
    public CompletableFuture<Void> init() {
        List<CompletableFuture<SqlJob>> futures = new ArrayList<>();
        for (int i = 0; i < options.getStartingSize(); i++) {
            futures.add(addJob());
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    /**
     * Check if there is space available in the pool for more jobs.
     * @return Whether there is space available.
     */
    public boolean hasSpace() {
        int activeJobCount = this.jobs.stream()
                .filter(j -> !INVALID_STATES.contains(j.getStatus()))
                .collect(Collectors.toList())
                .size();
        return activeJobCount < options.getMaxSize();
    }

    /**
     * Get the count of active jobs that are either busy or ready.
     * @return The number of active jobs.
     */
    public int getActiveJobCount() {
        return this.jobs.stream()
                .filter(j -> j.getStatus() == JobStatus.Busy || j.getStatus() == JobStatus.Ready)
                .collect(Collectors.toList())
                .size();
    }

    /**
     * Clean up the pool by removing jobs that are in invalid states.
     */
    public void cleanup() {
        this.jobs.removeIf(j -> INVALID_STATES.contains(j.getStatus()));
    }

    /**
     * Add a new job to the pool.
     * @return A CompletableFuture that resolves to the new job.
     */
    public CompletableFuture<SqlJob> addJob() {
        PoolAddOptions options = new PoolAddOptions();
        return this.addJob(options);
    }

    /**
     * Add a new job to the pool.
     * @param options Options for configuring an addition to the connection pool.
     * @return A CompletableFuture that resolves to the new job.
     */
    public CompletableFuture<SqlJob> addJob(PoolAddOptions options) {
        if (options.getExistingJob() != null) {
            cleanup();
        }

        SqlJob newSqlJob = options.getExistingJob() != null ? options.getExistingJob()
                : new SqlJob(this.options.getOpts());

        if (options.getPoolIgnore() != true) {
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

    /**
     * Get a ready job from the pool.
     * @return The first ready job found, or null if none are ready.
     */
    public SqlJob getReadyJob() {
        return this.jobs
                .stream()
                .filter(j -> j.getStatus() == JobStatus.Ready)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get the index of a ready job in the pool.
     * @return The index of the first ready job, or -1 if none are ready.
     */
    public int getReadyJobIndex() {
        return IntStream.range(0, this.jobs.size())
                .filter(j -> jobs.get(j).getStatus() == JobStatus.Ready)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Get a job as fast as possible. It will either be a ready job or the job with
     * the least requests on the queue. It will spawn new jobs if the pool is not
     * full but all jobs are busy.
     * @return The retrieved job.
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

    /**
     * Wait for a job to become available. It will return a ready job if one exists,
     * otherwise, it may create a new job if the pool is not full.
     * @return A CompletableFuture that resolves to a ready job.
     */
    public CompletableFuture<SqlJob> waitForJob() {
        return this.waitForJob(false);
    }

    /**
     * Wait for a job to become available. It will return a ready job if one exists,
     * otherwise, it may create a new job if the pool is not full.
     * @param useNewJob Whether a new job should be created even if the pool is
     *                  full.
     * @return A CompletableFuture that resolves to a ready job.
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

    /**
     * Pops a job from the pool if one is ready. If no jobs are ready, it will
     * create a new job and return that. The returned job should be added back to
     * the pool.
     * @return A CompletableFuture that resolves to a ready job or a new job.
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

    /**
     * Create a Query object using a job from the pool.
     * @param <T> The type of data to be returned.
     * @param sql The SQL query.
     * @return A new Query instance.
     */
    public <T> Query<T> query(String sql) {
        QueryOptions options = new QueryOptions();
        return this.query(sql, options);
    }

    /**
     * Create a Query object using a job from the pool.
     * @param <T>  The type of data to be returned.
     * @param sql  The SQL query.
     * @param opts The options for configuring the query.
     * @return A new Query instance.
     */
    public <T> Query<T> query(String sql, QueryOptions opts) {
        SqlJob job = this.getJob();
        return job.query(sql, opts);
    }

    /**
     * Execute a SQL command using a job from the pool.
     * @param <T> The type of data to be returned.
     * @param sql The SQL command to execute.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql) throws Exception {
        QueryOptions options = new QueryOptions();
        return this.execute(sql, options);
    }

    /**
     * Execute a SQL command using a job from the pool.
     * @param <T>  The type of data to be returned.
     * @param sql  The SQL command to execute.
     * @param opts The options for configuring the query.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) throws Exception {
        SqlJob job = this.getJob();
        return job.execute(sql, opts);
    }

    /**
     * Close all jobs in the pool.
     */
    public void end() {
        this.jobs.forEach(j -> j.close());
    }
}