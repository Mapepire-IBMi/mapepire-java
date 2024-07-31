package io.github.mapapire.types;

public class ConnectionResult extends ServerResponse {
    private final String job;

    public ConnectionResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state, String _job) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.job = _job;
    }

    public String getJob() {
        return job;
    }
}
