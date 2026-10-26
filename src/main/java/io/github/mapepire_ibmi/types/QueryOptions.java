package io.github.mapepire_ibmi.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the options for configuring a query.
 */
public class QueryOptions {
    /**
     * Whether to return terse results.
     */
    @JsonProperty("isTerseResults")
    private boolean isTerseResults;

    /**
     * The parameters for the query.
     */
    @JsonProperty("parameters")
    private List<Object> parameters;

    /**
     * Construct a new QueryOptions instance.
     */
    public QueryOptions() {

    }

    /**
     * Construct a new QueryOptions instance.
     * 
     * @param isTerseResults Whether to return terse results.
     * @param parameters     The parameters for the query.
     */
    public QueryOptions(boolean isTerseResults, List<Object> parameters) {
        this.isTerseResults = isTerseResults;
        this.parameters = parameters;
    }

    /**
     * Get whether to return terse results.
     * 
     * @return Whether to return terse results.
     */
    public boolean getIsTerseResults() {
        return isTerseResults;
    }

    /**
     * Set whether to return terse results.
     * 
     * @param isTerseResults Whether to return terse results.
     */
    public void setIsTerseResults(boolean isTerseResults) {
        this.isTerseResults = isTerseResults;
    }

    /**
     * Get the parameters for the query.
     * 
     * @return The parameters for the query.
     */
    public List<Object> getParameters() {
        return parameters;
    }

    /**
     * Set the parameters for the query.
     * 
     * @param parameters The parameters for the query.
     */
    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
