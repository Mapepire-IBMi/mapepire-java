package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "XA loosely coupled
 * support" JDBC option.
 */
public enum XALooselyCoupledSupport {
    /**
     * Locks cannot be shared.
     */
    SUPPORT_0("0"),

    /**
     * Locks can be shared.
     */
    SUPPORT_1("1");

    /**
     * The "XA loosely coupled support" value.
     */
    private final String value;

    /**
     * Construct a new XALooselyCoupledSupport instance.
     * @param value The "XA loosely coupled support" value.
     */
    XALooselyCoupledSupport(String value) {
        this.value = value;
    }

    /**
     * Get the "XA loosely coupled support" value.
     * @return The "XA loosely coupled support" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "XA loosely coupled support" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static XALooselyCoupledSupport fromValue(String value) {
        for (XALooselyCoupledSupport type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
