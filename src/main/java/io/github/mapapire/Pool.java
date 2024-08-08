package io.github.mapapire;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import io.github.mapapire.types.DaemonServer;
import io.github.mapapire.types.JDBCOptions;
import io.github.mapapire.types.JobStatus;

class PoolOptions {
    private DaemonServer creds;
    private JDBCOptions opts;
    private int maxSize;
    private int startingSize;

    PoolOptions(DaemonServer _creds, JDBCOptions _opts, int _maxSize, int _startingSize) {
        this.creds = _creds;
        this.opts = _opts;
        this.maxSize = _maxSize;
        this.startingSize = _startingSize;
    }

    PoolOptions(DaemonServer _creds, int _maxSize, int _startingSize) {
        this.creds = _creds;
        this.maxSize = _maxSize;
        this.startingSize = _startingSize;
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

    PoolAddOptions(SqlJob _existingJob, boolean _poolIgnore) {
        this.existingJob = _existingJob;
        this.poolIgnore = _poolIgnore;
    }

    public SqlJob getExistingJob() {
        return existingJob;
    }

    public boolean isPoolIgnore() {
        return poolIgnore;
    }
}

public class Pool {
    private List<SqlJob> jobs = new ArrayList<>();
    private PoolOptions options;
    private List<JobStatus> INVALID_STATES = new ArrayList<JobStatus>(
            Arrays.asList(JobStatus.Ended, JobStatus.NotStarted));

    Pool(PoolOptions _options) {
        this.options = _options;
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
                .filter(job -> !INVALID_STATES.contains(job.getStatus()))
                .collect(Collectors.toList())
                .size();
        return activeJobCount < options.getMaxSize();
    }

    public int getActiveJobCount() {
        return this.jobs.stream()
                .filter(job -> job.getStatus() == JobStatus.Busy || job.getStatus() == JobStatus.Ready)
                .collect(Collectors.toList())
                .size();
    }

    public void cleanup() {
        this.jobs.removeIf(job -> INVALID_STATES.contains(job.getStatus()));
    }

    // TODO: test cases with existingJob parameter
    public CompletableFuture<SqlJob> addJob(PoolAddOptions options) {
        if (options.getExistingJob() != null) {
            cleanup();
        }

        SqlJob newSqlJob = options.getExistingJob() != null ? options.getExistingJob() : new SqlJob(this.options.getOpts());

        if (options.isPoolIgnore() != true) {
            this.jobs.add(newSqlJob);
        }

        if (newSqlJob.getStatus() == JobStatus.NotStarted) {
            try {
                newSqlJob.connect(this.options.getCreds()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return CompletableFuture.completedFuture(newSqlJob);
    }
}
