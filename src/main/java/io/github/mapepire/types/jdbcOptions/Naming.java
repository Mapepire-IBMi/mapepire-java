package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "naming" JDBC option.
 */
public enum Naming {
    /**
     * As in schema.table.
     */
    SQL("sql"),

    /**
     * As in schema/table.
     */
    SYSTEM("system");

    /**
     * The "naming" value.
     */
    private final String value;

    /**
     * Construct a new Naming instance.
     * @param value The "naming" value.
     */
    Naming(String value) {
        this.value = value;
    }

    /**
     * Get the "naming" value.
     * @return The "naming" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "naming" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Naming fromValue(String value) {
        for (Naming type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
