package io.github.mapapire.types.jdbcOptions;

public enum Remarks {
    SQL("sql"),
    SYSTEM("system");

    private final String value;

    Remarks(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Remarks fromValue(String value) {
        for (Remarks type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
