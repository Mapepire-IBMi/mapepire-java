package io.github.mapepire_ibmi.types;

/**
 * Enum representing the possible parameter modes.
 */
public enum ParameterMode {
    /**
     * The IN mode.
     */
    In("IN"),

    /**
     * The OUT mode.
     */
    Out("OUT"),

    /**
     * The INOUT mode.
     */
    InOut("INOUT");

    /**
     * The parameter mode.
     */
    private String value;

    /**
     * Construct a new ParameterMode instance.
     * 
     * @param value The parameter mode
     */
    ParameterMode(String value) {
        this.value = value;
    }

    /**
     * Get the parameter mode.
     * 
     * @return The parameter mode.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum parameter mode representation of a string.
     * 
     * @param value The string representation of the parameter mode.
     * @return The enum representation of the parameter mode.
     */
    public static ParameterMode fromValue(String value) {
        for (ParameterMode type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
