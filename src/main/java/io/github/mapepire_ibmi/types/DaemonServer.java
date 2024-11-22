package io.github.mapepire_ibmi.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a DB2 server daemon with connection details.
 */
public class DaemonServer {
    /**
     * The hostname or IP address of the server.
     */
    @JsonProperty("host")
    private String host;

    /**
     * The port number to connect to. The default port is 8076.
     */
    @JsonProperty("port")
    private int port = 8076;

    /**
     * The username for authentication.
     */
    @JsonProperty("user")
    private String user;

    /**
     * The password for authentication.
     */
    @JsonProperty("password")
    private String password;

    /**
     * Whether to ignore unauthorized certificates.
     */
    @JsonProperty("rejectUnauthorized")
    private boolean rejectUnauthorized = true;

    /**
     * The certificate authority (CA) for validating the server's certificate.
     */
    @JsonProperty("ca")
    private String ca;

    /**
     * Construct a new DaemonServer instance.
     */
    public DaemonServer() {

    }

    /**
     * Construct a new DaemonServer instance.
     * 
     * @param host               The hostname or IP address of the server.
     * @param port               The port number to connect to.
     * @param user               The username for authentication.
     * @param password           The password for authentication.
     * @param rejectUnauthorized Whether to ignore unauthorized certificates.
     * @param ca                 The certificate authority (CA) for validating the
     *                           server's certificate.
     */
    public DaemonServer(String host, int port, String user, String password, boolean rejectUnauthorized,
            String ca) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.rejectUnauthorized = rejectUnauthorized;
        this.ca = ca;
    }

    /**
     * Get the hostname or IP address of the server.
     * 
     * @return The hostname or IP address of the server.
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the hostname or IP address of the server.
     * 
     * @param host The hostname or IP address of the server.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Get port number to connect to.
     * 
     * @return The port number to connect to.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set port number to connect to.
     * 
     * @param port The port number to connect to.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get username for authentication.
     * 
     * @return The username for authentication.
     */
    public String getUser() {
        return user;
    }

    /**
     * Set username for authentication.
     * 
     * @param user The username for authentication.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get password for authentication.
     * 
     * @return The password for authentication.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password for authentication.
     * 
     * @param password The password for authentication.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get whether to ignore unauthorized certificates.
     * 
     * @return Whether to ignore unauthorized certificates.
     */
    public boolean getRejectUnauthorized() {
        return rejectUnauthorized;
    }

    /**
     * Set whether to ignore unauthorized certificates.
     * 
     * @param rejectUnauthorized Whether to ignore unauthorized certificates.
     */
    public void setRejectUnauthorized(boolean rejectUnauthorized) {
        this.rejectUnauthorized = rejectUnauthorized;
    }

    /**
     * Get the certificate authority (CA) for validating the server's certificate.
     * 
     * @return The certificate authority (CA) for validating the server's
     *         certificate.
     */
    public String getCa() {
        return ca;
    }

    /**
     * Set the certificate authority (CA) for validating the server's certificate.
     * 
     * @param ca The certificate authority (CA) for validating the server's
     *           certificate.
     */
    public void setCa(String ca) {
        this.ca = ca;
    }
}
