package io.github.mapepire_ibmi.types.jdbcOptions;

/**
 * Enum representing the possible types of JDBC options.
 */
public enum Option {
    /**
     * Specifies the naming convention used when referring to tables.
     */
    NAMING("naming"),

    /**
     * Specifies the date format used in date literals within SQL statements.
     */
    DATE_FORMAT("date format"),

    /**
     * Specifies the date separator used in date literals within SQL statements.
     * This property has no effect unless the "date format" property is set to
     * "julian", "mdy", "dmy" or "ymd".
     */
    DATE_SEPARATOR("date separator"),

    /**
     * Specifies the decimal separator used in numeric literals within SQL
     * statements.
     */
    DECIMAL_SEPARATOR("decimal separator"),

    /**
     * Specifies the time format used in time literals within SQL statements.
     */
    TIME_FORMAT("time format"),

    /**
     * Specifies the time separator used in time literals within SQL statements.
     * This property has no effect unless the "time format" property is set to
     * "hms".
     */
    TIME_SEPARATOR("time separator"),

    /**
     * Specifies whether the server fully opens a file for each query. By default
     * the server optimizes open requests. This optimization improves performance
     * but may fail if a database monitor is active when a query is run more than
     * once. Set the property to true only when identical queries are issued when
     * monitors are active.
     */
    FULL_OPEN("full open"),

    /**
     * Specifies the level of database access for the connection.
     */
    ACCESS("access"),

    /**
     * Specifies whether to throw an SQLException when Connection.commit() or
     * Connection.rollback() is called if autocommit is enabled.
     */
    AUTOCOMMIT_EXCEPTION("autocommit exception"),

    /**
     * Specifies the output string type of bidirectional data.
     */
    BIDI_STRING_TYPE("bidi string type"),

    /**
     * Specifies if bidi implicit LTR-RTL reordering should be used.
     */
    BIDI_IMPLICIT_REORDERING("bidi implicit reordering"),

    /**
     * Specifies if the numeric ordering round trip feature should be used.
     */
    BIDI_NUMERIC_ORDERING("bidi numeric ordering"),

    /**
     * Specifies whether truncation of character data generates attention notices
     * and exceptions. When this property is "true", writing truncated character
     * data to the database throws an exception and using truncated character data
     * in a query posts an attention notice. When this property is "false", writing
     * truncated data to the database or using such data in a query generates no
     * exception or attention notice. The default value is "true". This property
     * does not affect numeric data. Writing truncated numeric data to the database
     * always throws an error and using truncated numeric data in a query always
     * posts attention notices.
     */
    DATA_TRUNCATION("data truncation"),

    /**
     * Specifies the JDBC driver implementation. The IBM Toolbox for Java JDBC
     * driver can use different JDBC driver implementations based on the
     * environment. If the environment is an IBM i JVM on the same server as the
     * database to which the program is connecting, the native IBM Developer Kit for
     * Java JDBC driver can be used. In any other environment, the IBM Toolbox for
     * Java JDBC driver is used. This property has no effect if the "secondary URL"
     * property is set.
     */
    DRIVER("driver"),

    /**
     * Specifies the amount of detail to be returned in the message for errors that
     * occur on the server.
     */
    ERRORS("errors"),

    /**
     * Specifies whether the driver requests extended metadata from the server.
     * Setting this property to true increases the accuracy of the information
     * returned from the following ResultSetMetaData methods: getColumnLabel(int),
     * isReadOnly(int), isSearchable(int), isWriteable(int). Additionally, setting
     * this property to true enables support for the
     * ResultSetMetaData.getSchemaName(int) and
     * ResultSetMetaData.isAutoIncrement(int) methods.
     */
    EXTENDED_METADATA("extended metadata"),

    /**
     * Specifies whether input locators should be allocated as type hold locators or
     * not hold locators. If the locators are of type hold, they will not be
     * released when a commit is done.
     */
    HOLD_INPUT_LOCATORS("hold input locators"),

