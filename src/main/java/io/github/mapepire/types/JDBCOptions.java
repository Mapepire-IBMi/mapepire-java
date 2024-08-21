package io.github.mapepire.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.mapepire.types.jdbcOptions.Access;
import io.github.mapepire.types.jdbcOptions.BidiStringType;
import io.github.mapepire.types.jdbcOptions.BlockCriteria;
import io.github.mapepire.types.jdbcOptions.BlockSize;
import io.github.mapepire.types.jdbcOptions.ConcurrentAccessResolution;
import io.github.mapepire.types.jdbcOptions.CursorSensitivity;
import io.github.mapepire.types.jdbcOptions.DateFormat;
import io.github.mapepire.types.jdbcOptions.DateSeparator;
import io.github.mapepire.types.jdbcOptions.DecfloatRoundingMode;
import io.github.mapepire.types.jdbcOptions.DecimalSeparator;
import io.github.mapepire.types.jdbcOptions.Driver;
import io.github.mapepire.types.jdbcOptions.Errors;
import io.github.mapepire.types.jdbcOptions.MaximumPrecision;
import io.github.mapepire.types.jdbcOptions.MetadataSource;
import io.github.mapepire.types.jdbcOptions.MinimumDivideScale;
import io.github.mapepire.types.jdbcOptions.Naming;
import io.github.mapepire.types.jdbcOptions.PackageCcsid;
import io.github.mapepire.types.jdbcOptions.PackageCriteria;
import io.github.mapepire.types.jdbcOptions.PackageError;
import io.github.mapepire.types.jdbcOptions.Option;
import io.github.mapepire.types.jdbcOptions.QueryOptimizeGoal;
import io.github.mapepire.types.jdbcOptions.QueryTimeoutMechanism;
import io.github.mapepire.types.jdbcOptions.Remarks;
import io.github.mapepire.types.jdbcOptions.ServerTrace;
import io.github.mapepire.types.jdbcOptions.Sort;
import io.github.mapepire.types.jdbcOptions.SortWeight;
import io.github.mapepire.types.jdbcOptions.TimeFormat;
import io.github.mapepire.types.jdbcOptions.TimeSeparator;
import io.github.mapepire.types.jdbcOptions.ToolboxTrace;
import io.github.mapepire.types.jdbcOptions.TransactionIsolation;
import io.github.mapepire.types.jdbcOptions.TranslateHex;
import io.github.mapepire.types.jdbcOptions.XALooselyCoupledSupport;

/**
 * Represents the JDBC options.
 */
public class JDBCOptions {
    /**
     * Map of JDBC options.
     */
    private Map<String, Object> options = new HashMap<>();

    /**
     * Construct a new JDBCOptions instance.
     */
    public JDBCOptions() {

    }

    /**
     * Get the map of JDBC options.
     * 
     * @return The map of JDBC options.
     */
    public Map<String, Object> getOptions() {
        return this.options;
    }

    /**
     * Get the value of a JDBC option.
     * 
     * @param option The JDBC option.
     * @return The value of the JDBC option.
     */
    public Object getOption(Option option) {
        return this.options.get(option.getValue());
    }

    /**
     * Set the value of a JDBC option.
     * 
     * @param option The JDBC option.
     * @param value  The value of the JDBC option.
     */
    private void setOption(Option option, Object value) {
        this.options.put(option.getValue(), value);
    }

    /**
     * Set the "naming" JDBC option.
     * 
     * @param naming The value to set.
     */
    public void setNaming(Naming naming) {
        this.setOption(Option.NAMING, naming.getValue());
    }

    /**
     * Set the "date format" JDBC option.
     * 
     * @param dateFormat The value to set.
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.setOption(Option.DATE_FORMAT, dateFormat.getValue());
    }

    /**
     * Set the "date separator" JDBC option.
     * 
     * @param dateSeparator The value to set.
     */
    public void setDateSeparator(DateSeparator dateSeparator) {
        this.setOption(Option.DATE_SEPARATOR, dateSeparator.getValue());
    }

    /**
     * Set the "decimal separator" JDBC option.
     * 
     * @param decimalSeparator The value to set.
     */
    public void setDecimalSeparator(DecimalSeparator decimalSeparator) {
        this.setOption(Option.DECIMAL_SEPARATOR, decimalSeparator.getValue());
    }

    /**
     * Set the "time format" JDBC option.
     * 
     * @param timeFormat The value to set.
     */
    public void setTimeFormat(TimeFormat timeFormat) {
        this.setOption(Option.TIME_FORMAT, timeFormat.getValue());
    }

