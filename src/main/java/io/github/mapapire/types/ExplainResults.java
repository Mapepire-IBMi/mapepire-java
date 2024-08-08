package io.github.mapapire.types;

import java.util.List;

public class ExplainResults<T> extends QueryResult<T> {
    private QueryMetaData vemetadata;
    private Object vedata;

    public ExplainResults() {
        super();
    }

    public ExplainResults(String id, boolean success, String error, int sql_rc, String sql_state,
            QueryMetaData metadata, boolean is_done, boolean has_results, int update_count, List<T> data,
            QueryMetaData vemetadata, Object vedata) {
        super(id, success, error, sql_rc, sql_state, metadata, is_done, has_results, update_count, data);
        this.vemetadata = vemetadata;
        this.vedata = vedata;
    }

    public QueryMetaData getVemetadata() {
        return vemetadata;
    }

    public void setVemetadata(QueryMetaData vemetadata) {
        this.vemetadata = vemetadata;
    }

    public Object getVedata() {
        return vedata;
    }

    public void setVedata(Object vedata) {
        this.vedata = vedata;
    }
}
