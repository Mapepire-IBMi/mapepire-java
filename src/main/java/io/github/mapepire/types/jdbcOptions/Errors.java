package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "errors" JDBC option.
 */
public enum Errors {
    /**
     * Full error detail.
     */
    FULL("full"),

    /**
     * Basic error detail.
     */
    BASIC("basic");

    /**
     * The "errors" value.
     */
    private final String value;

    /**
     * Construct a new Errors instance.
     * 
     * @param value The "errors" value.
     */
    Errors(String value) {
        this.value = value;
    }

    /**
     * Get the "errors" value.
     * 
     * @return The "errors" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "errors" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Errors fromValue(String value) {
        for (Errors type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
