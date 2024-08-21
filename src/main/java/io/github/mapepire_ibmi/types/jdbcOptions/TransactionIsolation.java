package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "transaction
 * isolation" JDBC option.
 */
public enum TransactionIsolation {
    /**
     * None transaction isolation.
     */
    NONE("none"),

    /**
     * Read uncommitted transaction isolation.
     */
    READ_UNCOMMITTED("read uncommitted"),

    /**
     * Read committed transaction isolation.
     */
    READ_COMMITTED("read committed"),

    /**
     * Repeatable read transaction isolation.
     */
    REPEATABLE_READ("repeatable read"),

    /**
     * Serializable transaction isolation.
     */
    SERIALIZABLE("serializable");

    /**
     * The "transaction isolation" value.
     */
    private final String value;

    /**
     * Construct a new TransactionIsolation instance.
     * @param value The "transaction isolation" value.
     */
    TransactionIsolation(String value) {
        this.value = value;
    }

    /**
     * Get the "transaction isolation" value.
     * @return The "transaction isolation" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "transaction isolation" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static TransactionIsolation fromValue(String value) {
        for (TransactionIsolation type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
