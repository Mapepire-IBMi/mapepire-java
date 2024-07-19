package io.github.mapapire.types;

public class ServerResponse {
    private final String id;
    private final boolean success;
    private final String error;
    private final int sql_rc;
    private final String sql_state;

    public ServerResponse(String _id, boolean _success, String _error, int _sql_rc, String _sql_state) {
        this.id = _id;
        this.success = _success;
        this.error = _error;
        this.sql_rc = _sql_rc;
        this.sql_state = _sql_state;
    }
}