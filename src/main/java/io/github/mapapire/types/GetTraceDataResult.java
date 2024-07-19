package io.github.mapapire.types;

public class GetTraceDataResult extends ServerResponse {
    private final String tracedata;

    public GetTraceDataResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            String _tracedata) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.tracedata = _tracedata;
    }
}