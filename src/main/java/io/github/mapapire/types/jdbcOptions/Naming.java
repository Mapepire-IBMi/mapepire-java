package io.github.mapapire.types.jdbcOptions;

public enum Naming {
    SQL("sql"),
    SYSTEM("system");

    private final String value;

    Naming(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Naming fromValue(String value) {
        for (Naming type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
