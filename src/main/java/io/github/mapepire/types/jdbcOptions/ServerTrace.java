package io.github.mapepire.types.jdbcOptions;

public enum ServerTrace {
    TRACE_0("0"),
    TRACE_2("2"),
    TRACE_4("4"),
    TRACE_8("8"),
    TRACE_16("16"),
    TRACE_32("32"),
    TRACE_64("64");

    private final String value;

    ServerTrace(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ServerTrace fromValue(String value) {
        for (ServerTrace type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
