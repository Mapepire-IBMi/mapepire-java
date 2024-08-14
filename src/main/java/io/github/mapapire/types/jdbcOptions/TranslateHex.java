package io.github.mapapire.types.jdbcOptions;

public enum TranslateHex {
    CHARACTER("character"),
    BINARY("binary");

    private final String value;

    TranslateHex(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TranslateHex fromValue(String value) {
        for (TranslateHex type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
