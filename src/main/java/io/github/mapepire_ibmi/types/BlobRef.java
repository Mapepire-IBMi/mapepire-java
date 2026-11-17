package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reference to a BLOB value stored on the mapepire server, returned in query
 * result data when a BLOB or binary column is selected in daemon mode.
 *
 * <p>Retrieve the raw bytes by calling {@link io.github.mapepire_ibmi.SqlJob#fetchBlob(BlobRef)}
 * or by issuing an authenticated HTTP GET to
 * {@code https://<host>:<port><blob_url>}.
 *
 * <p>The token is <b>single-use</b> and expires after the server-configured TTL
 * (default 60 s, overridable via the {@code BLOB_TOKEN_TTL} environment
 * variable on the server).
 *
 * <p>In single mode (no HTTP server) BLOB columns are returned as inline
 * Base64 strings instead of a {@code BlobRef}.
 */
public class BlobRef {

    /**
     * Relative URL path for the blob, e.g. {@code "/blob/<token>"}.
     */
    @JsonProperty("blob_url")
    private String blobUrl;

    /**
     * Size of the blob in bytes.
     */
    @JsonProperty("size")
    private long size;

    /**
     * Construct a new BlobRef instance.
     */
    public BlobRef() {
    }

    /**
     * Construct a new BlobRef instance.
     *
     * @param blobUrl The relative URL path for the blob.
     * @param size    The size of the blob in bytes.
     */
    public BlobRef(String blobUrl, long size) {
        this.blobUrl = blobUrl;
        this.size = size;
    }

    /**
     * Get the relative URL path for the blob.
     *
     * @return The relative URL path for the blob.
     */
    public String getBlobUrl() {
        return blobUrl;
    }

    /**
     * Set the relative URL path for the blob.
     *
     * @param blobUrl The relative URL path for the blob.
     */
    public void setBlobUrl(String blobUrl) {
        this.blobUrl = blobUrl;
    }

    /**
     * Get the size of the blob in bytes.
     *
     * @return The size of the blob in bytes.
     */
    public long getSize() {
        return size;
    }

    /**
     * Set the size of the blob in bytes.
     *
     * @param size The size of the blob in bytes.
     */
    public void setSize(long size) {
        this.size = size;
    }
}
