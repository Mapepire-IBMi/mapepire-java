package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a connection request.
 */
public class SetConfigResult extends ServerResponse {
    /**
     * The server trace data destination.
     */
    @JsonProperty("tracedest")
    private String traceDest;

    /**
     * The server trace level.
     */
    @JsonProperty("tracelevel")
    private ServerTraceLevel traceLevel;

    /**
     * The JTOpen trace data destination.
     */
    @JsonProperty("jtopentracedest")
    private String jtOpenTraceDest;

    /**
     * The JTOpen trace level.
     */
    @JsonProperty("jtopentracelevel")
    private ServerTraceLevel jtOpenTraceLevel;

    /**
     * Construct a new SetConfigResult instance.
     */
    public SetConfigResult() {
        super();
    }

    /**
     * Construct a new SetConfigResult instance.
     * 
     * @param id               The unique identifier for the request.
     * @param success          Whether the request was successful.
     * @param error            The error message, if any.
     * @param sqlRc            The SQL return code.
     * @param sqlState         The SQL state code.
     * @param executionTime    The execution time in milliseconds.
     * @param traceDest        The server trace data destination.
     * @param traceLevel       The server trace level.
     * @param jtOpenTraceDest  The JTOpen trace data destination.
     * @param jtOpenTraceLevel The JTOpen trace level.
     */
    public SetConfigResult(String id, boolean success, String error, int sqlRc, String sqlState, long executionTime,
            String traceDest, ServerTraceLevel traceLevel, String jtOpenTraceDest,
            ServerTraceLevel jtOpenTraceLevel) {
        super(id, success, error, sqlRc, sqlState, executionTime);
        this.traceDest = traceDest;
        this.traceLevel = traceLevel;
        this.jtOpenTraceDest = jtOpenTraceDest;
        this.jtOpenTraceLevel = jtOpenTraceLevel;
    }

    /**
     * Get the server trace data destination.
     * 
     * @return The server trace data destination.
     */
    public String getTraceDest() {
        return traceDest;
    }

    /**
     * Set the server trace data destination.
     * 
     * @param traceDest The server trace data destination.
     */
    public void setTraceDest(String traceDest) {
        this.traceDest = traceDest;
    }

    /**
     * Get the server trace level.
     * 
     * @return The server trace level.
     */
    public ServerTraceLevel getTraceLevel() {
        return traceLevel;
    }

    /**
     * Set the server trace level.
     * 
     * @param traceLevel The server trace level.
     */
    public void setTraceLevel(ServerTraceLevel traceLevel) {
        this.traceLevel = traceLevel;
    }

    /**
     * Get the JTOpen trace data destination.
     * 
     * @return The JTOpen trace data destination.
     */
    public String getJtOpenTraceDest() {
        return jtOpenTraceDest;
    }

    /**
     * Set the JTOpen trace data destination.
     * 
     * @param jtOpenTraceDest The JTOpen trace data destination.
     */
    public void setJtOpenTraceDest(String jtOpenTraceDest) {
        this.jtOpenTraceDest = jtOpenTraceDest;
    }

    /**
     * Get the JTOpen trace level.
     * 
     * @return The JTOpen trace level.
     */
    public ServerTraceLevel getJtOpenTraceLevel() {
        return jtOpenTraceLevel;
    }

    /**
     * Set the JTOpen trace level.
     * 
     * @param jtOpenTraceLevel The JTOpen trace level.
     */
    public void setJtOpenTraceLevel(ServerTraceLevel jtOpenTraceLevel) {
        this.jtOpenTraceLevel = jtOpenTraceLevel;
    }
}
