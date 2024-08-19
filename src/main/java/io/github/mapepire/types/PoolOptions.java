package io.github.mapepire.types;

/**
 * Represents the options for configuring a connection pool.
 */
public class PoolOptions {
    /**
     * The credentials required to connect to the daemon server.
     */
    private DaemonServer creds;

    /**
     * The JDBC options for configuring the connection. These options may
     * include settings such as connection timeout, SSL settings, etc.
     */
    private JDBCOptions opts;

    /**
     * The maximum number of connections allowed in the pool. This defines
     * the upper limit on the number of active connections.
     */
    private int maxSize;

    /**
     * The number of connections to create when the pool is initialized.
     * This determines the starting size of the connection pool.
     */
    private int startingSize;

    /**
     * Constructs a new PoolOptions instance.
     * 
     * @param creds        The credentials required to connect to the daemon server.
     * @param opts         The JDBC options for configuring the connection.
     * @param maxSize      The maximum number of connections allowed in the pool.
     * @param startingSize The number of connections to create when the pool
     *                     is initialized.
     */
    public PoolOptions(DaemonServer creds, JDBCOptions opts, int maxSize, int startingSize) {
        this.creds = creds;
        this.opts = opts;
        this.maxSize = maxSize;
        this.startingSize = startingSize;
    }

    /**
     * Constructs a new PoolOptions instance.
     * 
     * @param creds        The credentials required to connect to the daemon server.
     * @param maxSize      The maximum number of connections allowed in the pool.
     * @param startingSize The number of connections to create when the pool
     *                     is initialized.
     */
    public PoolOptions(DaemonServer creds, int maxSize, int startingSize) {
        this.creds = creds;
        this.opts = new JDBCOptions();
        this.maxSize = maxSize;
        this.startingSize = startingSize;
    }

    /**
     * Get the credentials required to connect to the daemon server.
     * 
     * @return The credentials required to connect to the daemon server.
     */
    public DaemonServer getCreds() {
        return creds;
    }

    /**
     * Set the credentials required to connect to the daemon server.
     * 
     * @param creds The credentials required to connect to the daemon server.
     */
    public void setCreds(DaemonServer creds) {
        this.creds = creds;
    }

    /**
     * Get the JDBC options for configuring the connection.
     * 
     * @return The JDBC options for configuring the connection.
     */
    public JDBCOptions getOpts() {
        return opts;
    }

    /**
     * Set the JDBC options for configuring the connection.
     * 
     * @param opts The JDBC options for configuring the connection.
     */
    public void setOpts(JDBCOptions opts) {
        this.opts = opts;
    }

    /**
     * Get the maximum number of connections allowed in the pool.
     * 
     * @return The maximum number of connections allowed in the pool.
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Set the maximum number of connections allowed in the pool.
     * 
     * @param maxSize The maximum number of connections allowed in the pool.
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Get the number of connections to create when the pool is initialized.
     * 
     * @return The number of connections to create when the pool is initialized.
     */
    public int getStartingSize() {
        return startingSize;
    }

    /**
     * Set the number of connections to create when the pool is initialized.
     * 
     * @param startingSize The number of connections to create when the pool is
     *                     initialized.
     */
    public void setStartingSize(int startingSize) {
        this.startingSize = startingSize;
    }
}