package io.github.mapapire.types;

public class VersionCheckResult extends ServerResponse {
    private String buildDate;
    private String version;

    public VersionCheckResult(String id, boolean success, String error, int sql_rc, String sql_state,
            String buildDate, String version) {
        super(id, success, error, sql_rc, sql_state);
        this.buildDate = buildDate;
        this.version = version;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