    /**
     * Specifies if statements should remain open until a transaction boundary when
     * autocommit is off and they are associated with a LOB locator. By default, all
     * the resources associated with a statement are released when the statement is
     * closed. Set this property to true only when access to a LOB locator is needed
     * after a statement has been closed.
     */
    HOLD_STATEMENTS("hold statements"),

    /**
     * Specifies a list of SQL states for which the driver should not create warning
     * objects. By default, the IBM Toolbox for Java JDBC driver will internally
     * create a java.sql.SQLWarning object for each warning returned by the
     * database. For example, a warning with the SQLSTATE 0100C is created every
     * time a result set is returned from a stored procedure. This warning can be
     * safely ignored to improve the performance of applications that call stored
     * procedures.
     */
    IGNORE_WARNINGS("ignore warnings"),

    /**
     * Specifies whether socket connection is to be periodically checked for
     * operational status.
     */
    KEEP_ALIVE("keep alive"),

    /**
     * Specifies the key ring class name used for SSL connections with the server.
     * This property has no effect unless "secure" is set to true and a key ring
     * password is set using the "key ring password" property.
     */
    KEY_RING_NAME("key ring name"),

    /**
     * Specifies the password for the key ring class used for SSL communications
     * with the server. This property has no effect unless "secure" is set to true
     * and a key ring name is set using the "key ring name" property.
     */
    KEY_RING_PASSWORD("key ring password"),

    /**
     * Specifies how to retrieve DatabaseMetaData. If set to "0," database metadata
     * will be retrieved through the Retrieve Object Information (ROI) data flow. If
     * set to "1," database metadata will be retrieved by calling system stored
     * procedures.
     */
    METADATA_SOURCE("metadata source"),

    /**
     * Specifies the host name and port of the middle-tier machine where the proxy
     * server is running. The format for this is hostname[:port], where the port is
     * optional. If this is not set, then the hostname and port are retrieved from
     * the com.ibm.as400.access.AS400.proxyServer property. The default port is 3470
     * (if the connection uses SSL, the default port is 3471). The ProxyServer must
     * be running on the middle-tier machine. The name of the middle-tier machine is
     * ignored in a two-tier environment.
     */
    PROXY_SERVER("proxy server"),

    /**
     * Specifies the source of the text for REMARKS columns in ResultSets returned
     * by DatabaseMetaData methods. This property is only used if the "metadata
     * source" property is set to "0".
     */
    REMARKS("remarks"),

    /**
     * Specifies the URL to be used for a connection on the middle-tier's
     * DriverManager in a multiple tier environment, if it is different than already
     * specified. This property allows you to use this driver to connect to other
     * databases. Use a backslash as an escape character before backslashes and
     * semicolons in the URL.
     */
    SECONDARY_URL("secondary URL"),

    /**
     * Specifies whether a Secure Sockets Layer (SSL) connection is used to
     * communicate with the server.
     */
    SECURE("secure"),

    /**
     * Specifies the level of tracing of the JDBC server job. When tracing is
     * enabled, tracing starts when the client connects to the server and ends when
     * the connection is disconnected. You must start tracing before connecting to
     * the server, because the client enables server tracing only at connect time.
     */
    SERVER_TRACE("server trace"),

    /**
     * Specifies whether threads are used in communication with the host servers.
     */
    THREAD_USED("thread used"),

    /**
     * Specifies what category of an IBM Toolbox for Java trace to log. Trace
     * messages are useful for debugging programs that call JDBC. However, there is
     * a performance penalty associated with logging trace messages, so this
     * property is only set for debugging. Trace messages are logged to System.out.
     */
    TOOLBOX_TRACE("toolbox trace"),

    /**
     * Specifies whether trace messages are logged. Trace messages are useful for
     * debugging programs that call JDBC. However, there is a performance penalty
     * associated with logging trace messages, so this property only set to "true"
     * for debugging. Trace messages are logged to System.out.
     */
    TRACE("trace"),

    /**
     * Specifies whether binary data is translated. If this property is set to
     * "true", then BINARY and VARBINARY fields are treated as CHAR and VARCHAR
     * fields.
     */
    TRANSLATE_BINARY("translate binary"),

