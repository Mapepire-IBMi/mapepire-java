package io.github.mapepire.types;

import io.github.mapepire.SqlJob;

/**
 * Represents the options for configuring an addition to a connection pool.
 */
public class PoolAddOptions {
    /**
     * An existing job to add to the pool.
     */
    private SqlJob existingJob;

    /**
     * Whether to not add to the pool.
     */
    private boolean poolIgnore;

    /**
     * Constructs a new PoolAddOptions instance.
     */
    public PoolAddOptions() {

    }

    /**
     * Constructs a new PoolAddOptions instance.
     * 
     * @param existingJob An existing job to add to the pool.
     * @param poolIgnore  Whether to not add to the pool.
     */
    public PoolAddOptions(SqlJob existingJob, boolean poolIgnore) {
        this.existingJob = existingJob;
        this.poolIgnore = poolIgnore;
    }

    /**
     * Get the existing job to add to the pool.
     * 
     * @return An existing job to add to the pool.
     */
    public SqlJob getExistingJob() {
        return existingJob;
    }

    /**
     * Set the existing job to add to the pool.
     * 
     * @param existingJob An existing job to add to the pool.
     */
    public void setExistingJob(SqlJob existingJob) {
        this.existingJob = existingJob;
    }

    /**
     * Get whether to not add to the pool.
     * 
     * @return Whether to not add to the pool
     */
    public boolean getPoolIgnore() {
        return poolIgnore;
    }

    /**
     * Set whether to not add to the pool.
     * 
     * @param poolIgnore Whether to not add to the pool
     */
    public void setPoolIgnore(boolean poolIgnore) {
        this.poolIgnore = poolIgnore;
    }
}