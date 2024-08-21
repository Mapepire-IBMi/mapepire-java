package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "date format" JDBC
 * option.
 */
public enum DateFormat {
    /**
     * mdy date format.
     */
    MDY("mdy"),

    /**
     * dmy date format.
     */
    DMY("dmy"),

    /**
     * ymd date format.
     */
    YMD("ymd"),

    /**
     * usa date format.
     */
    USA("usa"),

    /**
     * iso date format.
     */
    ISO("iso"),

    /**
     * eur date format.
     */
    EUR("eur"),

    /**
     * jis date format.
     */
    JIS("jis"),

    /**
     * julian date format.
     */
    JULIAN("julian");

    /**
     * The "date format" value.
     */
    private final String value;

    /**
     * Construct a new DateFormat instance.
     * @param value The "date format" value.
     */
    DateFormat(String value) {
        this.value = value;
    }

    /**
     * Get the "date format" value.
     * @return The "date format" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "date format" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static DateFormat fromValue(String value) {
        for (DateFormat type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
