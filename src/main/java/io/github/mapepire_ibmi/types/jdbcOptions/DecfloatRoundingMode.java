package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "decfloat rounding
 * mode" JDBC option.
 */
public enum DecfloatRoundingMode {
    /**
     * Half even rounding mode.
     */
    HALF_EVEN("half even"),

    /**
     * Half up rounding mode.
     */
    HALF_UP("half up"),

    /**
     * Down rounding mode.
     */
    DOWN("down"),

    /**
     * Ceiling rounding mode.
     */
    CEILING("ceiling"),

    /**
     * Floor rounding mode.
     */
    FLOOR("floor"),

    /**
     * Up rounding mode.
     */
    UP("up"),

    /**
     * Half down rounding mode.
     */
    HALF_DOWN("half down");

    /**
     * The "decfloat rounding mode" value.
     */
    private final String value;

    /**
     * Construct a new DecfloatRoundingMode instance.
     * @param value The "decfloat rounding mode" value.
     */
    DecfloatRoundingMode(String value) {
        this.value = value;
    }

    /**
     * Get the "decfloat rounding mode" value.
     * @return The "decfloat rounding mode" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "decfloat rounding mode" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static DecfloatRoundingMode fromValue(String value) {
        for (DecfloatRoundingMode type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
