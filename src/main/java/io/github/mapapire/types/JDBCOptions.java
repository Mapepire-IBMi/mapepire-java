package io.github.mapapire.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mapapire.types.jdbcOptions.Access;
import io.github.mapapire.types.jdbcOptions.BidiStringType;
import io.github.mapapire.types.jdbcOptions.BlockCriteria;
import io.github.mapapire.types.jdbcOptions.BlockSize;
import io.github.mapapire.types.jdbcOptions.ConcurrentAccessResolution;
import io.github.mapapire.types.jdbcOptions.CursorSensitivity;
import io.github.mapapire.types.jdbcOptions.DateFormat;
import io.github.mapapire.types.jdbcOptions.DateSeparator;
import io.github.mapapire.types.jdbcOptions.DecfloatRoundingMode;
import io.github.mapapire.types.jdbcOptions.DecimalSeparator;
import io.github.mapapire.types.jdbcOptions.Driver;
import io.github.mapapire.types.jdbcOptions.Error;
import io.github.mapapire.types.jdbcOptions.MaximumPrecision;
import io.github.mapapire.types.jdbcOptions.MetadataSource;
import io.github.mapapire.types.jdbcOptions.MinimumDivideScale;
import io.github.mapapire.types.jdbcOptions.Naming;
import io.github.mapapire.types.jdbcOptions.PackageCcsid;
import io.github.mapapire.types.jdbcOptions.PackageCriteria;
import io.github.mapapire.types.jdbcOptions.PackageError;
import io.github.mapapire.types.jdbcOptions.Property;
import io.github.mapapire.types.jdbcOptions.QueryOptimizeGoal;
import io.github.mapapire.types.jdbcOptions.QueryTimeoutMechanism;
import io.github.mapapire.types.jdbcOptions.Remarks;
import io.github.mapapire.types.jdbcOptions.ServerTrace;
import io.github.mapapire.types.jdbcOptions.Sort;
import io.github.mapapire.types.jdbcOptions.SortWeight;
import io.github.mapapire.types.jdbcOptions.TimeFormat;
import io.github.mapapire.types.jdbcOptions.TimeSeparator;
import io.github.mapapire.types.jdbcOptions.ToolboxTrace;
import io.github.mapapire.types.jdbcOptions.TransactionIsolation;
import io.github.mapapire.types.jdbcOptions.TranslateHex;
import io.github.mapapire.types.jdbcOptions.XALooselyCoupledSupport;

public class JDBCOptions {
    private Map<String, Object> options = new HashMap<>();

    public JDBCOptions() {

    }

    public Map<String, Object> getOptions() {
        return this.options;
    }

    public Object getOption(Property property) {
        return this.options.get(property.getValue());
    }

    private void setOption(Property property, Object value) {
        this.options.put(property.getValue(), value);
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
}
