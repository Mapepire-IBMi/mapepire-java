package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a trace data request.
 */
public class GetTraceDataResult extends ServerResponse {
    /**
     * The trace data.
     */
    @JsonProperty("tracedata")
    private String traceData;

    /**
     * The JTOpen trace data.
     */
    @JsonProperty("jtopentracedata")
    private String jtOpenTraceData;

    /**
     * Construct a new GetTraceDataResult instance.
     */
    public GetTraceDataResult() {
        super();
    }

    /**
     * Construct a new GetTraceDataResult instance.
     * 
     * @param id              The unique identifier for the request.
     * @param success         Whether the request was successful.
     * @param error           The error message, if any.
     * @param sqlRc           The SQL return code.
     * @param sqlState        The SQL state code.
     * @param traceData       The trace data.
     * @param jtOpenTraceData The JTOpen trace data.
     */
    public GetTraceDataResult(String id, boolean success, String error, int sqlRc, String sqlState,
            String traceData, String jtOpenTraceData) {
        super(id, success, error, sqlRc, sqlState);
        this.traceData = traceData;
        this.jtOpenTraceData = jtOpenTraceData;
    }

    /**
     * Get the trace data.
     * 
     * @return The trace data.
     */
    public String getTraceData() {
        return traceData;
    }

    /**
     * Set the trace data.
     * 
     * @param traceData The trace data.
     */
    public void setTraceData(String traceData) {
        this.traceData = traceData;
    }

    /**
     * Get the JTOpen trace data.
     * 
     * @return The JTOpen trace data.
     */
    public String getJtOpenTraceData() {
        return jtOpenTraceData;
    }

    /**
     * Set the JTOpen trace data.
     * 
     * @param jtOpenTraceData The JTOpen trace data.
     */
    public void setJtOpenTraceData(String jtOpenTraceData) {
        this.jtOpenTraceData = jtOpenTraceData;
    }
}
