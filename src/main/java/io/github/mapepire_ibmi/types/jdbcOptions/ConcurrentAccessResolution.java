package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "concurrent access
 * resolution" JDBC option.
 */
public enum ConcurrentAccessResolution {
    /**
     * Currently committed.
     */
    RESOLUTION_1("1"),

    /**
     * Wait for outcome.
     */
    RESOLUTION_2("2"),

    /**
     * Skip locks.
     */
    RESOLUTION_3("3");

    /**
     * The "concurrent access resolution" value.
     */
    private final String value;

    /**
     * Construct a new ConcurrentAccessResolution instance.
     * 
     * @param value The "concurrent access resolution" value.
     */
    ConcurrentAccessResolution(String value) {
        this.value = value;
    }

    /**
     * Get the "concurrent access resolution" value.
     * 
     * @return The "concurrent access resolution" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "concurrent access resolution" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static ConcurrentAccessResolution fromValue(String value) {
        for (ConcurrentAccessResolution type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
