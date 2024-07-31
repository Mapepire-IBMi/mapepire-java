package io.github.mapapire.types;

import java.util.List;

public class ExplainResults<T> extends QueryResult<T> {
    private final QueryMetaData vemetadata;
    private final Object vedata;

    public ExplainResults(String _id, boolean _success, String _error, int _sql_rc, String _sql_state,
            QueryMetaData _metadata, boolean _is_done, boolean _has_results, int _update_count, List<T> _data,
            QueryMetaData _vemetadata, Object _vedata) {
        super(_id, _success, _error, _sql_rc, _sql_state, _metadata, _is_done, _has_results, _update_count, _data);
        this.vemetadata = _vemetadata;
        this.vedata = _vedata;
    }

    public QueryMetaData getVemetadata() {
        return vemetadata;
    }

    public Object getVedata() {
        return vedata;
    }
}