    /**
     * Specifies how Boolean objects are interpreted when setting the value for a
     * character field/parameter using the PreparedStatement.setObject(),
     * CallableStatement.setObject() or ResultSet.updateObject() methods. Setting
     * the property to "true" would store the Boolean object in the character field
     * as either "true" or "false." Setting the property to "false" would store the
     * Boolean object in the character field as either "1" or "0."
     */
    TRANSLATE_BOOLEAN("translate boolean"),

    /**
     * 
     * Specifies one or more libraries that you want to add to or replace the
     * library list of the server job, and optionally sets the default SQL schema
     * (default library). Note that libraries cannot be longer than 10 characters in
     * length. You must use the SET PATH SQL statement if you have libraries longer
     * than 10 characters.
     */
    LIBRARIES("libraries"),

    /**
     * Specifies whether auto-commit mode is the default connection mode for new
     * connections. Calling AS400JDBCConnection.setAutoCommit() will override this
     * property on a per-connection basis.
     */
    AUTO_COMMIT("auto commit"),

    /**
     * Specifies whether "currently committed" access is used on the connection. A
     * value of 1 indicates that "currently committed" will be used. A value of 2
     * indicates that "wait for outcome" will be used. A value of 3 indicates that
     * "skip locks" will be used.
     */
    CONCURRENT_ACCESS_RESOLUTION("concurrent access resolution"),

    /**
     * Specifies whether to hold the cursor across transactions. If this property is
     * set to "true", cursors are not closed when a transaction is committed or
     * rolled back. All resources acquired during the unit of work are held, but
     * locks on specific rows and objects implicitly acquired during the unit of
     * work are released.
     */
    CURSOR_HOLD("cursor hold"),

    /**
     * Specifies the cursor sensitivity to request from the database. The behavior
     * depends on the resultSetType. ResultSet.TYPE_FORWARD_ONLY or
     * ResultSet.TYPE_SCROLL_SENSITIVE means that the value of this property
     * controls what cursor sensitivity the Java™ program requests from the
     * database. ResultSet.TYPE_SCROLL_INSENSITIVE causes this property to be
     * ignored.
     */
    CURSOR_SENSITIVITY("cursor sensitivity"),

    /**
     * Specifies the database to use for the connection to an independent auxiliary
     * storage pool (ASP). This property applies only when connecting to an IBM® i
     * server. When you specify a database name, the name must exist in the
     * relational database directory on the server and correspond to either an
     * independent ASP or the system default database. There are several criteria
     * which determine which database is accessed. When this property is used to
     * specify a database which corresponds to an independent ASP, the connection is
     * made to the independent ASP. When the specified database does not exist, the
     * connection fails. When this property is used to specify *SYSBAS as the
     * database name, the system default database is used. When this property is
     * omitted, the initial ASP group specified in the job description for the user
     * profile is used. When the job description does not specify an initial ASP
     * group, the system default database is used.
     */
    DATABASE_NAME("database name"),

    /**
     * Specifies the rounding mode to use when working with the decfloat data type.
     * This property is ignored when connecting to systems running IBM i 5.4 and
     * earlier.
     */
    DECFLOAT_ROUNDING_MODE("decfloat rounding mode"),

    /**
     * Specifies the maximum decimal precision the database might use.
     */
    MAXIMUM_PRECISION("maximum precision"),

    /**
     * Specifies the maximum scale the database might use.
     */
    MAXIMUM_SCALE("maximum scale"),

    /**
     * Specifies the minimum scale value for the result of decimal division.
     */
    MINIMUM_DIVIDE_SCALE("minimum divide scale"),

    /**
     * Specifies the character encoding to use for the SQL package and any
     * statements sent to the server.
     */
    PACKAGE_CCSID("package ccsid"),

    /**
     * Specifies the default transaction isolation.
     */
    TRANSACTION_ISOLATION("transaction isolation"),

