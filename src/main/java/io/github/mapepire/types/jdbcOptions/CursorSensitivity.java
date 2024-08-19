package io.github.mapepire.types.jdbcOptions;

public enum CursorSensitivity {
    ASENSITIVE("asensitive"),
    INSENSITIVE("insensitive"),
    SENSITIVE("sensitive");

    private final String value;

    CursorSensitivity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CursorSensitivity fromValue(String value) {
        for (CursorSensitivity type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
