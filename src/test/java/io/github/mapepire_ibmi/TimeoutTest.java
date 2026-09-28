package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.JobStatus;
import io.github.mapepire_ibmi.types.PoolOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.exceptions.RequestTimeoutException;

/**
 * Tests for client-side request timeouts. These do not require a Mapepire
 * server: the socket is mocked and responses are fed in via handleMessage.
 */
@Timeout(10)
class TimeoutTest {
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

    private static String response(String id) {
        return "{\"id\":\"" + id + "\",\"success\":true}";
    }

    @Test
    void noTimeoutByDefault() throws Exception {
        assertEquals(0, job.getRequestTimeout());

        CompletableFuture<String> future = job.send(request("default1"));
        Thread.sleep(300);

        assertFalse(future.isDone());
        assertEquals(1, job.getRunningCount());
    }

    @Test
    void requestTimesOut() throws Exception {
        job.setRequestTimeout(100);
        CompletableFuture<String> future = job.send(request("timeout1"));

        ExecutionException e = assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
        RequestTimeoutException cause = assertInstanceOf(RequestTimeoutException.class, e.getCause());
        assertEquals("timeout1", cause.getRequestId());
        assertEquals(100, cause.getTimeoutMillis());
        assertEquals("Request timeout1 timed out after 100ms", cause.getMessage());
    }

    @Test
    void timedOutRequestIsRemovedFromResponseMap() throws Exception {
        job.setRequestTimeout(100);
        CompletableFuture<String> future = job.send(request("timeout2"));
        assertEquals(1, job.getRunningCount());

        assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));

        assertEquals(0, job.getRunningCount());
        assertEquals(JobStatus.Ready, job.getStatus());
    }

    @Test
    void perRequestTimeoutOverridesDefault() throws Exception {
        CompletableFuture<String> future = job.send(request("override1"), 100);

        ExecutionException e = assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
        assertInstanceOf(RequestTimeoutException.class, e.getCause());
    }

    @Test
    void responseBeforeTimeoutCompletesNormally() throws Exception {
        job.setRequestTimeout(500);
        CompletableFuture<String> future = job.send(request("fast1"));

        job.handleMessage(response("fast1"));

        assertEquals(response("fast1"), future.get(5, TimeUnit.SECONDS));
        Thread.sleep(700);
        assertFalse(future.isCompletedExceptionally());
        assertEquals(0, job.getRunningCount());
    }

    @Test
    void lateResponseIsIgnored() throws Exception {
        job.setRequestTimeout(100);
        CompletableFuture<String> future = job.send(request("late1"));
        assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));

        job.handleMessage(response("late1"));

        assertTrue(future.isCompletedExceptionally());
        assertEquals(0, job.getRunningCount());
    }

    @Test
    void queryExecuteTimesOut() throws Exception {
        job.setRequestTimeout(100);
        CompletableFuture<QueryResult<Object>> future = job.query("SELECT * FROM SAMPLE.DEPARTMENT").execute();

        ExecutionException e = assertThrows(ExecutionException.class, () -> future.get(5, TimeUnit.SECONDS));
        assertInstanceOf(RequestTimeoutException.class, e.getCause());
        assertEquals(0, job.getRunningCount());
    }

    @Test
    void negativeTimeoutIsRejected() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> job.setRequestTimeout(-1));
        assertThrows(IllegalArgumentException.class, () -> job.send(request("negative1"), -1));
        assertEquals(0, job.getRunningCount());
    }

    @Test
    void poolOptionsRequestTimeout() throws Exception {
        PoolOptions options = new PoolOptions(null, 5, 1);
        assertEquals(0, options.getRequestTimeout());

        options.setRequestTimeout(250);
        assertEquals(250, options.getRequestTimeout());
        assertThrows(IllegalArgumentException.class, () -> options.setRequestTimeout(-1));
    }

    @Test
    void failedSendIsRemovedFromResponseMap() throws Exception {
        doThrow(new WebsocketNotConnectedException()).when(socket).send(anyString());

        assertThrows(WebsocketNotConnectedException.class, () -> job.send(request("failed1")));
        assertEquals(0, job.getRunningCount());
    }
}
