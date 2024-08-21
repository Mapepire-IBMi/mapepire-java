package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "access" JDBC option.
 */
public enum Access {
    /**
     * All SQL statements allowed.
     */
    ALL("all"),

    /**
     * SELECT and CALL statements allowed.
     */
    READ_CALL("read call"),

    /**
     * SELECT statements only.
     */
    READ_ONLY("read only");

    /**
     * The "access" value.
     */
    private final String value;

    /**
     * Construct a new Access instance.
     * 
     * @param value The "access" value.
     */
    Access(String value) {
        this.value = value;
    }

    /**
     * Get the "access" value.
     * 
     * @return The "access" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "access" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Access fromValue(String value) {
        for (Access type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
