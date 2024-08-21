package io.github.mapepire_ibmi.types.jdbcOptions;

public enum Sort {
    HEX("hex"),
    LANGUAGE("language"),
    TABLE("table");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Sort fromValue(String value) {
        for (Sort type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
