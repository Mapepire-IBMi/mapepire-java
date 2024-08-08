package io.github.mapapire.types;

public class SetConfigResult extends ServerResponse {
    private ServerTraceDest tracedest;
    private ServerTraceLevel tracelevel;

    public SetConfigResult() {
        super();
    }

    public SetConfigResult(String id, boolean success, String error, int sql_rc, String sql_state,
            ServerTraceDest tracedest, ServerTraceLevel tracelevel) {
        super(id, success, error, sql_rc, sql_state);
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