package io.github.mapepire.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DaemonServer {
    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private int port;

    @JsonProperty("user")
    private String user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("ignoreUnauthorized")
    private boolean ignoreUnauthorized;

    @JsonProperty("ca")
    private String ca;

    public DaemonServer() {

    }

    public DaemonServer(String host, int port, String user, String password, boolean ignoreUnauthorized,
            String ca) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.ignoreUnauthorized = ignoreUnauthorized;
        this.ca = ca;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIgnoreUnauthorized() {
        return ignoreUnauthorized;
    }

    public void setIgnoreUnauthorized(boolean ignoreUnauthorized) {
        this.ignoreUnauthorized = ignoreUnauthorized;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }
}
