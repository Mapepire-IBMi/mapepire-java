package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "maximum precision"
 * JDBC option.
 */
public enum MaximumPrecision {
    /**
     * 31 maximum decimal precision.
     */
    PRECISION_31("31"),

    /**
     * 63 maximum decimal precision.
     */
    PRECISION_63("63");

    /**
     * The "maximum precision" value.
     */
    private final String value;

    /**
     * Construct a new MaximumPrecision instance.
     * 
     * @param value The "maximum precision" value.
     */
    MaximumPrecision(String value) {
        this.value = value;
    }

    /**
     * Get the "maximum precision" value.
     * 
     * @return The "maximum precision" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "maximum precision" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static MaximumPrecision fromValue(String value) {
        for (MaximumPrecision type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
