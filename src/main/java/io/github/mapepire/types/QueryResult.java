package io.github.mapepire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standard query result.
 */
public class QueryResult<T> extends ServerResponse {
    /**
     * The metadata about the query results.
     */
    @JsonProperty("metadata")
    private QueryMetadata metadata;

    /**
     * Whether the query execution is complete.
     */
    @JsonProperty("is_done")
    private boolean isDone;

    /**
     * Whether the results were returned.
     */
    @JsonProperty("has_results")
    private boolean hasResults;

    /**
     * The number of rows affected by the query.
     */
    @JsonProperty("update_count")
    private int updateCount;

    /**
     * The data returned from the query.
     */
    @JsonProperty("data")
    private List<T> data;

    /**
     * Construct a new QueryResult instance.
     */
    public QueryResult() {
        super();
    }

    /**
     * Construct a new QueryResult instance.
     * 
     * @param id          The unique identifier for the request.
     * @param success     Whether the request was successful.
     * @param error       The error message, if any.
     * @param sqlRc       The SQL return code.
     * @param sqlState    The SQL state code.
     * @param metadata    The metadata about the query results.
     * @param isDone      Whether the query execution is complete.
     * @param hasResults  Whether the results were returned.
     * @param updateCount The number of rows affected by the query.
     * @param data        The data returned from the query.
     */
    public QueryResult(String id, boolean success, String error, int sqlRc, String sqlState,
            QueryMetadata metadata, boolean isDone, boolean hasResults, int updateCount, List<T> data) {
        super(id, success, error, sqlRc, sqlState);
        this.metadata = metadata;
        this.isDone = isDone;
        this.hasResults = hasResults;
        this.updateCount = updateCount;
        this.data = data;
    }

    /**
     * Get the metadata about the query results.
     * 
     * @return The metadata about the query results.
     */
    public QueryMetadata getMetadata() {
        return metadata;
    }

    /**
     * Set the metadata about the query results.
     * 
     * @param metadata The metadata about the query results.
     */
    public void setMetadata(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get whether the query execution is complete.
     * 
     * @return Whether the query execution is complete.
     */
    public boolean getIsDone() {
        return isDone;
    }

    /**
     * Set whether the query execution is complete.
     * 
     * @param isDone Whether the query execution is complete.
     */
    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Get whether the results were returned.
     * 
     * @return Whether the results were returned.
     */
    public boolean getHasResults() {
        return hasResults;
    }

    /**
     * Set whether the results were returned.
     * 
     * @param hasResults Whether the results were returned.
     */
    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }

    /**
     * Get the number of rows affected by the query.
     * 
     * @return The number of rows affected by the query.
     */
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * Set the number of rows affected by the query.
     * 
     * @param updateCount The number of rows affected by the query.
     */
    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    /**
     * Get the data returned from the query.
     * 
     * @return The data returned from the query.
     */
    public List<T> getData() {
        return data;
    }

    /**
     * Set the data returned from the query.
     * 
     * @param data The data returned from the query.
     */
    public void setData(List<T> data) {
        this.data = data;
    }
}
