package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "time format" JDBC
 * option.
 */
public enum TimeFormat {
    /**
     * hms time format.
     */
    HMS("hms"),

    /**
     * usa time format.
     */
    USA("usa"),

    /**
     * iso time format.
     */
    ISO("iso"),

    /**
     * eur time format.
     */
    EUR("eur"),

    /**
     * jis time format.
     */
    JIS("jis");

    /**
     * The "time format" value.
     */
    private final String value;

    /**
     * Construct a new TimeFormat instance.
     * 
     * @param value The "time format" value.
     */
    TimeFormat(String value) {
        this.value = value;
    }

    /**
     * Get the "time format" value.
     * 
     * @return The "time format" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "time format" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static TimeFormat fromValue(String value) {
        for (TimeFormat type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
