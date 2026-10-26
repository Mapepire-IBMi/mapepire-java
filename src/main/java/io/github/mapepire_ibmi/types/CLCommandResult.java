package io.github.mapepire_ibmi.types;

import java.util.List;

/**
 * Represents the result of a CL command execution through CL.
 * SQL return code and SQL state are not part of this result.
 */
public class CLCommandResult extends SqlCLCommandResult {
    /**
     * Construct a new CLCommandResult instance.
     */
    public CLCommandResult() {
        super();
    }

    /**
     * Construct a new CLCommandResult instance.
     * 
     * @param id            The unique identifier for the request.
     * @param success       Whether the request was successful.
     * @param error         The error message, if any.
     * @param executionTime The execution time in milliseconds.
     * @param data          The log entries generated during the execution of the
     *                      command.
     */
    public CLCommandResult(String id, boolean success, String error, long executionTime, List<CLMessage> data) {
        super(id, success, error, null, null, executionTime, data);
    }
}
