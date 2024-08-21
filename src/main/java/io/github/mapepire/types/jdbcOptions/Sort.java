package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "sort" JDBC option.
 */
public enum Sort {
    /**
     * Base the sort on hexadecimal values.
     */
    HEX("hex"),

    /**
     * Base the sort on the language set in the "sort language" property.
     */
    LANGUAGE("language"),

    /**
     * Base the sort on the sort sequence table set in the "sort table" property.
     */
    TABLE("table");

    /**
     * The "sort" value.
     */
    private final String value;

    /**
     * Construct a new Sort instance.
     * @param value The "sort" value.
     */
    Sort(String value) {
        this.value = value;
    }

    /**
     * Get the "sort" value.
     * @return The "sort" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "sort" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Sort fromValue(String value) {
        for (Sort type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
