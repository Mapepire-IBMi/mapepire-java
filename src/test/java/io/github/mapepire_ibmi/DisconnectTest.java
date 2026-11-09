package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.java_websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.JobStatus;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.QueryState;

/**
 * Regression tests for disconnects during active requests. These do not require
 * a Mapepire server: the socket is mocked and in-flight work is left unanswered
 * until the job is closed.
 */
@Timeout(10)
class DisconnectTest {
    private SqlJob job;
    private WebSocketClient socket;

    @BeforeEach
    void setup() throws Exception {
        job = new SqlJob();
        socket = mock(WebSocketClient.class);

        Field socketField = SqlJob.class.getDeclaredField("socket");
        socketField.setAccessible(true);
        socketField.set(job, socket);
    }

    private static String request(String id) {
        return "{\"id\":\"" + id + "\",\"type\":\"getversion\"}";
    }

    @Test
    void closeDuringActiveQueryFailsFutureAndCleansUp() throws Exception {
        Query query = job.query("SELECT 1 FROM SYSIBM.SYSDUMMY1");
        CompletableFuture<QueryResult<Object>> future = query.execute();

        assertFalse(future.isDone());
        assertEquals(1, job.getRunningCount());

        job.close();

        ExecutionException e = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        SocketException cause = assertInstanceOf(SocketException.class, e.getCause());
        assertTrue(cause.getMessage().contains("Connection closed"));

        assertEquals(0, job.getRunningCount());
        assertEquals(JobStatus.Ended, job.getStatus());
        assertEquals(QueryState.ERROR, query.getState());
    }

    @Test
    void closeDuringSocketOpenFailsConnectFuture() throws Exception {
        Field statusField = SqlJob.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(job, JobStatus.Connecting);

        CompletableFuture<String> openedConnectionFuture = new CompletableFuture<>();
        Field openFutureField = SqlJob.class.getDeclaredField("openedConnectionFuture");
        openFutureField.setAccessible(true);
        openFutureField.set(job, openedConnectionFuture);

        job.close();

        ExecutionException e = assertThrows(ExecutionException.class,
                () -> openedConnectionFuture.get(2, TimeUnit.SECONDS));
        SocketException cause = assertInstanceOf(SocketException.class, e.getCause());
        assertTrue(cause.getMessage().contains("Connection closed"));

        assertEquals(JobStatus.Ended, job.getStatus());
        assertEquals(0, job.getRunningCount());
    }

    @Test
    void closeWithMultipleInFlightRequestsFailsAll() throws Exception {
        CompletableFuture<String> future1 = job.send(request("inflight1"));
        CompletableFuture<String> future2 = job.send(request("inflight2"));
        CompletableFuture<String> future3 = job.send(request("inflight3"));

        assertEquals(3, job.getRunningCount());
        assertFalse(future1.isDone());
        assertFalse(future2.isDone());
        assertFalse(future3.isDone());

        job.close();

        assertClosedException(future1);
        assertClosedException(future2);
        assertClosedException(future3);

        assertEquals(0, job.getRunningCount());
        assertEquals(JobStatus.Ended, job.getStatus());
    }

    private static void assertClosedException(CompletableFuture<String> future)
            throws InterruptedException, TimeoutException {
        ExecutionException e = assertThrows(ExecutionException.class, () -> future.get(2, TimeUnit.SECONDS));
        SocketException cause = assertInstanceOf(SocketException.class, e.getCause());
        assertTrue(cause.getMessage().contains("Connection closed"));
    }
}
