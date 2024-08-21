package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "bidi string type"
 * JDBC option.
 */
public enum BidiStringType {
    /**
     * Bidi String Type 4
     */
    TYPE_4("4"),

    /**
     * Bidi String Type 5
     */
    TYPE_5("5"),

    /**
     * Bidi String Type 6
     */
    TYPE_6("6"),

    /**
     * Bidi String Type 7
     */
    TYPE_7("7"),

    /**
     * Bidi String Type 8
     */
    TYPE_8("8"),

    /**
     * Bidi String Type 9
     */
    TYPE_9("9"),

    /**
     * Bidi String Type 10
     */
    TYPE_10("10"),

    /**
     * Bidi String Type 11
     */
    TYPE_11("11");

    /**
     * The "bidi string type" value.
     */
    private final String value;

    /**
     * Construct a new BidiStringType instance.
     * 
     * @param value The "bidi string type" value.
     */
    BidiStringType(String value) {
        this.value = value;
    }

    /**
     * Get the "bidi string type" value.
     * 
     * @return The "bidi string type" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "bidi string type" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static BidiStringType fromValue(String value) {
        for (BidiStringType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
