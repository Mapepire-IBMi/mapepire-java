package io.github.mapepire.types;

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
     * Whether the command is a CL command.
     */
    @JsonProperty("isClCommand")
    private boolean isClCommand;

    /**
     * The parameters for the query.
     */
    @JsonProperty("parameters")
    private List<Object> parameters;

    /**
     * Constructs a new QueryOptions instance.
     */
    public QueryOptions() {

    }

    /**
     * Constructs a new QueryOptions instance.
     * 
     * @param isTerseResults Whether to return terse results.
     * @param isClCommand    Whether the command is a CL command.
     * @param parameters     The parameters for the query.
     */
    public QueryOptions(boolean isTerseResults, boolean isClCommand, List<Object> parameters) {
        this.isTerseResults = isTerseResults;
        this.isClCommand = isClCommand;
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
     * Get whether the command is a CL command.
     * 
     * @return Whether the command is a CL command.
     */
    public boolean getIsClCommand() {
        return isClCommand;
    }

    /**
     * Set whether the command is a CL command.
     * 
     * @param isClCommand Whether the command is a CL command.
     */
    public void setIsClCommand(boolean isClCommand) {
        this.isClCommand = isClCommand;
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
