package io.github.mapepire_ibmi.types;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a CL command execution through SQL.
 */
public class SqlCLCommandResult extends ServerResponse {
    /**
     * The log entries generated during the execution of the command.
     */
    @JsonProperty("data")
    private List<CLMessage> data = new ArrayList<CLMessage>();

    /**
     * Construct a new SqlCLCommandResult instance.
     */
    public SqlCLCommandResult() {
        super();
    }

    /**
     * Construct a new SqlCLCommandResult instance.
     * 
     * @param id            The unique identifier for the request.
     * @param success       Whether the request was successful.
     * @param error         The error message, if any.
     * @param sqlRc         The SQL return code, if any.
     * @param sqlState      The SQL state code, if any.
     * @param executionTime The execution time in milliseconds.
     * @param data          The log entries generated during the execution of the
     *                      command.
     */
    public SqlCLCommandResult(String id, boolean success, String error, Integer sqlRc, String sqlState,
            long executionTime, List<CLMessage> data) {
        super(id, success, error, sqlRc, sqlState, executionTime);
        this.data = data;
    }

    /**
     * Get the log entries generated during the execution of the command.
     * 
     * @return The log entries generated during the execution of the command.
     */
    public List<CLMessage> getData() {
        return data;
    }

    /**
     * Set the log entries generated during the execution of the command.
     * 
     * @param data The log entries generated during the execution of the command.
     */
    public void setData(List<CLMessage> data) {
        this.data = data;
    }
}
