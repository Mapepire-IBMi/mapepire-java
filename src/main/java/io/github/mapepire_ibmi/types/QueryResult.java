package io.github.mapepire_ibmi.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryResult<T> extends ServerResponse {
    @JsonProperty("metadata")
    private QueryMetadata metadata;

    @JsonProperty("is_done")
    private boolean isDone;

    @JsonProperty("has_results")
    private boolean hasResults;

    @JsonProperty("update_count")
    private int updateCount;

    @JsonProperty("data")
    private List<T> data;

    public QueryResult() {
        super();
    }

    public QueryResult(String id, boolean success, String error, int sqlRc, String sqlState,
            QueryMetadata metadata, boolean isDone, boolean hasResults, int updateCount, List<T> data) {
        super(id, success, error, sqlRc, sqlState);
        this.metadata = metadata;
        this.isDone = isDone;
        this.hasResults = hasResults;
        this.updateCount = updateCount;
        this.data = data;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean getHasResults() {
        return hasResults;
    }

    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
