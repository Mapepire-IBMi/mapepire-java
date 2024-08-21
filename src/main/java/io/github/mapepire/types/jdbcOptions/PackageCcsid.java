package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "package ccsid" JDBC
 * option.
 */
public enum PackageCcsid {
    /**
     * UCS-2 encoding.
     */
    CCSID_1200("1200"),

    /**
     * UTF-16 encoding.
     */
    CCSID_13488("13488"),

    /**
     * System encoding.
     */
    SYSTEM("system");

    /**
     * The "package ccsid" value.
     */
    private final String value;

    /**
     * Construct a new PackageCcsid instance.
     * 
     * @param value The "package ccsid" value.
     */
    PackageCcsid(String value) {
        this.value = value;
    }

    /**
     * Get the "package ccsid" value.
     * 
     * @return The "package ccsid" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "package ccsid" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static PackageCcsid fromValue(String value) {
        for (PackageCcsid type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
