package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a trace data request.
 */
public class GetTraceDataResult extends ServerResponse {
    /**
     * The retrieved trace data.
     */
    @JsonProperty("tracedata")
    private String tracedata;

    /**
     * Construct a new GetTraceDataResult instance.
     */
    public GetTraceDataResult() {
        super();
    }

    /**
     * Construct a new GetTraceDataResult instance.
     * 
     * @param id       The unique identifier for the request.
     * @param success  Whether the request was successful.
     * @param error    The error message, if any.
     * @param sqlRc    The SQL return code.
     * @param sqlState The SQL state code.
     * @param tracedata The retrieved trace data.
     */
    public GetTraceDataResult(String id, boolean success, String error, int sqlRc, String sqlState,
            String tracedata) {
        super(id, success, error, sqlRc, sqlState);
        this.tracedata = tracedata;
    }

    /**
     * Get the retrieved trace data.
     * 
     * @return The retrieved trace data.
     */
    public String getTracedata() {
        return tracedata;
    }

    /**
     * Set the retrieved trace data.
     * @param tracedata The retrieved trace data.
     */
    public void setTracedata(String tracedata) {
        this.tracedata = tracedata;
    }
}
