package io.github.mapepire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the results of an explain request.
 */
public class ExplainResults<T> extends QueryResult<T> {
    /**
     * The metadata about the query execution.
     */
    @JsonProperty("vemetadata")
    private QueryMetadata vemetadata;

    /**
     * The data returned from the explain request.
     */
    @JsonProperty("vedata")
    private Object vedata;

    /**
     * Construct a new ExplainResults instance.
     */
    public ExplainResults() {
        super();
    }

    /**
     * Construct a new ExplainResults instance.
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
     * @param vemetadata  The metadata about the query execution.
     * @param vedata      The data returned from the explain request.
     */
    public ExplainResults(String id, boolean success, String error, int sqlRc, String sqlState,
            QueryMetadata metadata, boolean isDone, boolean hasResults, int updateCount, List<T> data,
            QueryMetadata vemetadata, Object vedata) {
        super(id, success, error, sqlRc, sqlState, metadata, isDone, hasResults, updateCount, data);
        this.vemetadata = vemetadata;
        this.vedata = vedata;
    }

    /**
     * Get the metadata about the query execution.
     * @return The metadata about the query execution.
     */
    public QueryMetadata getVemetadata() {
        return vemetadata;
    }

    /**
     * Set the metadata about the query execution.
     * @param vemetadata The metadata about the query execution.
     */
    public void setVemetadata(QueryMetadata vemetadata) {
        this.vemetadata = vemetadata;
    }

    /**
     * Get the data returned from the explain request.
     * @return The data returned from the explain request.
     */
    public Object getVedata() {
        return vedata;
    }

    /**
     * Set the data returned from the explain request.
     * @param vedata The data returned from the explain request.
     */
    public void setVedata(Object vedata) {
        this.vedata = vedata;
    }
}
