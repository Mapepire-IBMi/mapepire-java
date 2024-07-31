package io.github.mapapire.types;

public class VersionCheckResult extends ServerResponse {
    private final String buildDate;
    private final String version;

    public VersionCheckResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            String _buildDate, String _version) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.buildDate = _buildDate;
        this.version = _version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public String getVersion() {
        return version;
    }
}
