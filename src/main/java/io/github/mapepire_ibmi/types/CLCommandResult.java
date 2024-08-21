package io.github.mapepire_ibmi.types;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CLCommandResult extends ServerResponse {
    @JsonProperty("joblog")
    private List<JobLogEntry> joblog = new ArrayList<JobLogEntry>();

    public CLCommandResult() {
        super();
    }

    public CLCommandResult(String id, boolean success, String error, int sqlRc, String sqlState,
            List<JobLogEntry> joblog) {
        super(id, success, error, sqlRc, sqlState);
        this.joblog = joblog;
    }

    public List<JobLogEntry> getJoblog() {
        return joblog;
    }

    public void setJoblog(List<JobLogEntry> joblog) {
        this.joblog = joblog;
    }
}
