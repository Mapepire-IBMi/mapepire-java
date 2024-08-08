package io.github.mapapire.types;

import java.util.List;

public class QueryResult<T> extends ServerResponse {
    private QueryMetaData metadata;
    private boolean is_done;
    private boolean has_results;
    private int update_count;
    private List<T> data;

    public QueryResult() {
        super();
    }

    public QueryResult(String id, boolean success, String error, int sql_rc, String sql_state,
            QueryMetaData metadata, boolean is_done, boolean has_results, int update_count, List<T> data) {
        super(id, success, error, sql_rc, sql_state);
        this.metadata = metadata;
        this.is_done = is_done;
        this.has_results = has_results;
        this.update_count = update_count;
        this.data = data;
    }

    public QueryMetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(QueryMetaData metadata) {
        this.metadata = metadata;
    }

    public boolean isDone() {
        return is_done;
    }

    public void setIsDone(boolean is_done) {
        this.is_done = is_done;
    }

    public boolean hasResults() {
        return has_results;
    }

    public void setHasResults(boolean has_results) {
        this.has_results = has_results;
    }

    public int getUpdateCount() {
        return update_count;
    }

    public void setUpdateCount(int update_count) {
        this.update_count = update_count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}