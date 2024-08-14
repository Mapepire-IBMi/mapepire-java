package io.github.mapapire.types;

public class ServerResponse {
    private String id;
    private boolean success;
    private String error;
    private int sql_rc;
    private String sql_state;

    public ServerResponse() {

    }

    public ServerResponse(String id, boolean success, String error, int sql_rc, String sql_state) {
        this.id = id;
        this.success = success;
        this.error = error;
        this.sql_rc = sql_rc;
        this.sql_state = sql_state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
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
        return sql_rc;
    }

    public void setSqlRc(int sql_rc) {
        this.sql_rc = sql_rc;
    }

    public String getSqlState() {
        return sql_state;
    }

    public void setSqlState(String sql_state) {
        this.sql_state = sql_state;
    }
}
