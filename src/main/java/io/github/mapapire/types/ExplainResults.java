package io.github.mapapire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExplainResults<T> extends QueryResult<T> {
    @JsonProperty("vemetadata")
    private QueryMeta vemetadata;

    @JsonProperty("vedata")
    private Object vedata;

    public ExplainResults() {
        super();
    }

    public ExplainResults(String id, boolean success, String error, int sqlRc, String sqlState,
            QueryMeta metadata, boolean isDone, boolean hasResults, int updateCount, List<T> data,
            QueryMeta vemetadata, Object vedata) {
        super(id, success, error, sqlRc, sqlState, metadata, isDone, hasResults, updateCount, data);
        this.vemetadata = vemetadata;
        this.vedata = vedata;
    }

    public QueryMeta getVemetadata() {
        return vemetadata;
    }

    public void setVemetadata(QueryMeta vemetadata) {
        this.vemetadata = vemetadata;
    }

    public Object getVedata() {
        return vedata;
    }

    public void setVedata(Object vedata) {
        this.vedata = vedata;
    }
}
