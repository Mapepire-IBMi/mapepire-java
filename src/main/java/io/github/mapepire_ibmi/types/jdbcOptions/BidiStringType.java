package io.github.mapepire_ibmi.types.jdbcOptions;

public enum BidiStringType {
    TYPE_4("4"),
    TYPE_5("5"),
    TYPE_6("6"),
    TYPE_7("7"),
    TYPE_8("8"),
    TYPE_9("9"),
    TYPE_10("10"),
    TYPE_11("11");

    private final String value;

    BidiStringType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BidiStringType fromValue(String value) {
        for (BidiStringType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
