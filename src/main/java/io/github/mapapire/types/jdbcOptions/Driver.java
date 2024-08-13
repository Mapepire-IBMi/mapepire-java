package io.github.mapapire.types.jdbcOptions;

public enum Driver {
    TOOLBOX("toolbox"),
    NATIVE("native");

    private final String value;

    Driver(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Driver fromValue(String value) {
        for (Driver type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
