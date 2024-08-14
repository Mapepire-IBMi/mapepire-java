package io.github.mapapire.types.jdbcOptions;

public enum PackageCriteria {
    DEFAULT("default"),
    SELECT("select");

    private final String value;

    PackageCriteria(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PackageCriteria fromValue(String value) {
        for (PackageCriteria type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
