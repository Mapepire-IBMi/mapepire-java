package io.github.mapapire.types.jdbcOptions;

public enum MaximumPrecision {
    PRECISION_31("31"),
    PRECISION_63("63");

    private final String value;

    MaximumPrecision(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MaximumPrecision fromValue(String value) {
        for (MaximumPrecision type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}