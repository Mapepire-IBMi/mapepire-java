package io.github.mapapire.types;

import java.util.List;

public class QueryMetaData {
    private int column_count;
    private List<ColumnMetaData> columns;
    private String job;

    public QueryMetaData() {

    }

    public QueryMetaData(int column_count, List<ColumnMetaData> columns, String job) {
        this.column_count = column_count;
        this.columns = columns;
        this.job = job;
    }

    public int getColumnCount() {
        return column_count;
    }

    public void setColumnCount(int column_count) {
        this.column_count = column_count;
    }

    public List<ColumnMetaData> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetaData> columns) {
        this.columns = columns;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}