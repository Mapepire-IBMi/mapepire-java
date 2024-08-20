package io.github.mapepire.types;

/**
 * Enum representing the possible states of a query execution.
 */
public enum QueryState {
    /**
     * Indicates that the query has not yet been run.
     */
    NOT_YET_RUN(1),

    /**
     * Indicates that the query has been executed, and more data is available for
     * retrieval.
     */
    RUN_MORE_DATA_AVAILABLE(2),

    /**
     * Indicates that the query has been successfully executed and all data has been
     * retrieved.
     */
    RUN_DONE(3),

    /**
     * Indicates that an error occurred during the query execution.
     */
    ERROR(4);

    /**
     * The query state.
     */
    private int value;

    /**
     * Construct a new QueryState instance.
     * 
     * @param value The query state.
     */
    QueryState(int value) {
        this.value = value;
    }

    /**
     * Get the query state.
     * 
     * @return The query state.
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the enum query state representation of a string.
     * 
     * @param value The string representation of the query state.
     * @return The enum representation of the query state.
     */
    public static QueryState fromValue(int value) {
        for (QueryState type : values()) {
            if (type.value == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
