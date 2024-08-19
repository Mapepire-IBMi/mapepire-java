package io.github.mapepire.types.jdbcOptions;

public enum SortWeight {
    SHARED("shared"),
    UNIQUE("unique");

    private final String value;

    SortWeight(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortWeight fromValue(String value) {
        for (SortWeight type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
