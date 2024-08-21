package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "decimal separator"
 * JDBC option.
 */
public enum DecimalSeparator {
    /**
     * Period decimal separator.
     */
    DOT("."),

    /**
     * Comma decimal separator.
     */
    COMMA(",");

    /**
     * The "decimal separator" value.
     */
    private final String value;

    /**
     * Construct a new DecimalSeparator instance.
     * @param value The "decimal separator" value.
     */
    DecimalSeparator(String value) {
        this.value = value;
    }

    /**
     * Get the "decimal separator" value.
     * @return The "decimal separator" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "decimal separator" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static DecimalSeparator fromValue(String value) {
        for (DecimalSeparator type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
