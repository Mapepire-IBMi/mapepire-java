package io.github.mapepire.types.jdbcOptions;

/**
 * Enum representing the possible types of values for the "block size" JDBC
 * option.
 */
public enum BlockSize {
    /**
     * 0 kilobytes
     */
    SIZE_0("0"),

    /**
     * 8 kilobytes.
     */
    SIZE_8("8"),

    /**
     * 16 kilobytes.
     */
    SIZE_16("16"),

    /**
     * 32 kilobytes.
     */
    SIZE_32("32"),

    /**
     * 64 kilobytes.
     */
    SIZE_64("64"),

    /**
     * 128 kilobytes.
     */
    SIZE_128("128"),

    /**
     * 256 kilobytes.
     */
    SIZE_256("256"),

    /**
     * 512 kilobytes.
     */
    SIZE_512("512");

    /**
     * The "block size" value.
     */
    private final String value;

    /**
     * Construct a new BlockSize instance.
     * @param value The "block size" value.
     */
    BlockSize(String value) {
        this.value = value;
    }

    /**
     * Get the "block size" value.
     * @return The "block size" value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum "block size" value representation of a string.
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static BlockSize fromValue(String value) {
        for (BlockSize type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
