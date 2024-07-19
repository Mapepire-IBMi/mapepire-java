package io.github.mapapire.types;

public enum ServerTraceLevel {
    OFF("OFF"), // off
    ON("ON"), // all except datastream
    ERRORS("ERRORS"), // errors only
    DATASTREAM("DATASTREAM"); // all including datastream

    private String serverTraceLevel;

    ServerTraceLevel(String _serverTraceLevel) {
        this.serverTraceLevel = _serverTraceLevel;
    }
}