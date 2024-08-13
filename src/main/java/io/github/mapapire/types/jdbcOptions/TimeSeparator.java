package io.github.mapapire.types.jdbcOptions;

public enum TimeSeparator {
    COLON(":"),
    DOT("."),
    COMMA(","),
    B("b");

    private final String value;

    TimeSeparator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TimeSeparator fromValue(String value) {
        for (TimeSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}