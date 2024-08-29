package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "translate hex" JDBC
 * option.
 */
public enum TranslateHex {
    /**
     * Interpret hexadecimal literals as character data.
     */
    CHARACTER("character"),

    /**
     * Interpret hexadecimal literals as binary data.
     */
    BINARY("binary");

    /**
     * The "translate hex" value.
     */
    private final String value;

    /**
     * Construct a new TranslateHex instance.
     * 
     * @param value The "translate hex" value.
     */
    TranslateHex(String value) {
        this.value = value;
    }

    /**
     * Get the "translate hex" value.
     * 
     * @return The "translate hex" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "translate hex" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static TranslateHex fromValue(String value) {
        for (TranslateHex type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