    /**
     * Set the "time separator" JDBC option.
     * 
     * @param timeSeparator The value to set.
     */
    public void setTimeSeparator(TimeSeparator timeSeparator) {
        this.setOption(Option.TIME_SEPARATOR, timeSeparator.getValue());
    }

    /**
     * Set the "full open" JDBC option.
     * 
     * @param fullOpen The value to set.
     */
    public void setFullOpen(boolean fullOpen) {
        this.setOption(Option.FULL_OPEN, fullOpen);
    }

    /**
     * Set the "access" JDBC option.
     * 
     * @param access The value to set.
     */
    public void setAccess(Access access) {
        this.setOption(Option.ACCESS, access.getValue());
    }

    /**
     * Set the "autocommit exception" JDBC option.
     * 
     * @param autocommitException The value to set.
     */
    public void setAutocommitException(String autocommitException) {
        this.setOption(Option.AUTOCOMMIT_EXCEPTION, autocommitException);
    }

    /**
     * Set the "bidi string type" JDBC option.
     * 
     * @param bidiStringType The value to set.
     */
    public void setBidiStringType(BidiStringType bidiStringType) {
        this.setOption(Option.BIDI_STRING_TYPE, bidiStringType.getValue());
    }

    /**
     * Set the "bidi implicit reordering" JDBC option.
     * 
     * @param bidiImplicitReordering The value to set.
     */
    public void setBidiImplicitReordering(boolean bidiImplicitReordering) {
        this.setOption(Option.BIDI_IMPLICIT_REORDERING, bidiImplicitReordering);
    }

    /**
     * Set the "bidi numeric ordering" JDBC option.
     * 
     * @param bidiNumericOrdering The value to set.
     */
    public void setBidiNumericOrdering(boolean bidiNumericOrdering) {
        this.setOption(Option.BIDI_NUMERIC_ORDERING, bidiNumericOrdering);
    }

    /**
     * Set the "data truncation" JDBC option.
     * 
     * @param dataTruncation The value to set.
     */
    public void setDataTruncation(boolean dataTruncation) {
        this.setOption(Option.DATA_TRUNCATION, dataTruncation);
    }

    /**
     * Set the "driver" JDBC option.
     * 
     * @param driver The value to set.
     */
    public void setDriver(Driver driver) {
        this.setOption(Option.DRIVER, driver.getValue());
    }

    /**
     * Set the "errors" JDBC option.
     * 
     * @param errors The value to set.
     */
    public void setErrors(Errors errors) {
        this.setOption(Option.ERRORS, errors.getValue());
    }

    /**
     * Set the "extended metadata" JDBC option.
     * 
     * @param extendedMetadata The value to set.
     */
    public void setExtendedMetadata(boolean extendedMetadata) {
        this.setOption(Option.EXTENDED_METADATA, extendedMetadata);
    }

    /**
     * Set the "hold input locators" JDBC option.
     * 
     * @param holdInputLocators The value to set.
     */
    public void setHoldInputLocators(boolean holdInputLocators) {
        this.setOption(Option.HOLD_INPUT_LOCATORS, holdInputLocators);
    }

    /**
     * Set the "hold statements" JDBC option.
     * 
     * @param holdStatements The value to set.
     */
    public void setHoldStatements(boolean holdStatements) {
        this.setOption(Option.HOLD_STATEMENTS, holdStatements);
    }

    /**
     * Set the "ignore warnings" JDBC option.
     * 
     * @param ignoreWarnings The value to set.
     */
    public void setIgnoreWarnings(String ignoreWarnings) {
        this.setOption(Option.IGNORE_WARNINGS, ignoreWarnings);
    }

    /**
     * Set the "keep alive" JDBC option.
     * 
     * @param keepAlive The value to set.
     */
    public void setKeepAlive(boolean keepAlive) {
        this.setOption(Option.KEEP_ALIVE, keepAlive);
    }

    /**
     * Set the "key ring name" JDBC option.
     * 
     * @param keyRingName The value to set.
     */
    public void setKeyRingName(String keyRingName) {
        this.setOption(Option.KEY_RING_NAME, keyRingName);
    }

    /**
     * Set the "key ring password" JDBC option.
     * 
     * @param keyRingPassword The value to set.
     */
    public void setKeyRingPassword(String keyRingPassword) {
        this.setOption(Option.KEY_RING_PASSWORD, keyRingPassword);
    }

    /**
     * Set the "metadata source" JDBC option.
     * 
     * @param metadataSource The value to set.
     */
    public void setMetadataSource(MetadataSource metadataSource) {
        this.setOption(Option.METADATA_SOURCE, metadataSource.getValue());
    }

