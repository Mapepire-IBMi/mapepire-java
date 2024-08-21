package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "date separator" JDBC
 * option.
 */
public enum DateSeparator {
    /**
     * Slash date separator.
     */
    SLASH("/"),

    /**
     * Dash date separator.
     */
    DASH("-"),

    /**
     * Dot date separator.
     */
    DOT("."),

    /**
     * Comma date separator.
     */
    COMMA(","),

    /**
     * Space date separator.
     */
    B("b");

    /**
     * The "date separator" value.
     */
    private final String value;

    /**
     * Construct a new DateSeparator instance.
     * 
     * @param value The "date separator" value.
     */
    DateSeparator(String value) {
        this.value = value;
    }

    /**
     * Get the "date separator" value.
     * 
     * @return The "date separator" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "date separator" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static DateSeparator fromValue(String value) {
        for (DateSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