    /**
     * Specifies how hexadecimal literals are interpreted.
     */
    TRANSLATE_HEX("translate hex"),

    /**
     * Specifies whether the connection should use true auto commit support. True
     * autocommit means that autocommit is on and is running under a isolation level
     * other than *NONE. By default, the driver handles autocommit by running under
     * the server isolation level of *NONE.
     */
    TRUE_AUTOCOMMIT("true autocommit"),

    /**
     * Specifies whether lock sharing is allowed for loosely coupled transaction
     * branches.
     */
    XA_LOOSELY_COUPLED_SUPPORT("XA loosely coupled support"),

    /**
     * Specifies whether an intermediate java.math.BigDecimal object is used for
     * packed and zoned decimal conversions. If this property is set to "true", an
     * intermediate java.math.BigDecimal object is used for packed and zoned decimal
     * conversions as described by the JDBC specification. If this property is set
     * to "false", no intermediate objects are used for packed and zoned decimal
     * conversions. Instead, such values are converted directly to and from Java
     * double values. Such conversions will be faster but may not follow all
     * conversion and data truncation rules documented by the JDBC specification.
     */
    BIG_DECIMAL("big decimal"),

    /**
     * Specifies the criteria for retrieving data from the server in blocks of
     * records. Specifying a non-zero value for this property will reduce the
     * frequency of communication to the server, and therefore improve performance.
     * Ensure that record blocking is off if the cursor is going to be used for
     * subsequent UPDATEs, or else the row that is updated will not necessarily be
     * the current row.
     */
    BLOCK_CRITERIA("block criteria"),

    /**
     * Specifies the block size (in kilobytes) to retrieve from the server and cache
     * on the client. This property has no effect unless the "block criteria"
     * property is non-zero. Larger block sizes reduce the frequency of
     * communication to the server, and therefore may improve performance.
     */
    BLOCK_SIZE("block size"),

    /**
     * Specifies whether result set data is compressed. If this property is set to
     * "true", then result set data is compressed. If this property is set to
     * "false", then result set data is not compressed. Data compression may improve
     * performance when retrieving large result sets.
     */
    DATA_COMPRESSION("data compression"),

    /**
     * Specifies whether to use extended dynamic support. Extended dynamic support
     * provides a mechanism for caching dynamic SQL statements on the server. The
     * first time a particular SQL statement is prepared, it is stored in a SQL
     * package on the server. If the package does not exist, it is automatically
     * created. On subsequent prepares of the same SQL statement, the server can
     * skip a significant part of the processing by using information stored in the
     * SQL package. If this is set to "true", then a package name must be set using
     * the "package" property.
     */
    EXTENDED_DYNAMIC("extended dynamic"),

    /**
     * Specifies whether to delay closing cursors until subsequent requests. This
     * will improve overall performance by reducing the total number of requests.
     */
    LAZY_CLOSE("lazy close"),

    /**
     * Specifies the maximum LOB (large object) size (in bytes) that can be
     * retrieved as part of a result set. LOBs that are larger than this threshold
     * will be retrieved in pieces using extra communication to the server. Larger
     * LOB thresholds will reduce the frequency of communication to the server, but
     * will download more LOB data, even if it is not used. Smaller LOB thresholds
     * may increase frequency of communication to the server, but will only download
     * LOB data as it is needed.
     */
    LOB_THRESHOLD("lob threshold"),

    /**
     * Specifies the maximum number of rows to be sent to the database engine when
     * using a blocked insert or update operation. The database engine has a limit
     * of 32000 rows with a total of 16MB of data. This property may be used to
     * reduce the size of buffers in the JVM when using batched insert operations.
     */
    MAXIMUM_BLOCKED_INPUT_ROWS("maximum blocked input rows"),

    /**
     * Specifies the base name of the SQL package. Note that only the first six
     * characters are used to generate the name of the SQL package on the server.
     * This property has no effect unless the "extended dynamic" property is set to
     * "true". In addition, this property must be set if the "extended dynamic"
     * property is set to "true".
     */
    PACKAGE("package"),

