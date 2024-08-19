package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConnectionResult extends ServerResponse {
    @JsonProperty("job")
    private String job;

    public ConnectionResult() {
        super();
    }

    public ConnectionResult(String id, boolean success, String error, int sqlRc, String sqlState, String job) {
        super(id, success, error, sqlRc, sqlState);
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
