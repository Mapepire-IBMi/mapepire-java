package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a connection request.
 */
public class SetConfigResult extends ServerResponse {
    /**
     * The destination for trace data.
     */
    @JsonProperty("tracedest")
    private ServerTraceDest tracedest;

    /**
     * The level of tracing set on the server.
     */
    @JsonProperty("tracelevel")
    private ServerTraceLevel tracelevel;

    /**
     * Constructs a new SetConfigResult instance.
     */
    public SetConfigResult() {
        super();
    }

    /**
     * Constructs a new SetConfigResult instance.
     * 
     * @param id         The unique identifier for the request.
     * @param success    Whether the request was successful.
     * @param error      The error message, if any.
     * @param sqlRc      The SQL return code.
     * @param sqlState   The SQL state code.
     * @param tracedest  The destination for trace data.
     * @param tracelevel The level of tracing set on the server.
     */
    public SetConfigResult(String id, boolean success, String error, int sqlRc, String sqlState,
            ServerTraceDest tracedest, ServerTraceLevel tracelevel) {
        super(id, success, error, sqlRc, sqlState);
        this.tracedest = tracedest;
        this.tracelevel = tracelevel;
    }

    /**
     * Get the destination for trace data.
     * 
     * @return The destination for trace data.
     */
    public ServerTraceDest getTracedest() {
        return tracedest;
    }

    /**
     * Set the destination for trace data.
     * 
     * @param tracedestThe destination for trace data.
     */
    public void setTracedest(ServerTraceDest tracedest) {
        this.tracedest = tracedest;
    }

    /**
     * Get the level of tracing set on the server.
     * 
     * @return The level of tracing set on the server.
     */
    public ServerTraceLevel getTracelevel() {
        return tracelevel;
    }

    /**
     * Set the level of tracing set on the server.
     * 
     * @param tracelevel The level of tracing set on the server.
     */
    public void setTracelevel(ServerTraceLevel tracelevel) {
        this.tracelevel = tracelevel;
    }
}
