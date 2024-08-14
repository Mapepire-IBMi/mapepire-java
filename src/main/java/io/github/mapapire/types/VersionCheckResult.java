package io.github.mapapire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionCheckResult extends ServerResponse {
    @JsonProperty("buildDate")
    private String buildDate;

    @JsonProperty("version")
    private String version;

    public VersionCheckResult(String id, boolean success, String error, int sqlRc, String sqlState,
            String buildDate, String version) {
        super(id, success, error, sqlRc, sqlState);
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
