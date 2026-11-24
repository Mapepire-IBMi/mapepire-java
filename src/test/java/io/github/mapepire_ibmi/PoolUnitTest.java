package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.JobStatus;
import io.github.mapepire_ibmi.types.PoolAddOptions;
import io.github.mapepire_ibmi.types.PoolOptions;
import io.github.mapepire_ibmi.types.exceptions.ClientException;

/**
 * Offline pool tests for the no-available-job path. These do not require a
 * Mapepire server.
 */
@Timeout(10)
class PoolUnitTest {
    private static final String NO_JOBS_MESSAGE = "No jobs are available. Please wait for a job to "
            + "become available or increase the starting size of the pool.";

    private static Pool emptyPool() {
        DaemonServer creds = new DaemonServer("localhost", 8076, "user", "password", false);
        return new Pool(new PoolOptions(creds, 2, 1));
    }

    private static void setStatus(SqlJob job, JobStatus status) throws Exception {
        Field statusField = SqlJob.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(job, status);
    }

    private static void addExistingJob(Pool pool, SqlJob job) throws Exception {
        PoolAddOptions options = new PoolAddOptions();
        options.setExistingJob(job);
        pool.addJob(options).get();
    }

    @Test
    void getJobThrowsWhenPoolNeverInitialized() throws Exception {
        Pool pool = emptyPool();

        ClientException e = assertThrowsExactly(ClientException.class, pool::getJob);
        assertEquals(NO_JOBS_MESSAGE, e.getMessage());
    }

    @Test
    void getJobThrowsWhenOnlyEndedJobsPresent() throws Exception {
        Pool pool = emptyPool();
        SqlJob ended = new SqlJob();
        ended.close();
        assertEquals(JobStatus.Ended, ended.getStatus());
        addExistingJob(pool, ended);

        ClientException e = assertThrowsExactly(ClientException.class, pool::getJob);
        assertEquals(NO_JOBS_MESSAGE, e.getMessage());
    }

    @Test
    void waitForJobFailsFutureWhenPoolFullWithNoReadyOrBusyJobs() throws Exception {
        DaemonServer creds = new DaemonServer("localhost", 8076, "user", "password", false);
        Pool pool = new Pool(new PoolOptions(creds, 1, 1));

        SqlJob connecting = new SqlJob();
        setStatus(connecting, JobStatus.Connecting);
        addExistingJob(pool, connecting);
        assertEquals(false, pool.hasSpace());

        // waitForJob must return a future; getJob failures stay on that future.
        CompletableFuture<SqlJob> future = pool.waitForJob();
        assertTrue(future.isCompletedExceptionally());

        ExecutionException e = assertThrowsExactly(ExecutionException.class, future::get);
        ClientException cause = assertInstanceOf(ClientException.class, e.getCause());
        assertEquals(NO_JOBS_MESSAGE, cause.getMessage());
    }

    @Test
    void executeThrowsWhenNoJobsAvailable() throws Exception {
        DaemonServer creds = new DaemonServer("localhost", 8076, "user", "password", false);
        Pool pool = new Pool(new PoolOptions(creds, 1, 1));

        SqlJob connecting = new SqlJob();
        setStatus(connecting, JobStatus.Connecting);
        addExistingJob(pool, connecting);

        ClientException e = assertThrowsExactly(ClientException.class,
                () -> pool.execute("SELECT 1 FROM SYSIBM.SYSDUMMY1"));
        assertEquals(NO_JOBS_MESSAGE, e.getMessage());
    }

    @Test
    void queryThrowsWhenNoJobsAvailable() throws Exception {
        DaemonServer creds = new DaemonServer("localhost", 8076, "user", "password", false);
        Pool pool = new Pool(new PoolOptions(creds, 1, 1));

        SqlJob connecting = new SqlJob();
        setStatus(connecting, JobStatus.Connecting);
        addExistingJob(pool, connecting);

        ClientException e = assertThrowsExactly(ClientException.class,
                () -> pool.query("SELECT 1 FROM SYSIBM.SYSDUMMY1"));
        assertEquals(NO_JOBS_MESSAGE, e.getMessage());
    }
}