    /**
     * Set the "proxy server" JDBC option.
     * 
     * @param proxyServer The value to set.
     */
    public void setProxyServer(String proxyServer) {
        this.setOption(Option.PROXY_SERVER, proxyServer);
    }

    /**
     * Set the "remarks" JDBC option.
     * 
     * @param remarks The value to set.
     */
    public void setRemarks(Remarks remarks) {
        this.setOption(Option.REMARKS, remarks.getValue());
    }

    /**
     * Set the "secondary URL" JDBC option.
     * 
     * @param secondaryUrl The value to set.
     */
    public void setSecondaryUrl(boolean secondaryUrl) {
        this.setOption(Option.SECONDARY_URL, secondaryUrl);
    }

    /**
     * Set the "secure" JDBC option.
     * 
     * @param secure The value to set.
     */
    public void setSecure(boolean secure) {
        this.setOption(Option.SECURE, secure);
    }

    /**
     * Set the "server trace" JDBC option.
     * 
     * @param serverTrace The value to set.
     */
    public void setServerTrace(ServerTrace serverTrace) {
        this.setOption(Option.SERVER_TRACE, serverTrace.getValue());
    }

    /**
     * Set the "thread used" JDBC option.
     * 
     * @param threadUsed The value to set.
     */
    public void setThreadUsed(boolean threadUsed) {
        this.setOption(Option.THREAD_USED, threadUsed);
    }

    /**
     * Set the "toolbox trace" JDBC option.
     * 
     * @param toolboxTrace The value to set.
     */
    public void setToolboxTrace(ToolboxTrace toolboxTrace) {
        this.setOption(Option.TOOLBOX_TRACE, toolboxTrace.getValue());
    }

    /**
     * Set the "trace" JDBC option.
     * 
     * @param trace The value to set.
     */
    public void setTrace(boolean trace) {
        this.setOption(Option.TRACE, trace);
    }

    /**
     * Set the "translate binary" JDBC option.
     * 
     * @param translateBinary The value to set.
     */
    public void setTranslateBinary(boolean translateBinary) {
        this.setOption(Option.TRANSLATE_BINARY, translateBinary);
    }

    /**
     * Set the "translate boolean" JDBC option.
     * 
     * @param translateBoolean The value to set.
     */
    public void setTranslateBoolean(boolean translateBoolean) {
        this.setOption(Option.TRANSLATE_BOOLEAN, translateBoolean);
    }

    /**
     * Set the "libraries" JDBC option.
     * 
     * @param libraries The value to set.
     */
    public void setLibraries(List<String> libraries) {
        this.setOption(Option.LIBRARIES, libraries);
    }

    /**
     * Set the "auto commit" JDBC option.
     * 
     * @param autoCommit The value to set.
     */
    public void setAutoCommit(boolean autoCommit) {
        this.setOption(Option.AUTO_COMMIT, autoCommit);
    }

    /**
     * Set the "concurrent access resolution" JDBC option.
     * 
     * @param concurrentAccessResolution The value to set.
     */
    public void setConcurrentAccessResolution(ConcurrentAccessResolution concurrentAccessResolution) {
        this.setOption(Option.CONCURRENT_ACCESS_RESOLUTION, concurrentAccessResolution.getValue());
    }

    /**
     * Set the "cursor hold" JDBC option.
     * 
     * @param cursorHold The value to set.
     */
    public void setCursorHold(boolean cursorHold) {
        this.setOption(Option.CURSOR_HOLD, cursorHold);
    }

    /**
     * Set the "cursor sensitivity" JDBC option.
     * 
     * @param cursorSensitivity The value to set.
     */
    public void setCursorSensitivity(CursorSensitivity cursorSensitivity) {
        this.setOption(Option.CURSOR_SENSITIVITY, cursorSensitivity.getValue());
    }

    /**
     * Set the "database name" JDBC option.
     * 
     * @param databaseName The value to set.
     */
    public void setDatabaseName(String databaseName) {
        this.setOption(Option.DATABASE_NAME, databaseName);
    }

    /**
     * Set the "decfloat rounding mode" JDBC option.
     * 
     * @param decfloatRoundingMode The value to set.
     */
    public void setDecfloatRoundingMode(DecfloatRoundingMode decfloatRoundingMode) {
        this.setOption(Option.DECFLOAT_ROUNDING_MODE, decfloatRoundingMode.getValue());
    }

