package io.github.mapapire.types.jdbcOptions;

public enum BlockCriteria {
    CRITERIA_0("0"),
    CRITERIA_1("1"),
    CRITERIA_2("2");

    private final String value;

    BlockCriteria(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BlockCriteria fromValue(String value) {
        for (BlockCriteria type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}