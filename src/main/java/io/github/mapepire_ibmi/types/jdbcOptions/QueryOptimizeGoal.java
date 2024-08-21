package io.github.mapepire_ibmi.types.jdbcOptions;

public enum QueryOptimizeGoal {
    GOAL_0("0"),
    GOAL_1("1"),
    GOAL_2("2");

    private final String value;

    QueryOptimizeGoal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static QueryOptimizeGoal fromValue(String value) {
        for (QueryOptimizeGoal type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
