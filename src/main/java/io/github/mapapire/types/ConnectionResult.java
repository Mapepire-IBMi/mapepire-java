package io.github.mapapire.types;

public class ConnectionResult extends ServerResponse {
    private String job;

    public ConnectionResult() {
        super();
    }

    public ConnectionResult(String id, boolean success, String error, int sql_rc, String sql_state, String job) {
        super(id, success, error, sql_rc, sql_state);
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
