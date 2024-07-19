package io.github.mapapire.types;

import java.util.List;

public class QueryMetaData {
    private final int column_count;
    private final List<ColumnMetaData> columns;
    private final String job;

    public QueryMetaData(int column_count, List<ColumnMetaData> _columns, String _job) {
        this.column_count = column_count;
        this.columns = _columns;
        this.job = _job;
    }
}