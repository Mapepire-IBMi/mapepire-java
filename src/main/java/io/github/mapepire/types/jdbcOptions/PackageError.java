package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "package error" JDBC
 * option.
 */
public enum PackageError {
    /**
     * Exception action.
     */
    EXCEPTION("exception"),

    /**
     * Warning action.
     */
    WARNING("warning"),

    /**
     * No action.
     */
    NONE("none");

    /**
     * The "package error" value.
     */
    private final String value;

    /**
     * Construct a new PackageError instance.
     * 
     * @param value The "package error" value.
     */
    PackageError(String value) {
        this.value = value;
    }

    /**
     * Get the "package error" value.
     * 
     * @return The "package error" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "package error" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static PackageError fromValue(String value) {
        for (PackageError type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
