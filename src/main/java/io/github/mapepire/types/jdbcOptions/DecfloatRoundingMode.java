package io.github.mapepire.types.jdbcOptions;

public enum DecfloatRoundingMode {
    HALF_EVEN("half even"),
    HALF_UP("half up"),
    DOWN("down"),
    CEILING("ceiling"),
    FLOOR("floor"),
    UP("up"),
    HALF_DOWN("half down");

    private final String value;

    DecfloatRoundingMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DecfloatRoundingMode fromValue(String value) {
        for (DecfloatRoundingMode type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
