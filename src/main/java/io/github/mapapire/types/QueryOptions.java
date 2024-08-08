package io.github.mapapire.types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueryOptions {
    private boolean isTerseResults;
    private boolean isClCommand;
    private List<Object> parameters;

    public QueryOptions() {

    }

    public QueryOptions(boolean isTerseResults, boolean isClCommand, List<Object> parameters) {
        this.isTerseResults = isTerseResults;
        this.isClCommand = isClCommand;
        this.parameters = parameters;
    }

    public boolean isTerseResults() {
        return isTerseResults;
    }

    public void setTerseResults(boolean isTerseResults) {
        this.isTerseResults = isTerseResults;
    }

    public boolean isClCommand() {
        return isClCommand;
    }

    public void setClCommand(boolean isClCommand) {
        this.isClCommand = isClCommand;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
