package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "block criteria" JDBC
 * option.
 */
public enum BlockCriteria {
    /**
     * No record blocking.
     */
    CRITERIA_0("0"),

    /**
     * Block if FOR FETCH ONLY is specified.
     */
    CRITERIA_1("1"),

    /**
     * Block unless FOR UPDATE is specified.
     */
    CRITERIA_2("2");

    /**
     * The "block criteria" value.
     */
    private final String value;

    /**
     * Construct a new BlockCriteria instance.
     * @param value The "block criteria" value.
     */
    BlockCriteria(String value) {
        this.value = value;
    }

    /**
     * Get the "block criteria" value.
     * @return The "block criteria" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "block criteria" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static BlockCriteria fromValue(String value) {
        for (BlockCriteria type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
