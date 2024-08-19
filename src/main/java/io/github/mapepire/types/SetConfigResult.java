package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SetConfigResult extends ServerResponse {
    @JsonProperty("tracedest")
    private ServerTraceDest tracedest;

    @JsonProperty("tracelevel")
    private ServerTraceLevel tracelevel;

    public SetConfigResult() {
        super();
    }

    public SetConfigResult(String id, boolean success, String error, int sqlRc, String sqlState,
            ServerTraceDest tracedest, ServerTraceLevel tracelevel) {
        super(id, success, error, sqlRc, sqlState);
        this.tracedest = tracedest;
        this.tracelevel = tracelevel;
    }

    public ServerTraceDest getTracedest() {
        return tracedest;
    }

    public void setTracedest(ServerTraceDest tracedest) {
        this.tracedest = tracedest;
    }

    public ServerTraceLevel getTracelevel() {
        return tracelevel;
    }

    public void setTracelevel(ServerTraceLevel tracelevel) {
        this.tracelevel = tracelevel;
    }
}
