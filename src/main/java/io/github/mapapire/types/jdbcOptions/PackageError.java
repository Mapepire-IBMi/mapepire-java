package io.github.mapapire.types.jdbcOptions;

public enum PackageError {
    EXCEPTION("exception"),
    WARNING("warning"),
    NONE("none");

    private final String value;

    PackageError(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PackageError fromValue(String value) {
        for (PackageError type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}