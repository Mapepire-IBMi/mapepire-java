package io.github.mapapire.types;

import java.util.ArrayList;
import java.util.List;

public class CLCommandResult extends ServerResponse {
    private List<JobLogEntry> joblog = new ArrayList<JobLogEntry>();

    public CLCommandResult() {
        super();
    }

    public CLCommandResult(String id, boolean success, String error, int sql_rc, String sql_state,
            List<JobLogEntry> joblog) {
        super(id, success, error, sql_rc, sql_state);
        this.joblog = joblog;
    }

    public List<JobLogEntry> getJoblog() {
        return joblog;
    }

    public void setJoblog(List<JobLogEntry> joblog) {
        this.joblog = joblog;
    }
}
