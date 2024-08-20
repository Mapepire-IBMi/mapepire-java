package io.github.mapepire.types;

/**
 * Enum representing the possible server trace destinations.
 */
public enum ServerTraceDest {
    /**
     * Trace data is saved to a file.
     */
    FILE("FILE"),

    /**
     * Trace data is kept in memory.
     */
    IN_MEM("IN_MEM");

    /**
     * The server trace destination.
     */
    private String value;

    /**
     * Construct a new ServerTraceDest instance.
     * 
     * @param value The server trace destination.
     */
    ServerTraceDest(String value) {
        this.value = value;
    }

    /**
     * Get the server trace destination.
     * 
     * @return The server trace destination.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum server trace destination representation of a string.
     * 
     * @param value The string representation of the server trace destination.
     * @return The enum representation of the server trace destination.
     */
    public static ServerTraceDest fromValue(String value) {
        for (ServerTraceDest type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
