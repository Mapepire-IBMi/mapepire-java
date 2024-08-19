package io.github.mapepire.types.jdbcOptions;

public enum TimeFormat {
    HMS("hms"),
    USA("usa"),
    ISO("iso"),
    EUR("eur"),
    JIS("jis");

    private final String value;

    TimeFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TimeFormat fromValue(String value) {
        for (TimeFormat type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
