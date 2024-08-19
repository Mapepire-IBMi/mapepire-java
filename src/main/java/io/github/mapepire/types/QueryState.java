package io.github.mapepire.types;

public enum QueryState {
    NOT_YET_RUN(1),
    RUN_MORE_DATA_AVAILABLE(2),
    RUN_DONE(3),
    ERROR(4);

    private int value;

    QueryState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static QueryState fromValue(int value) {
        for (QueryState type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
