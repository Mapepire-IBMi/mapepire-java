package io.github.mapepire_ibmi.types;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a CL command execution.
 */
public class CLCommandResult extends ServerResponse {
    /**
     * The log entries generated during the execution of the job.
     */
    @JsonProperty("joblog")
    private List<JobLogEntry> joblog = new ArrayList<JobLogEntry>();

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
     * @param sqlRc         The SQL return code.
     * @param sqlState      The SQL state code.
     * @param executionTime The execution time in milliseconds.
     * @param joblog        The log entries generated during the execution of the
     *                      job.
     */
    public CLCommandResult(String id, boolean success, String error, int sqlRc, String sqlState,
            long executionTime, List<JobLogEntry> joblog) {
        super(id, success, error, sqlRc, sqlState, executionTime);
        this.joblog = joblog;
    }

    /**
     * Get the log entries generated during the execution of the job.
     * 
     * @return The log entries generated during the execution of the job.
     */
    public List<JobLogEntry> getJoblog() {
        return joblog;
    }

    /**
     * Set the log entries generated during the execution of the job.
     * 
     * @param joblog The log entries generated during the execution of the job.
     */
    public void setJoblog(List<JobLogEntry> joblog) {
        this.joblog = joblog;
    }
}
