package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTraceDataResult extends ServerResponse {
    @JsonProperty("tracedata")
    private String tracedata;

    public GetTraceDataResult() {
        super();
    }

    public GetTraceDataResult(String id, boolean success, String error, int sqlRc, String sqlState,
            String _tracedata) {
        super(id, success, error, sqlRc, sqlState);
        this.tracedata = _tracedata;
    }

    public String getTracedata() {
        return tracedata;
    }

    public void setTracedata(String tracedata) {
        this.tracedata = tracedata;
    }
}
