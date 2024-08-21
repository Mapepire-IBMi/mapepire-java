package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "metadata source" JDBC
 * option.
 */
public enum MetadataSource {
    /**
     * ROI access.
     */
    SOURCE_0("0"),

    /**
     * SQL stored procedures.
     */
    SOURCE_1("1");

    /**
     * The "metadata source" value.
     */
    private final String value;

    /**
     * Construct a new MetadataSource instance.
     * @param value The "metadata source" value.
     */
    MetadataSource(String value) {
        this.value = value;
    }

    /**
     * Get the "metadata source" value.
     * @return The "metadata source" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "metadata source" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static MetadataSource fromValue(String value) {
        for (MetadataSource type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
