package io.github.mapapire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryMetaData {
    @JsonProperty("column_count")
    private int columnCount;

    @JsonProperty("columns")
    private List<ColumnMetaData> columns;

    @JsonProperty("job")
    private String job;

    public QueryMetaData() {

    }

    public QueryMetaData(int columnCount, List<ColumnMetaData> columns, String job) {
        this.columnCount = columnCount;
        this.columns = columns;
        this.job = job;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
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
