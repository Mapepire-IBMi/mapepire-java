package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "server trace" JDBC
 * option.
 */
public enum ServerTrace {
    /**
     * Trace is not active.
     */
    TRACE_0("0"),

    /**
     * Start the database monitor on the JDBC server job.
     */
    TRACE_2("2"),

    /**
     * Start debug on the JDBC server job.
     */
    TRACE_4("4"),

    /**
     * Save the job log when the JDBC server job ends.
     */
    TRACE_8("8"),

    /**
     * Start job trace on the JDBC server job.
     */
    TRACE_16("16"),

    /**
     * Save SQL information.
     */
    TRACE_32("32"),

    /**
     * Supports the activation of database host server tracing.
     */
    TRACE_64("64");

    /**
     * The "server trace" value.
     */
    private final String value;

    /**
     * Construct a new ServerTrace instance.
     * @param value The "server trace" value.
     */
    ServerTrace(String value) {
        this.value = value;
    }

    /**
     * Get the "server trace" value.
     * @return The "server trace" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "server trace" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static ServerTrace fromValue(String value) {
        for (ServerTrace type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
