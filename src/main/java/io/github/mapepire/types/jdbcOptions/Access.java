package io.github.mapepire.types.jdbcOptions;

public enum Access {
    ALL("all"),
    READ_CALL("read call"),
    READ_ONLY("read only");

    private final String value;

    Access(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Access fromValue(String value) {
        for (Access type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
