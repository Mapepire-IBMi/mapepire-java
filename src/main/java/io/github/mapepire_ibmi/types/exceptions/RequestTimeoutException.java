package io.github.mapepire_ibmi.types.exceptions;

/**
 * Represents a request to the Mapepire server that did not receive a response
 * within the configured timeout.
 */
public class RequestTimeoutException extends ClientException {
    /**
     * The ID of the request that timed out.
     */
    private final String requestId;

    /**
     * The timeout in milliseconds that was exceeded.
     */
    private final long timeoutMillis;

    /**
     * Construct a new RequestTimeoutException instance.
     *
     * @param requestId     The ID of the request that timed out.
     * @param timeoutMillis The timeout in milliseconds that was exceeded.
     */
    public RequestTimeoutException(String requestId, long timeoutMillis) {
        super("Request " + requestId + " timed out after " + timeoutMillis + "ms");
        this.requestId = requestId;
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * Get the ID of the request that timed out.
     *
     * @return The request ID.
     */
    public String getRequestId() {
        return this.requestId;
    }

    /**
     * Get the timeout in milliseconds that was exceeded.
     *
     * @return The timeout in milliseconds.
     */
    public long getTimeoutMillis() {
        return this.timeoutMillis;
    }
}
