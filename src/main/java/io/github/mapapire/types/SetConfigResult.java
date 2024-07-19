package io.github.mapapire.types;

public class SetConfigResult extends ServerResponse {
    private final ServerTraceDest tracedest;
    private final ServerTraceLevel tracelevel;

    public SetConfigResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            ServerTraceDest _tracedest, ServerTraceLevel _tracelevel) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.tracedest = _tracedest;
        this.tracelevel = _tracelevel;
    }
}