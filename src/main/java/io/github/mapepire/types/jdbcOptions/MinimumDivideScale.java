package io.github.mapepire.types.jdbcOptions;

public enum MinimumDivideScale {
    SCALE_0("0"),
    SCALE_1("1"),
    SCALE_2("2"),
    SCALE_3("3"),
    SCALE_4("4"),
    SCALE_5("5"),
    SCALE_6("6"),
    SCALE_7("7"),
    SCALE_8("8"),
    SCALE_9("9");

    private final String value;

    MinimumDivideScale(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MinimumDivideScale fromValue(String value) {
        for (MinimumDivideScale type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
