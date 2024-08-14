package io.github.mapapire.types;

public enum ServerTraceLevel {
    OFF("OFF"), // off
    ON("ON"), // all except datastream
    ERRORS("ERRORS"), // errors only
    DATASTREAM("DATASTREAM"); // all including datastream

    private String value;

    ServerTraceLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ServerTraceLevel fromValue(String value) {
        for (ServerTraceLevel type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
