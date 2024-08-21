package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "minimum divide scale"
 * JDBC option.
 */
public enum MinimumDivideScale {
    /**
     * 0 scale value.
     */
    SCALE_0("0"),

    /**
     * 1 scale value.
     */
    SCALE_1("1"),

    /**
     * 2 scale value.
     */
    SCALE_2("2"),

    /**
     * 3 scale value.
     */
    SCALE_3("3"),

    /**
     * 4 scale value.
     */
    SCALE_4("4"),

    /**
     * 5 scale value.
     */
    SCALE_5("5"),

    /**
     * 6 scale value.
     */
    SCALE_6("6"),

    /**
     * 7 scale value.
     */
    SCALE_7("7"),

    /**
     * 8 scale value.
     */
    SCALE_8("8"),

    /**
     * 9 scale value.
     */
    SCALE_9("9");

    /**
     * The "minimum divide scale" value.
     */
    private final String value;

    /**
     * Construct a new MinimumDivideScale instance.
     * 
     * @param value The "minimum divide scale" value.
     */
    MinimumDivideScale(String value) {
        this.value = value;
    }

    /**
     * Get the "minimum divide scale" value.
     * 
     * @return The "minimum divide scale" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "minimum divide scale" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static MinimumDivideScale fromValue(String value) {
        for (MinimumDivideScale type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
