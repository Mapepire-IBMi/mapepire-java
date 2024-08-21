package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "sort weight" JDBC
 * option.
 */
public enum SortWeight {
    /**
     * Uppercase and lowercase characters sort as the same character.
     */
    SHARED("shared"),

    /**
     * Uppercase and lowercase characters sort as different characters.
     */
    UNIQUE("unique");

    /**
     * The "sort weight" value.
     */
    private final String value;

    /**
     * Construct a new SortWeight instance.
     * @param value The "sort weight" value.
     */
    SortWeight(String value) {
        this.value = value;
    }

    /**
     * Get the "sort weight" value.
     * @return The "sort weight" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "sort weight" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static SortWeight fromValue(String value) {
        for (SortWeight type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
