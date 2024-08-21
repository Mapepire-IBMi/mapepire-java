package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "cursor sensitivity"
 * JDBC option.
 */
public enum CursorSensitivity {
    /**
     * Asensitive cursor sensitivity.
     */
    ASENSITIVE("asensitive"),

    /**
     * Insensitive cursor sensitivity.
     */
    INSENSITIVE("insensitive"),

    /**
     * Sensitive cursor sensitivity.
     */
    SENSITIVE("sensitive");

    /**
     * The "cursor sensitivity" value.
     */
    private final String value;

    /**
     * Construct a new CursorSensitivity instance.
     * 
     * @param value The "cursor sensitivity" value.
     */
    CursorSensitivity(String value) {
        this.value = value;
    }

    /**
     * Get the "cursor sensitivity" value.
     * 
     * @return The "cursor sensitivity" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "cursor sensitivity" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static CursorSensitivity fromValue(String value) {
        for (CursorSensitivity type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
