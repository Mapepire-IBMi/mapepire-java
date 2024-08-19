package io.github.mapepire.types.jdbcOptions;

public enum MetadataSource {
    SOURCE_0("0"),
    SOURCE_1("1");

    private final String value;

    MetadataSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MetadataSource fromValue(String value) {
        for (MetadataSource type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
