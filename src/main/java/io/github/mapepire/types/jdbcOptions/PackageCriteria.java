package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "package criteria"
 * JDBC option.
 */
public enum PackageCriteria {
    /**
     * Only store SQL statements with parameter markers in the package.
     */
    DEFAULT("default"),

    /**
     * Store all SQL SELECT statements in the package.
     */
    SELECT("select");

    /**
     * The "package criteria" value.
     */
    private final String value;

    /**
     * Construct a new PackageCriteria instance.
     * 
     * @param value The "package criteria" value.
     */
    PackageCriteria(String value) {
        this.value = value;
    }

    /**
     * Get the "package criteria" value.
     * 
     * @return The "package criteria" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "package criteria" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static PackageCriteria fromValue(String value) {
        for (PackageCriteria type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
