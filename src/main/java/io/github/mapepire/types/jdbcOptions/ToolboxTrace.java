package io.github.mapepire.types.jdbcOptions;

public enum ToolboxTrace {
    EMPTY(""),
    NONE("NONE"),
    DATASTREAM("datastream"),
    DIAGNOSTIC("diagnostic"),
    ERROR("error"),
    WARNING("warning"),
    CONVERSION("conversion"),
    JDBC("jdbc"),
    PCML("pcml"),
    ALL("all"),
    PROXY("proxy"),
    THREAD("thread"),
    INFORMATION("information");

    private final String value;

    ToolboxTrace(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ToolboxTrace fromValue(String value) {
        for (ToolboxTrace type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
