package io.github.mapepire.types.jdbcOptions;

public enum Error {
    FULL("full"),
    BASIC("basic");

    private final String value;

    Error(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Error fromValue(String value) {
        for (Error type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
