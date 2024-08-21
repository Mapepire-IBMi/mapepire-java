package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a version check.
 */
public class VersionCheckResult extends ServerResponse {
    /**
     * The build date of the version.
     */
    @JsonProperty("buildDate")
    private String buildDate;

    /**
     * The version string.
     */
    @JsonProperty("version")
    private String version;

    /**
     * Construct a new VersionCheckResult instance.
     */
    public VersionCheckResult() {

    }

    /**
     * Construct a new VersionCheckResult instance.
     * @param id        The unique identifier for the request.
     * @param success   Whether the request was successful.
     * @param error     The error message, if any.
     * @param sqlRc     The SQL return code.
     * @param sqlState  The SQL state code.
     * @param buildDate The build date of the version.
     * @param version   The version string.
     */
    public VersionCheckResult(String id, boolean success, String error, int sqlRc, String sqlState,
            String buildDate, String version) {
        super(id, success, error, sqlRc, sqlState);
        this.buildDate = buildDate;
        this.version = version;
    }

    /**
     * Get the build date of the version.
     * @return The build date of the version.
     */
    public String getBuildDate() {
        return buildDate;
    }

    /**
     * Set the build date of the version.
     * @param buildDate The build date of the version.
     */
    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    /**
     * Get the version string.
     * @return The version string.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version string.
     * @param version The version string.
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
