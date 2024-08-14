package io.github.mapapire.types.jdbcOptions;

public enum Property {
    // Format properties
    NAMING("naming"),
    DATE_FORMAT("date format"),
    DATE_SEPARATOR("date separator"),
    DECIMAL_SEPARATOR("decimal separator"),
    TIME_FORMAT("time format"),
    TIME_SEPARATOR("time separator"),

    // Other properties
    FULL_OPEN("full open"),
    ACCESS("access"),
    AUTOCOMMIT_EXCEPTION("autocommit exception"),
    BIDI_STRING_TYPE("bidi string type"),
    BIDI_IMPLICIT_REORDERING("bidi implicit reordering"),
    BIDI_NUMERIC_ORDERING("bidi numeric ordering"),
    DATA_TRUNCATION("data truncation"),
    DRIVER("driver"),
    ERRORS("errors"),
    EXTENDED_METADATA("extended metadata"),
    HOLD_INPUT_LOCATORS("hold input locators"),
    HOLD_STATEMENTS("hold statements"),
    IGNORE_WARNINGS("ignore warnings"),
    KEEP_ALIVE("keep alive"),
    KEY_RING_NAME("key ring name"),
    KEY_RING_PASSWORD("key ring password"),
    METADATA_SOURCE("metadata source"),
    PROXY_SERVER("proxy server"),
    REMARKS("remarks"),
    SECONDARY_URL("secondary URL"),
    SECURE("secure"),
    SERVER_TRACE("server trace"),
    THREAD_USED("thread used"),
    TOOLBOX_TRACE("toolbox trace"),
    TRACE("trace"),
    TRANSLATE_BINARY("translate binary"),
    TRANSLATE_BOOLEAN("translate boolean"),

    // System Properties
    LIBRARIES("libraries"),
    AUTO_COMMIT("auto commit"),
    CONCURRENT_ACCESS_RESOLUTION("concurrent access resolution"),
    CURSOR_HOLD("cursor hold"),
    CURSOR_SENSITIVITY("cursor sensitivity"),
    DATABASE_NAME("database name"),
    DECFLOAT_ROUNDING_MODE("decfloat rounding mode"),
    MAXIMUM_PRECISION("maximum precision"),
    MAXIMUM_SCALE("maximum scale"),
    MINIMUM_DIVIDE_SCALE("minimum divide scale"),
    PACKAGE_CCID("package ccsid"),
    TRANSACTION_ISOLATION("transaction isolation"),
    TRANSLATE_HEX("translate hex"),
    TRUE_AUTOCOMMIT("true autocommit"),
    XA_LOOSELY_COUPLED_SUPPORT("XA loosely coupled support"),

    // Performance Properties
    BIG_DECIMAL("big decimal"),
    BLOCK_CRITERIA("block criteria"),
    BLOCK_SIZE("block size"),
    DATA_COMPRESSION("data compression"),
    EXTENDED_DYNAMIC("extended dynamic"),
    LAZY_CLOSE("lazy close"),
    LOB_THRESHOLD("lob threshold"),
    MAXIMUM_BLOCKED_INPUT_ROWS("maximum blocked input rows"),
    PACKAGE("package"),
    PACKAGE_ADD("package add"),
    PACKAGE_CACHE("package cache"),
    PACKAGE_CRITERIA("package criteria"),
    PACKAGE_ERROR("package error"),
    PACKAGE_LIBRARY("package library"),
    PREFETCH("prefetch"),
    QAQQINILIB("qaqqinilib"),
    QUERY_OPTIMIZE_GOAL("query optimize goal"),
    QUERY_TIMEOUT_MECHANISM("query timeout mechanism"),
    QUERY_STORAGE_LIMIT("query storage limit"),
    RECEIVE_BUFFER_SIZE("receive buffer size"),
    SEND_BUFFER_SIZE("send buffer size"),
    VARIABLE_FIELD_COMPRESSION("variable field compression"),

    // Sort Properties
    SORT("sort"),
    SORT_LANGUAGE("sort language"),
    SORT_TABLE("sort table"),
    SORT_WEIGHT("sort weight");

    private final String value;

    Property(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Property fromValue(String value) {
        for (Property type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
