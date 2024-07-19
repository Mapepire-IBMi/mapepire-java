package io.github.mapapire.types;

import java.util.List;

public class CLCommandResult extends ServerResponse {
    private final List<JobLogEntry> joblog;

    public CLCommandResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            List<JobLogEntry> _joblog) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.joblog = _joblog;
    }
}