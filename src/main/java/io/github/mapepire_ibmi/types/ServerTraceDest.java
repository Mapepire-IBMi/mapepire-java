package io.github.mapepire_ibmi.types;

public enum ServerTraceDest {
    FILE("FILE"),
    IN_MEM("IN_MEM");

    private String value;

    ServerTraceDest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ServerTraceDest fromValue(String value) {
        for (ServerTraceDest type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
