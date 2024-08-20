package io.github.mapepire.types;

/**
 * Enum representing the possible server trace levels.
 */
public enum ServerTraceLevel {
    /**
     * Tracing is turned off.
     */
    OFF("OFF"),

    /**
     * All trace data is collected except datastream.
     */
    ON("ON"),

    /**
     * Only error trace data is collected.
     */
    ERRORS("ERRORS"),

    /**
     * All trace data is collected including datastream.
     */
    DATASTREAM("DATASTREAM");

    /**
     * The server trace level.
     */
    private String value;

    /**
     * Construct a new ServerTraceLevel instance.
     * 
     * @param value The server trace level.
     */
    ServerTraceLevel(String value) {
        this.value = value;
    }

    /**
     * Get the server trace level.
     * 
     * @return The server trace level.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum server trace level representation of a string.
     * 
     * @param value The string representation of the server trace level.
     * @return The enum representation of the server trace level.
     */
    public static ServerTraceLevel fromValue(String value) {
        for (ServerTraceLevel type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