    /**
     * Set the "maximum precision" JDBC option.
     * 
     * @param maximumPrecision The value to set.
     */
    public void setMaximumPrecision(MaximumPrecision maximumPrecision) {
        this.setOption(Option.MAXIMUM_PRECISION, maximumPrecision.getValue());
    }

    /**
     * Set the "maximum scale" JDBC option.
     * 
     * @param maximumScale The value to set.
     */
    public void setMaximumScale(String maximumScale) {
        this.setOption(Option.MAXIMUM_SCALE, maximumScale);
    }

    /**
     * Set the "minimum divide scale" JDBC option.
     * 
     * @param minimumDivideScale The value to set.
     */
    public void setMinimumDivideScale(MinimumDivideScale minimumDivideScale) {
        this.setOption(Option.MINIMUM_DIVIDE_SCALE, minimumDivideScale.getValue());
    }

    /**
     * Set the "package ccsid" JDBC option.
     * 
     * @param packageCcsid The value to set.
     */
    public void setPackageCcsid(PackageCcsid packageCcsid) {
        this.setOption(Option.PACKAGE_CCSID, packageCcsid.getValue());
    }

    /**
     * Set the "transaction isolation" JDBC option.
     * 
     * @param transactionIsolation The value to set.
     */
    public void setTransactionIsolation(TransactionIsolation transactionIsolation) {
        this.setOption(Option.TRANSACTION_ISOLATION, transactionIsolation.getValue());
    }

    /**
     * Set the "translate hex" JDBC option.
     * 
     * @param translateHex The value to set.
     */
    public void setTranslateHex(TranslateHex translateHex) {
        this.setOption(Option.TRANSLATE_HEX, translateHex.getValue());
    }

    /**
     * Set the "true autocommit" JDBC option.
     * 
     * @param trueAutocommit The value to set.
     */
    public void setTrueAutocommit(boolean trueAutocommit) {
        this.setOption(Option.TRUE_AUTOCOMMIT, trueAutocommit);
    }

    /**
     * Set the "xa loosely coupled support" JDBC option.
     * 
     * @param xaLooselyCoupledSupport The value to set.
     */
    public void setXALooselyCoupledSupport(XALooselyCoupledSupport xaLooselyCoupledSupport) {
        this.setOption(Option.XA_LOOSELY_COUPLED_SUPPORT, xaLooselyCoupledSupport.getValue());
    }

    /**
     * Set the "big decimal" JDBC option.
     * 
     * @param bigDecimal The value to set.
     */
    public void setBigDecimal(boolean bigDecimal) {
        this.setOption(Option.BIG_DECIMAL, bigDecimal);
    }

    /**
     * Set the "block criteria" JDBC option.
     * 
     * @param blockCriteria The value to set.
     */
    public void setBlockCriteria(BlockCriteria blockCriteria) {
        this.setOption(Option.BLOCK_CRITERIA, blockCriteria.getValue());
    }

    /**
     * Set the "block size" JDBC option.
     * 
     * @param blockSize The value to set.
     */
    public void setBlockSize(BlockSize blockSize) {
        this.setOption(Option.BLOCK_SIZE, blockSize.getValue());
    }

    /**
     * Set the "data compression" JDBC option.
     * 
     * @param dataCompression The value to set.
     */
    public void setDataCompression(boolean dataCompression) {
        this.setOption(Option.DATA_COMPRESSION, dataCompression);
    }

    /**
     * Set the "extended dynamic" JDBC option.
     * 
     * @param extendedDynamic The value to set.
     */
    public void setExtendedDynamic(boolean extendedDynamic) {
        this.setOption(Option.EXTENDED_DYNAMIC, extendedDynamic);
    }

    /**
     * Set the "lazy close" JDBC option.
     * 
     * @param lazyClose The value to set.
     */
    public void setLazyClose(boolean lazyClose) {
        this.setOption(Option.LAZY_CLOSE, lazyClose);
    }

    /**
     * Set the "lob threshold" JDBC option.
     * 
     * @param lobThreshold The value to set.
     */
    public void setLobThreshold(String lobThreshold) {
        this.setOption(Option.LOB_THRESHOLD, lobThreshold);
    }

    /**
     * Set the "maximum blocked input rows" JDBC option.
     * 
     * @param maximumBlockedInputRows The value to set.
     */
    public void setMaximumBlockedInputRows(String maximumBlockedInputRows) {
        this.setOption(Option.MAXIMUM_BLOCKED_INPUT_ROWS, maximumBlockedInputRows);
    }

    /**
     * Set the "package" JDBC option.
     * 
     * @param pkg The value to set.
     */
    public void setPackage(String pkg) {
        this.setOption(Option.PACKAGE, pkg);
    }

