package io.github.mapepire_ibmi.types.jdbcOptions;

public enum XALooselyCoupledSupport {
    SUPPORT_0("0"),
    SUPPORT_1("1");

    private final String value;

    XALooselyCoupledSupport(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static XALooselyCoupledSupport fromValue(String value) {
        for (XALooselyCoupledSupport type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
