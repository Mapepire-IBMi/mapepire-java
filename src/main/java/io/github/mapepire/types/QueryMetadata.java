package io.github.mapepire.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the metadata for a query.
 */
public class QueryMetadata {
    /**
     * The number of columns returned by the query.
     */
    @JsonProperty("column_count")
    private int columnCount;

    /**
     * The metadata for each column.
     */
    @JsonProperty("columns")
    private List<ColumnMetadata> columns;

    /**
     * The unique job identifier for the query.
     */
    @JsonProperty("job")
    private String job;

    /**
     * Constructs a new QueryMetadata instance.
     */
    public QueryMetadata() {

    }

    /**
     * Constructs a new QueryMetadata instance.
     * 
     * @param columnCount The number of columns returned by the query.
     * @param columns     The metadata for each column.
     * @param job         The unique job identifier for the query.
     */
    public QueryMetadata(int columnCount, List<ColumnMetadata> columns, String job) {
        this.columnCount = columnCount;
        this.columns = columns;
        this.job = job;
    }

    /**
     * Get the number of columns returned by the query.
     * 
     * @return The number of columns returned by the query.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Set the number of columns returned by the query.
     * 
     * @param columnCount The number of columns returned by the query.
     */
    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * Get the metadata for each column.
     * 
     * @return The metadata for each column.
     */
    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    /**
     * Set the metadata for each column.
     * 
     * @param columns The metadata for each column.
     */
    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }

    /**
     * Get the unique job identifier for the query.
     * 
     * @return The unique job identifier for the query.
     */
    public String getJob() {
        return job;
    }

    /**
     * Set the unique job identifier for the query.
     * 
     * @param job The unique job identifier for the query.
     */
    public void setJob(String job) {
        this.job = job;
    }
}
