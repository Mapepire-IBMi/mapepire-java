package io.github.mapepire_ibmi.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryOptions {
    @JsonProperty("isTerseResults")
    private boolean isTerseResults;

    @JsonProperty("isClCommand")
    private boolean isClCommand;

    @JsonProperty("parameters")
    private List<Object> parameters;

    public QueryOptions() {

    }

    public QueryOptions(boolean isTerseResults, boolean isClCommand, List<Object> parameters) {
        this.isTerseResults = isTerseResults;
        this.isClCommand = isClCommand;
        this.parameters = parameters;
    }

    public boolean getIsTerseResults() {
        return isTerseResults;
    }

    public void setIsTerseResults(boolean isTerseResults) {
        this.isTerseResults = isTerseResults;
    }

    public boolean getIsClCommand() {
        return isClCommand;
    }

    public void setIsClCommand(boolean isClCommand) {
        this.isClCommand = isClCommand;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
