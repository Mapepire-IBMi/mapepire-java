package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standard server response.
 */
public class ServerResponse {
    /**
     * The unique identifier for the request.
     */
    @JsonProperty("id")
    private String id;

    /**
     * Whether the request was successful.
     */
    @JsonProperty("success")
    private boolean success;

    /**
     * The error message, if any.
     */
    @JsonProperty("error")
    private String error;

    /**
     * The SQL return code.
     */
    @JsonProperty("sql_rc")
    private int sqlRc;

    /**
     * The SQL state code.
     */
    @JsonProperty("sql_state")
    private String sqlState;

    /**
     * Construct a new ServerResponse instance.
     */
    public ServerResponse() {

    }

    /**
     * Construct a new ServerResponse instance.
     * @param id       The unique identifier for the request.
     * @param success  Whether the request was successful.
     * @param error    The error message, if any.
     * @param sqlRc    The SQL return code.
     * @param sqlState The SQL state code.
     */
    public ServerResponse(String id, boolean success, String error, int sqlRc, String sqlState) {
        this.id = id;
        this.success = success;
        this.error = error;
        this.sqlRc = sqlRc;
        this.sqlState = sqlState;
    }

    /**
     * Get the unique identifier for the request.
     * @return The unique identifier for the request.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique identifier for the request.
     * @param id The unique identifier for the request.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get whether the request was successful.
     * @return Whether the request was successful.
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Set whether the request was successful.
     * @param success Whether the request was successful.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Get the error message, if any.
     * @return The error message, if any.
     */
    public String getError() {
        return error;
    }

    /**
     * Set the error message, if any.
     * @param error The error message, if any.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Get the SQL return code.
     * @return The SQL return code.
     */
    public int getSqlRc() {
        return sqlRc;
    }

    /**
     * Set the SQL return code.
     * @param sqlRc The SQL return code.
     */
    public void setSqlRc(int sqlRc) {
        this.sqlRc = sqlRc;
    }

    /**
     * Get the SQL state code.
     * @return The SQL state code.
     */
    public String getSqlState() {
        return sqlState;
    }

    /**
     * Set the SQL state code.
     * @param sqlState The SQL state code.
     */
    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }
}
