package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "remarks" JDBC option.
 */
public enum Remarks {
    /**
     * SQL object comment.
     */
    SQL("sql"),

    /**
     * IBM i object description.
     */
    SYSTEM("system");

    /**
     * The "remarks" value.
     */
    private final String value;

    /**
     * Construct a new Remarks instance.
     * 
     * @param value The "remarks" value.
     */
    Remarks(String value) {
        this.value = value;
    }

    /**
     * Get the "remarks" value.
     * 
     * @return The "remarks" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "remarks" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Remarks fromValue(String value) {
        for (Remarks type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
