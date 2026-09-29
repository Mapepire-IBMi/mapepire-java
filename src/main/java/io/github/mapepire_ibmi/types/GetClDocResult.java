package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the result of a CL command documentation request.
 */
public class GetClDocResult extends ServerResponse {
    /**
     * The command help documentation in HTML format.
     */
    @JsonProperty("html")
    private String html;

    /**
     * The command help documentation in UIM format.
     */
    @JsonProperty("uim")
    private String uim;

    /**
     * Construct a new GetClDocResult instance.
     */
    public GetClDocResult() {
        super();
    }

    /**
     * Construct a new GetClDocResult instance.
     *
     * @param id            The unique identifier for the request.
     * @param success       Whether the request was successful.
     * @param error         The error message, if any.
     * @param sqlRc         The SQL return code.
     * @param sqlState      The SQL state code.
     * @param executionTime The execution time in milliseconds.
     * @param html          The command help documentation in HTML format.
     * @param uim           The command help documentation in UIM format.
     */
    public GetClDocResult(String id, boolean success, String error, int sqlRc, String sqlState,
            long executionTime, String html, String uim) {
        super(id, success, error, sqlRc, sqlState, executionTime);
        this.html = html;
        this.uim = uim;
    }

    /**
     * Get the command help documentation in HTML format.
     *
     * @return The command help documentation in HTML format.
     */
    public String getHtml() {
        return html;
    }

    /**
     * Set the command help documentation in HTML format.
     *
     * @param html The command help documentation in HTML format.
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * Get the command help documentation in UIM format.
     *
     * @return The command help documentation in UIM format.
     */
    public String getUim() {
        return uim;
    }

    /**
     * Set the command help documentation in UIM format.
     *
     * @param uim The command help documentation in UIM format.
     */
    public void setUim(String uim) {
        this.uim = uim;
    }
}
