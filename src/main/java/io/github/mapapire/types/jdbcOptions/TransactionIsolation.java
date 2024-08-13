package io.github.mapapire.types.jdbcOptions;

public enum TransactionIsolation {
    NONE("none"),
    READ_UNCOMMITTED("read uncommitted"),
    READ_COMMITTED("read committed"),
    REPEATABLE_READ("repeatable read"),
    SERIALIZABLE("serializable");

    private final String value;

    TransactionIsolation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TransactionIsolation fromValue(String value) {
        for (TransactionIsolation type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}