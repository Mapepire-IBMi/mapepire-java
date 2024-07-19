package io.github.mapapire.types;

public enum ServerTraceDest {
    FILE("FILE"),
    IN_MEM("IN_MEM");

    private String serverTraceDest;

    ServerTraceDest(String _serverTraceDest) {
        this.serverTraceDest = _serverTraceDest;
    }
}
