package io.github.mapapire.types;

public class DaemonServer {
    private String host;
    private int port;
    private String user;
    private String password;
    private boolean ignoreUnauthorized;
    private String ca;

    public DaemonServer() {

    }

    public DaemonServer(String host, int port, String user, String password, boolean ignoreUnauthorized,
            String _ca) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.ignoreUnauthorized = ignoreUnauthorized;
        this.ca = _ca;
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

    public boolean isIgnoreUnauthorized() {
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