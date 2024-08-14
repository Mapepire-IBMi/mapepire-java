package io.github.mapapire.types.jdbcOptions;

public enum DateFormat {
    MDY("mdy"),
    DMY("dmy"),
    YMD("ymd"),
    USA("usa"),
    ISO("iso"),
    EUR("eur"),
    JIS("jis"),
    JULIAN("julian");

    private final String value;

    DateFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DateFormat fromValue(String value) {
        for (DateFormat type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
