package io.github.mapepire;

import java.util.concurrent.CompletableFuture;

import io.github.mapepire.types.DaemonServer;

/**
 * Represents a TLS configuration.
 */
public class Tls {
    /**
     * Get the SSL/TLS certificate from a specified DB2 server.
     *
     * This function establishes a secure connection to the server and retrieves the
     * peer certificate information, which includes details about the server's
     * SSL/TLS certificate.
     * @param creds The server details for the connection.
     * @return A CompletableFuture that resolves to the detailed peer certificate
     *         information.
     */
    public CompletableFuture<String> getCertificate(DaemonServer creds) {
        // TODO: Add implementation
        // TODO: Add @throws - ts docs -> An error if the connection fails or if there
        // is an issue retrieving the certificate.
        return CompletableFuture.completedFuture(null);
    }
}
