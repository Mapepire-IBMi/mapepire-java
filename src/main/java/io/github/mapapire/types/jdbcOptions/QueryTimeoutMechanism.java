package io.github.mapapire.types.jdbcOptions;

public enum QueryTimeoutMechanism {
    QQRYTIMLMT("qqrytimlmt"),
    CANCEL("cancel");

    private final String value;

    QueryTimeoutMechanism(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static QueryTimeoutMechanism fromValue(String value) {
        for (QueryTimeoutMechanism type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
