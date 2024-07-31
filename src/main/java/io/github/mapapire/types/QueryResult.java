package io.github.mapapire.types;

import java.util.List;

public class QueryResult<T> extends ServerResponse {
    private final QueryMetaData metadata;
    private final boolean is_done;
    private final boolean has_results;
    private final int update_count;
    private final List<T> data;

    public QueryResult(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            QueryMetaData _metadata, boolean _is_done, boolean _has_results, int _update_count, List<T> _data) {
        super(_id, _success, _error, _sql_rc, _sql_state);
        this.metadata = _metadata;
        this.is_done = _is_done;
        this.has_results = _has_results;
        this.update_count = _update_count;
        this.data = _data;
    }

    public QueryMetaData getMetadata() {
        return metadata;
    }

    public boolean isDone() {
        return is_done;
    }

    public boolean hasResults() {
        return has_results;
    }

    public int getUpdateCount() {
        return update_count;
    }

    public List<T> getData() {
        return data;
    }
}