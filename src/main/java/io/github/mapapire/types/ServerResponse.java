package io.github.mapapire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("error")
    private String error;

    @JsonProperty("sql_rc")
    private int sqlRc;

    @JsonProperty("sql_state")
    private String sqlState;

    public ServerResponse() {

    }

    public ServerResponse(String id, boolean success, String error, int sqlRc, String sqlState) {
        this.id = id;
        this.success = success;
        this.error = error;
        this.sqlRc = sqlRc;
        this.sqlState = sqlState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getSqlRc() {
        return sqlRc;
    }

    public void setSqlRc(int sqlRc) {
        this.sqlRc = sqlRc;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }
}
