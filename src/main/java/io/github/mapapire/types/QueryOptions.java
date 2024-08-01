package io.github.mapapire.types;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QueryOptions {
    private final boolean isTerseResults;
    private final boolean isClCommand;
    private final List<Object> parameters;

    public QueryOptions(boolean _isTerseResults, boolean _isClCommand, List<Object> _parameters) {
        this.isTerseResults = _isTerseResults;
        this.isClCommand = _isClCommand;
        this.parameters = _parameters;
    }

    public boolean isTerseResults() {
        return isTerseResults;
    }

    public boolean isClCommand() {
        return isClCommand;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
