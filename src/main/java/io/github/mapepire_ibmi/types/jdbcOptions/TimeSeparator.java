package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "time separator" JDBC
 * option.
 */
public enum TimeSeparator {
    /**
     * Colon time separator.
     */
    COLON(":"),

    /**
     * period time separator.
     */
    DOT("."),

    /**
     * Comma time separator.
     */
    COMMA(","),

    /**
     * Space time separator.
     */
    B("b");

    /**
     * The "time separator" value.
     */
    private final String value;

    /**
     * Construct a new TimeSeparator instance.
     * 
     * @param value The "time separator" value.
     */
    TimeSeparator(String value) {
        this.value = value;
    }

    /**
     * Get the "time separator" value.
     * 
     * @return The "time separator" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "time separator" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static TimeSeparator fromValue(String value) {
        for (TimeSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
