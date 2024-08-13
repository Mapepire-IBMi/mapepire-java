package io.github.mapapire.types.jdbcOptions;

public enum ConcurrentAccessResolution {
    RESOLUTION_1("1"),
    RESOLUTION_2("2"),
    RESOLUTION_3("3");

    private final String value;

    ConcurrentAccessResolution(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ConcurrentAccessResolution fromValue(String value) {
        for (ConcurrentAccessResolution type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}