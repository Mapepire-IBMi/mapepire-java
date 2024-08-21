package io.github.mapepire_ibmi.types.jdbcOptions;

public enum DecimalSeparator {
    DOT("."),
    COMMA(",");

    private final String value;

    DecimalSeparator(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DecimalSeparator fromValue(String value) {
        for (DecimalSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
