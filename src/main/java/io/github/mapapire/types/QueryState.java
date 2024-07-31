package io.github.mapapire.types;

public enum QueryState {
    NOT_YET_RUN(1),
    RUN_MORE_DATA_AVAILABLE(2),
    RUN_DONE(3),
    ERROR(4);

    private int queryState;

    QueryState(int _queryState) {
        this.queryState = _queryState;
    }
}