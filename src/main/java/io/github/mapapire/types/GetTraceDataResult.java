package io.github.mapapire.types;

public class GetTraceDataResult extends ServerResponse {
    private String tracedata;

    public GetTraceDataResult() {
        super();
    }

    public GetTraceDataResult(String id, boolean success, String error, int sql_rc, String sql_state,
            String _tracedata) {
        super(id, success, error, sql_rc, sql_state);
        this.tracedata = _tracedata;
    }

    public String getTracedata() {
        return tracedata;
    }

    public void setTracedata(String tracedata) {
        this.tracedata = tracedata;
    }
}