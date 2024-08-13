package io.github.mapapire.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCOptions {
    Map<String, Object> options = new HashMap<>();

    public JDBCOptions() {

    }

    public Object getOption(Property property) {
        return options.get(property.getValue());
    }

    private void setOption(Property property, Object value) {
        options.put(property.getValue(), value);
    }

    public void setNaming(Naming naming) {
        this.setOption(Property.NAMING, naming.getValue());
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.setOption(Property.DATE_FORMAT, dateFormat.getValue());
    }

    public void setDateSeparator(DateSeparator dateSeparator) {
        this.setOption(Property.DATE_SEPARATOR, dateSeparator.getValue());
    }

    public void setDecimalSeparator(DecimalSeparator decimalSeparator) {
        this.setOption(Property.DECIMAL_SEPARATOR, decimalSeparator.getValue());
    }

    public void setTimeFormat(TimeFormat timeFormat) {
        this.setOption(Property.TIME_FORMAT, timeFormat.getValue());
    }

    public void setTimeSeparator(TimeSeparator timeSeparator) {
        this.setOption(Property.TIME_SEPARATOR, timeSeparator.getValue());
    }

    public void setFullOpen(boolean fullOpen) {
        this.setOption(Property.FULL_OPEN, fullOpen);
    }

    public void setAccess(Access access) {
        this.setOption(Property.ACCESS, access.getValue());
    }

    public void setAutocommitException(String autocommitException) {
        this.setOption(Property.AUTOCOMMIT_EXCEPTION, autocommitException);
    }

    public void setBidiStringType(BidiStringType bidiStringType) {
        this.setOption(Property.BIDI_STRING_TYPE, bidiStringType.getValue());
    }

    public void setBidiImplicitReordering(boolean bidiImplicitReordering) {
        this.setOption(Property.BIDI_IMPLICIT_REORDERING, bidiImplicitReordering);
    }

    public void setBidiNumericOrdering(boolean bidiNumericOrdering) {
        this.setOption(Property.BIDI_NUMERIC_ORDERING, bidiNumericOrdering);
    }

    public void setDataTruncation(boolean dataTruncation) {
        this.setOption(Property.DATA_TRUNCATION, dataTruncation);
    }

    public void setDriver(Driver driver) {
        this.setOption(Property.DRIVER, driver.getValue());
    }

    public void setErrors(Error errors) {
        this.setOption(Property.ERRORS, errors.getValue());
    }

    public void setExtendedMetadata(boolean extendedMetadata) {
        this.setOption(Property.EXTENDED_METADATA, extendedMetadata);
    }

    public void setHoldInputLocators(boolean holdInputLocators) {
        this.setOption(Property.HOLD_INPUT_LOCATORS, holdInputLocators);
    }

    public void setHoldStatements(boolean holdStatements) {
        this.setOption(Property.HOLD_STATEMENTS, holdStatements);
    }

    public void setIgnoreWarnings(String ignoreWarnings) {
        this.setOption(Property.IGNORE_WARNINGS, ignoreWarnings);
    }

    public void setKeepAlive(boolean keepAlive) {
        this.setOption(Property.KEEP_ALIVE, keepAlive);
    }

    public void setKeyRingName(String keyRingName) {
        this.setOption(Property.KEY_RING_NAME, keyRingName);
    }

    public void setKeyRingPassword(String keyRingPassword) {
        this.setOption(Property.KEY_RING_PASSWORD, keyRingPassword);
    }

    public void setMetadataSource(MetadataSource metadataSource) {
        this.setOption(Property.METADATA_SOURCE, metadataSource.getValue());
    }

    public void setProxyServer(String proxyServer) {
        this.setOption(Property.PROXY_SERVER, proxyServer);
    }

    public void setRemarks(Remarks remarks) {
        this.setOption(Property.REMARKS, remarks.getValue());
    }

    public void setSecondaryUrl(boolean secondaryUrl) {
        this.setOption(Property.SECONDARY_URL, secondaryUrl);
    }

    public void setSecure(boolean secure) {
        this.setOption(Property.SECURE, secure);
    }

    public void setServerTrace(ServerTrace serverTrace) {
        this.setOption(Property.SERVER_TRACE, serverTrace.getValue());
    }

    public void setThreadUsed(boolean threadUsed) {
        this.setOption(Property.THREAD_USED, threadUsed);
    }

    public void setToolboxTrace(ToolboxTrace toolboxTrace) {
        this.setOption(Property.TOOLBOX_TRACE, toolboxTrace.getValue());
    }

    public void setTrace(boolean trace) {
        this.setOption(Property.TRACE, trace);
    }

    public void setTranslateBinary(boolean translateBinary) {
        this.setOption(Property.TRANSLATE_BINARY, translateBinary);
    }

    public void setTranslateBoolean(boolean translateBoolean) {
        this.setOption(Property.TRANSLATE_BOOLEAN, translateBoolean);
    }

    public void setLibraries(List<String> libraries) {
        this.setOption(Property.LIBRARIES, libraries);
    }

    public void setAutoCommit(boolean autoCommit) {
        this.setOption(Property.AUTO_COMMIT, autoCommit);
    }

    public void setConcurrentAccessResolution(ConcurrentAccessResolution concurrentAccessResolution) {
        this.setOption(Property.CONCURRENT_ACCESS_RESOLUTION, concurrentAccessResolution.getValue());
    }

    public void setCursorHold(boolean cursorHold) {
        this.setOption(Property.CURSOR_HOLD, cursorHold);
    }

    public void setCursorSensitivity(CursorSensitivity cursorSensitivity) {
        this.setOption(Property.CURSOR_SENSITIVITY, cursorSensitivity.getValue());
    }

    public void setDatabaseName(String databaseName) {
        this.setOption(Property.DATABASE_NAME, databaseName);
    }

    public void setDecfloatRoundingMode(DecfloatRoundingMode decfloatRoundingMode) {
        this.setOption(Property.DECFLOAT_ROUNDING_MODE, decfloatRoundingMode.getValue());
    }

    public void setMaximumPrecision(MaximumPrecision maximumPrecision) {
        this.setOption(Property.MAXIMUM_PRECISION, maximumPrecision.getValue());
    }

    public void setMaximumScale(String maximumScale) {
        this.setOption(Property.MAXIMUM_SCALE, maximumScale);
    }

    public void setMinimumDivideScale(MinimumDivideScale minimumDivideScale) {
        this.setOption(Property.MINIMUM_DIVIDE_SCALE, minimumDivideScale.getValue());
    }

    public void setPackageCcsid(PackageCcsid packageCcsid) {
        this.setOption(Property.PACKAGE_CCID, packageCcsid.getValue());
    }

    public void setTransactionIsolation(TransactionIsolation transactionIsolation) {
        this.setOption(Property.TRANSACTION_ISOLATION, transactionIsolation.getValue());
    }

    public void setTranslateHex(TranslateHex translateHex) {
        this.setOption(Property.TRANSLATE_HEX, translateHex.getValue());
    }

    public void setTrueAutocommit(boolean trueAutocommit) {
        this.setOption(Property.TRUE_AUTOCOMMIT, trueAutocommit);
    }

    public void setXALooselyCoupledSupport(XALooselyCoupledSupport xaLooselyCoupledSupport) {
        this.setOption(Property.XA_LOOSELY_COUPLED_SUPPORT, xaLooselyCoupledSupport.getValue());
    }

    public void setBigDecimal(boolean bigDecimal) {
        this.setOption(Property.BIG_DECIMAL, bigDecimal);
    }

    public void setBlockCriteria(BlockCriteria blockCriteria) {
        this.setOption(Property.BLOCK_CRITERIA, blockCriteria.getValue());
    }

    public void setBlockSize(BlockSize blockSize) {
        this.setOption(Property.BLOCK_SIZE, blockSize.getValue());
    }

    public void setDataCompression(boolean dataCompression) {
        this.setOption(Property.DATA_COMPRESSION, dataCompression);
    }

    public void setExtendedDynamic(boolean extendedDynamic) {
        this.setOption(Property.EXTENDED_DYNAMIC, extendedDynamic);
    }

    public void setLazyClose(boolean lazyClose) {
        this.setOption(Property.LAZY_CLOSE, lazyClose);
    }

    public void setLobThreshold(String lobThreshold) {
        this.setOption(Property.LOB_THRESHOLD, lobThreshold);
    }

    public void setMaximumBlockedInputRows(String maximumBlockedInputRows) {
        this.setOption(Property.MAXIMUM_BLOCKED_INPUT_ROWS, maximumBlockedInputRows);
    }

    public void setPackage(String pkg) {
        this.setOption(Property.PACKAGE, pkg);
    }

    public void setPackageAdd(boolean packageAdd) {
        this.setOption(Property.PACKAGE_ADD, packageAdd);
    }

    public void setPackageCache(boolean packageCache) {
        this.setOption(Property.PACKAGE_CACHE, packageCache);
    }

    public void setPackageCriteria(PackageCriteria packageCriteria) {
        this.setOption(Property.PACKAGE_CRITERIA, packageCriteria.getValue());
    }

    public void setPackageError(PackageError packageError) {
        this.setOption(Property.PACKAGE_ERROR, packageError.getValue());
    }

    public void setPackageLibrary(String packageLibrary) {
        this.setOption(Property.PACKAGE_LIBRARY, packageLibrary);
    }

    public void setPrefetch(boolean prefetch) {
        this.setOption(Property.PREFETCH, prefetch);
    }

    public void setQaqqinilib(String qaqqinilib) {
        this.setOption(Property.QAQQINILIB, qaqqinilib);
    }

    public void setQueryOptimizeGoal(QueryOptimizeGoal queryOptimizeGoal) {
        this.setOption(Property.QUERY_OPTIMIZE_GOAL, queryOptimizeGoal.getValue());
    }

    public void setQueryTimeoutMechanism(QueryTimeoutMechanism queryTimeoutMechanism) {
        this.setOption(Property.QUERY_TIMEOUT_MECHANISM, queryTimeoutMechanism.getValue());
    }

    public void setQueryStorageLimit(String queryStorageLimit) {
        this.setOption(Property.QUERY_STORAGE_LIMIT, queryStorageLimit);
    }

    public void receiveBufferSize(String receiveBufferSize) {
        this.setOption(Property.RECEIVE_BUFFER_SIZE, receiveBufferSize);
    }

    public void sendBufferSize(String sendBufferSize) {
        this.setOption(Property.SEND_BUFFER_SIZE, sendBufferSize);
    }

    public void vairiableFieldCompression(boolean vairiableFieldCompression) {
        this.setOption(Property.VARIABLE_FIELD_COMPRESSION, vairiableFieldCompression);
    }

    public void setSort(Sort sort) {
        this.setOption(Property.SORT, sort);
    }

    public void sortLanguage(String sortLanguage) {
        this.setOption(Property.SORT_LANGUAGE, sortLanguage);
    }

    public void sortTable(String sortTable) {
        this.setOption(Property.SORT_TABLE, sortTable);
    }

    public void setWeight(SortWeight sortWeight) {
        this.setOption(Property.SORT_WEIGHT, sortWeight);
    }

    enum Property {
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

    enum Naming {
        SQL("sql"),
        SYSTEM("system");

        private final String value;

        Naming(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Naming fromValue(String value) {
            for (Naming type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum DateFormat {
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

    enum DateSeparator {
        SLASH("/"),
        DASH("-"),
        DOT("."),
        COMMA(","),
        B("b");

        private final String value;

        DateSeparator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static DateSeparator fromValue(String value) {
            for (DateSeparator type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum DecimalSeparator {
        DOT("."),
        COMMA(",");

        private final String value;

        DecimalSeparator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static DecimalSeparator fromValue(String value) {
            for (DecimalSeparator type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum TimeFormat {
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

    enum TimeSeparator {
        COLON(":"),
        DOT("."),
        COMMA(","),
        B("b");

        private final String value;

        TimeSeparator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TimeSeparator fromValue(String value) {
            for (TimeSeparator type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum Access {
        ALL("all"),
        READ_CALL("read call"),
        READ_ONLY("read only");

        private final String value;

        Access(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Access fromValue(String value) {
            for (Access type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum BidiStringType {
        TYPE_4("4"),
        TYPE_5("5"),
        TYPE_6("6"),
        TYPE_7("7"),
        TYPE_8("8"),
        TYPE_9("9"),
        TYPE_10("10"),
        TYPE_11("11");

        private final String value;

        BidiStringType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static BidiStringType fromValue(String value) {
            for (BidiStringType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum Driver {
        TOOLBOX("toolbox"),
        NATIVE("native");

        private final String value;

        Driver(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Driver fromValue(String value) {
            for (Driver type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum Error {
        FULL("full"),
        BASIC("basic");

        private final String value;

        Error(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Error fromValue(String value) {
            for (Error type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum MetadataSource {
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

    enum Remarks {
        SQL("sql"),
        SYSTEM("system");

        private final String value;

        Remarks(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Remarks fromValue(String value) {
            for (Remarks type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum ServerTrace {
        TRACE_0("0"),
        TRACE_2("2"),
        TRACE_4("4"),
        TRACE_8("8"),
        TRACE_16("16"),
        TRACE_32("32"),
        TRACE_64("64");

        private final String value;

        ServerTrace(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ServerTrace fromValue(String value) {
            for (ServerTrace type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum ToolboxTrace {
        EMPTY(""),
        NONE("NONE"),
        DATASTREAM("datastream"),
        DIAGNOSTIC("diagnostic"),
        ERROR("error"),
        WARNING("warning"),
        CONVERSION("conversion"),
        JDBC("jdbc"),
        PCML("pcml"),
        ALL("all"),
        PROXY("proxy"),
        THREAD("thread"),
        INFORMATION("information");

        private final String value;

        ToolboxTrace(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ToolboxTrace fromValue(String value) {
            for (ToolboxTrace type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum ConcurrentAccessResolution {
        RESOLUTION_1("1"),
        RESOLUTION_2("2"),
        RESOLUTION_3("3");

        private final String value;

        ConcurrentAccessResolution(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ConcurrentAccessResolution fromValue(String value) {
            for (ConcurrentAccessResolution type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum CursorSensitivity {
        ASENSITIVE("asensitive"),
        INSENSITIVE("insensitive"),
        SENSITIVE("sensitive");

        private final String value;

        CursorSensitivity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static CursorSensitivity fromValue(String value) {
            for (CursorSensitivity type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

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

    public enum MaximumPrecision {
        PRECISION_31("31"),
        PRECISION_63("63");

        private final String value;

        MaximumPrecision(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MaximumPrecision fromValue(String value) {
            for (MaximumPrecision type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum MinimumDivideScale {
        SCALE_0("0"),
        SCALE_1("1"),
        SCALE_2("2"),
        SCALE_3("3"),
        SCALE_4("4"),
        SCALE_5("5"),
        SCALE_6("6"),
        SCALE_7("7"),
        SCALE_8("8"),
        SCALE_9("9");

        private final String value;

        MinimumDivideScale(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MinimumDivideScale fromValue(String value) {
            for (MinimumDivideScale type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

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

    enum TransactionIsolation {
        NONE("none"),
        READ_UNCOMMITTED("read uncommitted"),
        READ_COMMITTED("read committed"),
        REPEATABLE_READ("repeatable read"),
        SERIALIZABLE("serializable");

        private final String value;

        TransactionIsolation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TransactionIsolation fromValue(String value) {
            for (TransactionIsolation type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum TranslateHex {
        CHARACTER("character"),
        BINARY("binary");

        private final String value;

        TranslateHex(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TranslateHex fromValue(String value) {
            for (TranslateHex type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

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

    public enum BlockCriteria {
        CRITERIA_0("0"),
        CRITERIA_1("1"),
        CRITERIA_2("2");

        private final String value;

        BlockCriteria(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static BlockCriteria fromValue(String value) {
            for (BlockCriteria type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

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

    public enum PackageCriteria {
        DEFAULT("default"),
        SELECT("select");

        private final String value;

        PackageCriteria(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PackageCriteria fromValue(String value) {
            for (PackageCriteria type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum PackageError {
        EXCEPTION("exception"),
        WARNING("warning"),
        NONE("none");

        private final String value;

        PackageError(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PackageError fromValue(String value) {
            for (PackageError type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum QueryOptimizeGoal {
        GOAL_0("0"),
        GOAL_1("1"),
        GOAL_2("2");

        private final String value;

        QueryOptimizeGoal(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static QueryOptimizeGoal fromValue(String value) {
            for (QueryOptimizeGoal type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    enum QueryTimeoutMechanism {
        QQRYTIMLMT("qqrytimlmt"),
        CANCEL("cancel");

        private final String value;

        QueryTimeoutMechanism(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static QueryTimeoutMechanism fromValue(String value) {
            for (QueryTimeoutMechanism type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum Sort {
        HEX("hex"),
        LANGUAGE("language"),
        TABLE("table");

        private final String value;

        Sort(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Sort fromValue(String value) {
            for (Sort type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }

    public enum SortWeight {
        SHARED("shared"),
        UNIQUE("unique");

        private final String value;

        SortWeight(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SortWeight fromValue(String value) {
            for (SortWeight type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown value: " + value);
        }
    }
}