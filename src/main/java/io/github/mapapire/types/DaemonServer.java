package io.github.mapapire.types;

public class DaemonServer {
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final boolean ignoreUnauthorized;
    private final String ca;

    public DaemonServer(String _host, int _port, String _user, String _password, boolean _ignoreUnauthorized,
            String _ca) {
        this.host = _host;
        this.port = _port;
        this.user = _user;
        this.password = _password;
        this.ignoreUnauthorized = _ignoreUnauthorized;
        this.ca = _ca;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isIgnoreUnauthorized() {
        return ignoreUnauthorized;
    }

    public String getCa() {
        return ca;
    }
}