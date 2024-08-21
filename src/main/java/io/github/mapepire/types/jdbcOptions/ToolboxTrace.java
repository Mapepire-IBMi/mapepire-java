package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "toolbox trace" JDBC
 * option.
 */
public enum ToolboxTrace {
    /**
     * Empty.
     */
    EMPTY(""),

    /**
     * None.
     */
    NONE("NONE"),

    /**
     * Log data flow between the local host and the remote system.
     */
    DATASTREAM("datastream"),

    /**
     * Log object state information.
     */
    DIAGNOSTIC("diagnostic"),

    /**
     * Log errors that cause an exception.
     */
    ERROR("error"),

    /**
     * Log errors that are recoverable.
     */
    WARNING("warning"),

    /**
     * Log character set conversions between Unicode and native code pages.
     */
    CONVERSION("conversion"),

    /**
     * Log jdbc information.
     */
    JDBC("jdbc"),

    /**
     * Used to determine how PCML interprets the data that is sent to and from the
     * server.
     */
    PCML("pcml"),

    /**
     * Log all categories.
     */
    ALL("all"),

    /**
     * Log data flow between the client and the proxy server.
     */
    PROXY("proxy"),

    /**
     * Log thread information.
     */
    THREAD("thread"),

    /**
     * Used to track the flow of control through the code.
     */
    INFORMATION("information");

    /**
     * The "toolbox trace" value.
     */
    private final String value;

    /**
     * Construct a new ToolboxTrace instance.
     * 
     * @param value The "toolbox trace" value.
     */
    ToolboxTrace(String value) {
        this.value = value;
    }

    /**
     * Get the "toolbox trace" value.
     * 
     * @return The "toolbox trace" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "toolbox trace" value representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static ToolboxTrace fromValue(String value) {
        for (ToolboxTrace type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