    /**
     * Specifies whether to add newly prepared statements to the SQL package
     * specified on the "package" property. This property has no effect unless the
     * "extended dynamic" property is set to "true".
     */
    PACKAGE_ADD("package add"),

    /**
     * Specifies whether to cache a subset of the SQL package information in client
     * memory. Caching SQL packages locally reduces the amount of communication to
     * the server for prepares and describes. This property has no effect unless the
     * "extended dynamic" property is set to "true".
     */
    PACKAGE_CACHE("package cache"),

    /**
     * Specifies the type of SQL statements to be stored in the SQL package. This
     * can be useful to improve the performance of complex join conditions. This
     * property has no effect unless the "extended dynamic" property is set to
     * "true".
     */
    PACKAGE_CRITERIA("package criteria"),

    /**
     * Specifies the action to take when SQL package errors occur. When a SQL
     * package error occurs, the driver will optionally throw a SQLException or post
     * a notice to the Connection, based on the value of this property. This
     * property has no effect unless the "extended dynamic" property is set to
     * "true".
     */
    PACKAGE_ERROR("package error"),

    /**
     * Specifies the library for the SQL package. This property has no effect unless
     * the "extended dynamic" property is set to "true".
     */
    PACKAGE_LIBRARY("package library"),

    /**
     * Specifies whether to prefetch data upon executing a SELECT statement. This
     * will improve performance when accessing the initial rows in the ResultSet.
     */
    PREFETCH("prefetch"),

    /**
     * Specifies a QAQQINI library name. Used to specify the library that contains
     * the qaqqini file to use. A qaqqini file contains all of the attributes that
     * can potentially impact the performance of the Db2® for i database engine.
     */
    QAQQINILIB("qaqqinilib"),

    /**
     * Specifies the goal the server should use with optimization of queries. This
     * setting corresponds to the server's QAQQINI option called OPTIMIZATION_GOAL.
     */
    QUERY_OPTIMIZE_GOAL("query optimize goal"),

    /**
     * Specifies the mechanism to implement the queryTimeout feature.
     */
    QUERY_TIMEOUT_MECHANISM("query timeout mechanism"),

    /**
     * Limits the storage used by a query. This property compares the storage limit
     * you specify to the estimated storage usage of the query. If the estimated
     * storage usage exceeds the specified storage limit, the query is not allowed
     * to execute.
     */
    QUERY_STORAGE_LIMIT("query storage limit"),

    /**
     * Specifies the buffer size used to receive data through the socket connection
     * between the front-end driver and the system.
     */
    RECEIVE_BUFFER_SIZE("receive buffer size"),

    /**
     * Specifies the buffer size used to send data through the socket connection
     * between the front-end driver and the system.
     */
    SEND_BUFFER_SIZE("send buffer size"),

    /**
     * Specifies whether variable-length fields should be compressed.
     */
    VARIABLE_FIELD_COMPRESSION("variable field compression"),

    /**
     * Specifies how the server sorts records before sending them to the client.
     */
    SORT("sort"),

    /**
     * Specifies a 3-character language id to use for selection of a sort sequence.
     * This property has no effect unless the "sort" property is set to "language".
     */
    SORT_LANGUAGE("sort language"),

    /**
     * Specifies the library and file name of a sort sequence table stored on the
     * server. This property has no effect unless the "sort" property is set to
     * "table".
     */
    SORT_TABLE("sort table"),

    /**
     * Specifies how the server treats case while sorting records. This property has
     * no effect unless the "sort" property is set to "language."
     */
    SORT_WEIGHT("sort weight");

    /**
     * The JDBC option.
     */
    private final String value;

    /**
     * Construct a new Option instance.
     * 
     * @param value The JDBC option.
     */
    Option(String value) {
        this.value = value;
    }

    /**
     * Get the JDBC option.
     * 
     * @return The JDBC option.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the enum JDBC option representation of a string.
     * 
     * @param value The string representation of the option.
     * @return The enum representation of the option.
     */
    public static Option fromValue(String value) {
        for (Option type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
