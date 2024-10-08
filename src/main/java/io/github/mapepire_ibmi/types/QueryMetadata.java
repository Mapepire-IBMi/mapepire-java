package io.github.mapepire_ibmi.types;

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
     * The parameters for the query.
     */
    @JsonProperty("parameters")
    private List<ParameterDetail> parameters;

    /**
     * Construct a new QueryMetadata instance.
     */
    public QueryMetadata() {

    }

    /**
     * Construct a new QueryMetadata instance.
     * 
     * @param columnCount The number of columns returned by the query.
     * @param columns     The metadata for each column.
     * @param job         The unique job identifier for the query.
     * @param parameters  The parameters for the query.
     */
    public QueryMetadata(int columnCount, List<ColumnMetadata> columns, String job, List<ParameterDetail> parameters) {
        this.columnCount = columnCount;
        this.columns = columns;
        this.job = job;
        this.parameters = parameters;
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

    /**
     * Get the parameters for the query.
     * 
     * @return The parameters for the query.
     */
    public List<ParameterDetail> getParameters() {
        return parameters;
    }

    /**
     * Set the parameters for the query.
     * 
     * @param parameters The parameters for the query.
     */
    public void setParameters(List<ParameterDetail> parameters) {
        this.parameters = parameters;
    }
}
