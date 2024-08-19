package io.github.mapepire.types.jdbcOptions;

public enum BlockSize {
    SIZE_0("0"),
    SIZE_8("8"),
    SIZE_16("16"),
    SIZE_32("32"),
    SIZE_64("64"),
    SIZE_128("128"),
    SIZE_256("256"),
    SIZE_512("512");

    private final String value;

    BlockSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BlockSize fromValue(String value) {
        for (BlockSize type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
