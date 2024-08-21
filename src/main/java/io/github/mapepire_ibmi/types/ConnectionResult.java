package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a connection request.
 */
public class ConnectionResult extends ServerResponse {
    /**
     * The unique job identifier for the connection.
     */
    @JsonProperty("job")
    private String job;

    /**
     * Construct a new ConnectionResult instance.
     */
    public ConnectionResult() {
        super();
    }

    /**
     * Construct a new ConnectionResult instance.
     * @param id       The unique identifier for the request.
     * @param success  Whether the request was successful.
     * @param error    The error message, if any.
     * @param sqlRc    The SQL return code.
     * @param sqlState The SQL state code.
     * @param job      The unique job identifier for the connection.
     */
    public ConnectionResult(String id, boolean success, String error, int sqlRc, String sqlState, String job) {
        super(id, success, error, sqlRc, sqlState);
        this.job = job;
    }

    /**
     * Get the unique job identifier for the connection.
     * @return The unique job identifier for the connection.
     */
    public String getJob() {
        return job;
    }

    /**
     * Set the unique job identifier for the connection.
     * @param job The unique job identifier for the connection.
     */
    public void setJob(String job) {
        this.job = job;
    }
}
