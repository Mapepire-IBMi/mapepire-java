package io.github.mapepire.types.jdbcOptions;

public enum DateSeparator {
    SLASH("/"),
    DASH("-"),
    DOT("."),
    COMMA(","),
    B("b");

    private final String value;

    DateSeparator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DateSeparator fromValue(String value) {
        for (DateSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
