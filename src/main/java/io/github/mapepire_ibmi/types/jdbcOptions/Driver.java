package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "driver" JDBC option.
 */
public enum Driver {
    /**
     * Use only the IBM Toolbox for Java JDBC driver.
     */
    TOOLBOX("toolbox"),

    /**
     * Use the IBM Developer Kit for Java JDBC driver if running on the server,
     * otherwise use the IBM Toolbox for Java JDBC driver.
     */
    NATIVE("native");

    /**
     * The "driver" value.
     */
    private final String value;

    /**
     * Construct a new Driver instance.
     * 
     * @param value The "driver" value.
     */
    Driver(String value) {
        this.value = value;
    }

    /**
     * Get the "driver" value.
     * 
     * @return The "driver" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "driver" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Driver fromValue(String value) {
        for (Driver type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
