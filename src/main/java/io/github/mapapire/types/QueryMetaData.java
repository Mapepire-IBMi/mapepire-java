package io.github.mapapire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryMetadata {
    @JsonProperty("column_count")
    private int columnCount;

    @JsonProperty("columns")
    private List<ColumnMetadata> columns;

    @JsonProperty("job")
    private String job;

    public QueryMetadata() {

    }

    public QueryMetadata(int columnCount, List<ColumnMetadata> columns, String job) {
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

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
