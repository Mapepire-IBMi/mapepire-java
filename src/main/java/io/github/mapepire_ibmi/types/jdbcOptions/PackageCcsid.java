package io.github.mapepire_ibmi.types.jdbcOptions;

public enum PackageCcsid {
    CCSID_1200("1200"),
    CCSID_13488("13488"),
    SYSTEM("system");

    private final String value;

    PackageCcsid(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PackageCcsid fromValue(String value) {
        for (PackageCcsid type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