    /**
     * Set the "package add" JDBC option.
     * 
     * @param packageAdd The value to set.
     */
    public void setPackageAdd(boolean packageAdd) {
        this.setOption(Option.PACKAGE_ADD, packageAdd);
    }

    /**
     * Set the "package cache" JDBC option.
     * 
     * @param packageCache The value to set.
     */
    public void setPackageCache(boolean packageCache) {
        this.setOption(Option.PACKAGE_CACHE, packageCache);
    }

    /**
     * Set the "package criteria" JDBC option.
     * 
     * @param packageCriteria The value to set.
     */
    public void setPackageCriteria(PackageCriteria packageCriteria) {
        this.setOption(Option.PACKAGE_CRITERIA, packageCriteria.getValue());
    }

    /**
     * Set the "package error" JDBC option.
     * 
     * @param packageError The value to set.
     */
    public void setPackageError(PackageError packageError) {
        this.setOption(Option.PACKAGE_ERROR, packageError.getValue());
    }

    /**
     * Set the "package library" JDBC option.
     * 
     * @param packageLibrary The value to set.
     */
    public void setPackageLibrary(String packageLibrary) {
        this.setOption(Option.PACKAGE_LIBRARY, packageLibrary);
    }

    /**
     * Set the "prefetch" JDBC option.
     * 
     * @param prefetch The value to set.
     */
    public void setPrefetch(boolean prefetch) {
        this.setOption(Option.PREFETCH, prefetch);
    }

    /**
     * Set the "qaqqinilib" JDBC option.
     * 
     * @param qaqqinilib The value to set.
     */
    public void setQaqqinilib(String qaqqinilib) {
        this.setOption(Option.QAQQINILIB, qaqqinilib);
    }

    /**
     * Set the "query optimize goal" JDBC option.
     * 
     * @param queryOptimizeGoal The value to set.
     */
    public void setQueryOptimizeGoal(QueryOptimizeGoal queryOptimizeGoal) {
        this.setOption(Option.QUERY_OPTIMIZE_GOAL, queryOptimizeGoal.getValue());
    }

    /**
     * Set the "query timeout mechanism" JDBC option.
     * 
     * @param queryTimeoutMechanism The value to set.
     */
    public void setQueryTimeoutMechanism(QueryTimeoutMechanism queryTimeoutMechanism) {
        this.setOption(Option.QUERY_TIMEOUT_MECHANISM, queryTimeoutMechanism.getValue());
    }

    /**
     * Set the "query storage limit" JDBC option.
     * 
     * @param queryStorageLimit The value to set.
     */
    public void setQueryStorageLimit(String queryStorageLimit) {
        this.setOption(Option.QUERY_STORAGE_LIMIT, queryStorageLimit);
    }

    /**
     * Set the "receive buffer size" JDBC option.
     * 
     * @param receiveBufferSize The value to set.
     */
    public void receiveBufferSize(String receiveBufferSize) {
        this.setOption(Option.RECEIVE_BUFFER_SIZE, receiveBufferSize);
    }

    /**
     * Set the "send buffer size" JDBC option.
     * 
     * @param sendBufferSize The value to set.
     */
    public void sendBufferSize(String sendBufferSize) {
        this.setOption(Option.SEND_BUFFER_SIZE, sendBufferSize);
    }

    /**
     * Set the "variable field compression" JDBC option.
     * 
     * @param variableFieldCompression The value to set.
     */
    public void variableFieldCompression(boolean variableFieldCompression) {
        this.setOption(Option.VARIABLE_FIELD_COMPRESSION, variableFieldCompression);
    }

    /**
     * Set the "sort" JDBC option.
     * 
     * @param sort The value to set.
     */
    public void setSort(Sort sort) {
        this.setOption(Option.SORT, sort);
    }

    /**
     * Set the "sort language" JDBC option.
     * 
     * @param sortLanguage The value to set.
     */
    public void sortLanguage(String sortLanguage) {
        this.setOption(Option.SORT_LANGUAGE, sortLanguage);
    }

    /**
     * Set the "sort table" JDBC option.
     * 
     * @param sortTable The value to set.
     */
    public void sortTable(String sortTable) {
        this.setOption(Option.SORT_TABLE, sortTable);
    }

    /**
     * Set the "sort weight" JDBC option.
     * 
     * @param sortWeight The value to set.
     */
    public void setWeight(SortWeight sortWeight) {
        this.setOption(Option.SORT_WEIGHT, sortWeight);
    }
}
