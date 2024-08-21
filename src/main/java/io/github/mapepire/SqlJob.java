package io.github.mapepire;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapepire.types.ConnectionResult;
import io.github.mapepire.types.DaemonServer;
import io.github.mapepire.types.ExplainResults;
import io.github.mapepire.types.ExplainType;
import io.github.mapepire.types.GetTraceDataResult;
import io.github.mapepire.types.JDBCOptions;
import io.github.mapepire.types.JobLogEntry;
import io.github.mapepire.types.JobStatus;
import io.github.mapepire.types.QueryOptions;
import io.github.mapepire.types.QueryResult;
import io.github.mapepire.types.ServerTraceDest;
import io.github.mapepire.types.ServerTraceLevel;
import io.github.mapepire.types.SetConfigResult;
import io.github.mapepire.types.TransactionEndType;
import io.github.mapepire.types.VersionCheckResult;
import io.github.mapepire.types.jdbcOptions.Option;
import io.github.mapepire.types.jdbcOptions.TransactionIsolation;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a SQL job that manages connections and queries to a database.
 */
public class SqlJob {
    private String transactionCountQuery = String.join("\n", Arrays.asList(
            "select count(*) as thecount",
            "  from qsys2.db_transaction_info",
            "  where JOB_NAME = qsys2.job_name and",
            "    (local_record_changes_pending = 'YES' or local_object_changes_pending = 'YES')"));

    /**
     * A counter to generate unique IDs for each SQLJob instance.
     */
    private static int uniqueIdCounter = 0;

    /**
     * The socket used to communicate with the Mapepire Server component.
     */
    private WebSocketClient socket;

    /**
     * The job status.
     */
    private JobStatus status = JobStatus.NotStarted;

    /**
     * The destination for trace data.
     */
    private String tracedest;

    /**
     * Whether channel data is being traced.
     */
    private boolean isTracingChannelData = false;

    /**
     * The unique job identifier for the connection.
     * TODO: This is not being used.
     */
    private String id;

    /**
     * The JDBC options.
     */
    private JDBCOptions options;

    /**
     * A CompletableFuture to handle asynchronous opening of the socket connection.
     */
    CompletableFuture<String> openedConnectionFuture;

    /**
     * A map to handle asynchronous socket communication when sending and recieving.
     */
    private final Map<String, CompletableFuture<String>> responseMap = new HashMap<>();

    /**
     * TODO: Remove
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * TODO: Currently unused but we will inevitably need a unique ID assigned to
     * each instance since server job names can be reused in some circumstances
     */
    private String uniqueId = SqlJob.getNewUniqueId("sqljob");

    /**
     * Construct a new SqlJob instance.
     */
    public SqlJob() {
        this.options = new JDBCOptions();
    }

    /**
     * Construct a new SqlJob instance.
     * @param options The JDBC options.
     */
    public SqlJob(JDBCOptions options) {
        this.options = options;
    }

    /**
     * Get a new unique ID with "id" as the prefix.
     * @return The unique ID.
     */
    public static String getNewUniqueId() {
        return SqlJob.getNewUniqueId("id");
    }

    /**
     * Get a new unique ID with a custom prefix.
     * @param prefix The custom prefix.
     * @return The unique ID.
     */
    public static String getNewUniqueId(String prefix) {
        return prefix + (++uniqueIdCounter);
    }

    /**
     * Get a WebSocketClient instance which can be used to connect to the specified
     * DB2 server.
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the WebSocketClient instance.
     */
    private CompletableFuture<WebSocketClient> getChannel(DaemonServer db2Server) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { new NoAuthTrustManager() }, new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();

            URI uri = new URI("wss://" + db2Server.getHost() + ":" + db2Server.getPort() + "/db/");
            Map<String, String> httpHeaders = new HashMap<>();
            String auth = db2Server.getUser() + ":" + db2Server.getPassword();
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            httpHeaders.put("Authorization", "Basic " + encodedAuth);

            // TODO: Improve logging ->
            // https://gist.github.com/luketn/4e7595cf39dab63fbcfdb62930fe8f4d
            WebSocketClient wsc = new WebSocketClient(uri, new Draft_6455(), httpHeaders, 5000) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Opened connection");
                    openedConnectionFuture.complete("Opened connection");
                }

                @Override
                public void onMessage(String message) {
                    if (isTracingChannelData) {
                        System.out.println(message);
                    }

                    try {
                        Map<String, Object> response = objectMapper.readValue(message, Map.class);
                        String id = (String) response.get("id");

                        CompletableFuture<String> future = responseMap.get(id);
                        if (future != null) {
                            future.complete(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Closed connection");
                    dispose();
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    dispose();
                }
            };
            wsc.setSocketFactory(factory);

            if (db2Server.getIgnoreUnauthorized()) {
                // TODO:
            }

            if (db2Server.getCa() != null) {
                // TODO:
            }

            return CompletableFuture.completedFuture(wsc);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Send a message to the connected database server.
     * @param content The message content to send.
     * @return A CompletableFuture that resolves to the server's response.
     */
    public CompletableFuture<String> send(String content) {
        if (this.isTracingChannelData) {
            System.out.println(content);
        }

        try {
            Map<String, Object> req = objectMapper.readValue(content, Map.class);
            String id = (String) req.get("id");

            CompletableFuture<String> future = new CompletableFuture<>();
            responseMap.put(id, future);
            this.socket.send(content);
            String message = future.get();
            responseMap.remove(id);
            return CompletableFuture.completedFuture(message);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Get the current status of the job.
     * @return The current status of the job.
     */
    public JobStatus getStatus() {
        return this.getRunningCount() > 0 ? JobStatus.Busy : this.status;
    }

    /**
     * Get the count of ongoing requests for the job.
     * @return The number of ongoing requests.
     */
    public int getRunningCount() {
        // TODO:
        return 0;
    }

    /**
     * Connect to the specified DB2 server and initializes the SQL job.
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the connection result.
     */
    public CompletableFuture<ConnectionResult> connect(DaemonServer db2Server) throws Exception {
        this.status = JobStatus.Connecting;
        try {
            this.socket = this.getChannel(db2Server).get();
            this.socket.connect();
            openedConnectionFuture = new CompletableFuture<>();
            openedConnectionFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String props = String.join(";",
                this.options.getOptions()
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            if (entry.getValue() instanceof String[]) {
                                return entry.getKey() + "=" + String.join(",", (String[]) entry.getValue());
                            } else {
                                return entry.getKey() + "=" + entry.getValue();
                            }
                        })
                        .collect(Collectors.toList()));

        ObjectNode connectionObject = objectMapper.createObjectNode();
        connectionObject.put("id", SqlJob.getNewUniqueId());
        connectionObject.put("type", "connect");
        connectionObject.put("technique", "tcp");
        connectionObject.put("application", "Java client");
        if (props.length() > 0) {
            connectionObject.put("props", props);
        }

        String result;
        try {
            result = this.send(objectMapper.writeValueAsString(connectionObject)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        ConnectionResult connectResult;
        try {
            connectResult = objectMapper.readValue(result, ConnectionResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (connectResult.getSuccess()) {
            this.status = JobStatus.Ready;
        } else {
            this.dispose();
            this.status = JobStatus.NotStarted;
            if (connectResult.getError() != null) {
                throw new Exception(connectResult.getError());
            } else {
                throw new Exception("Failed to connect to server.");
            }
        }

        this.id = connectResult.getJob();
        this.isTracingChannelData = false;

        return CompletableFuture.completedFuture(connectResult);
    }

    /**
     * Create a Query object for the specified SQL statement.
     * @param <T> The type of data to be returned.
     * @param sql The SQL query.
     * @return A new Query instance.
     */
    public <T> Query<T> query(String sql) {
        return this.query(sql, new QueryOptions());
    }

    /**
     * Create a Query object for the specified SQL statement.
     * @param <T>  The type of data to be returned.
     * @param sql  The SQL query.
     * @param opts The options for configuring the query.
     * @return A new Query instance.
     */
    public <T> Query<T> query(String sql, QueryOptions opts) {
        return new Query(this, sql, opts);
    }

    /**
     * Execute an SQL command and returns the result.
     * @param <T> The type of data to be returned.
     * @param sql The SQL command to execute.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql) throws Exception {
        return this.execute(sql, new QueryOptions());
    }

    /**
     * Execute an SQL command and returns the result.
     * @param <T> The type of data to be returned.
     * @param sql  The SQL command to execute.
     * @param opts The options for configuring the query.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) throws Exception {
        Query<T> query = query(sql, opts);
        CompletableFuture<QueryResult<T>> future = query.execute();
        try {
            QueryResult<T> result = future.get();
            query.close().get();

            if (result.getError() != null) {
                throw new Exception(result.getError());
            }

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    /**
     * Get the version information from the database server.
     * @return A CompletableFuture that resolves to the version check result.
     */
    public CompletableFuture<VersionCheckResult> getVersion() throws Exception {
        ObjectNode verObj = objectMapper.createObjectNode();
        verObj.put("id", SqlJob.getNewUniqueId());
        verObj.put("type", "getversion");

        String result;
        try {
            result = this.send(objectMapper.writeValueAsString(verObj)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        VersionCheckResult version;
        try {
            version = objectMapper.readValue(result, VersionCheckResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!version.getSuccess()) {
            if (version.getError() != null) {
                throw new Exception(version.getError());
            } else {
                throw new Exception("Failed to get version from backend");
            }
        }

        return CompletableFuture.completedFuture(version);
    }

    /**
     * Explains a SQL statement and returns the results.
     * @param statement The SQL statement to explain.
     * @return A CompletableFuture that resolves to the explain results.
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement) throws Exception {
        return this.explain(statement, ExplainType.Run);
    }

    /**
     * Explains a SQL statement and returns the results.
     * @param statement The SQL statement to explain.
     * @param type      The type of explain to perform (default is ExplainType.Run).
     * @return A CompletableFuture that resolves to the explain results.
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement, ExplainType type) throws Exception {
        ObjectNode explainRequest = objectMapper.createObjectNode();
        explainRequest.put("id", SqlJob.getNewUniqueId());
        explainRequest.put("type", "dove");
        explainRequest.put("sql", statement);
        explainRequest.put("run", type == ExplainType.Run);

        String result;
        try {
            result = this.send(objectMapper.writeValueAsString(explainRequest)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        ExplainResults<?> explainResult;
        try {
            explainResult = objectMapper.readValue(result, ExplainResults.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!explainResult.getSuccess()) {
            if (explainResult.getError() != null) {
                throw new Exception(explainResult.getError());
            } else {
                throw new Exception("Failed to explain.");
            }
        }

        return CompletableFuture.completedFuture(explainResult);
    }

    /**
     * Get the file path of the trace file, if available.
     */
    public String getTraceFilePath() {
        return this.tracedest;
    }

    /**
     * Get trace data from the backend.
     * @return A CompletableFuture that resolves to the trace data result.
     */
    public CompletableFuture<GetTraceDataResult> getTraceData() throws Exception {
        ObjectNode tracedataReqObj = objectMapper.createObjectNode();
        tracedataReqObj.put("id", SqlJob.getNewUniqueId());
        tracedataReqObj.put("type", "gettracedata");

        String result;
        try {
            result = this.send(objectMapper.writeValueAsString(tracedataReqObj)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        GetTraceDataResult rpy;
        try {
            rpy = objectMapper.readValue(result, GetTraceDataResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!rpy.getSuccess()) {
            if (rpy.getError() != null) {
                throw new Exception(rpy.getError());
            } else {
                throw new Exception("Failed to get trace data from backend");
            }
        }

        return CompletableFuture.completedFuture(rpy);
    }

    /**
     * Set the trace config on the backend.
     * @param dest  The server trace destination.
     * @param level The server trace level.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setTraceConfig(ServerTraceDest dest, ServerTraceLevel level)
            throws Exception {
        ObjectNode reqObj = objectMapper.createObjectNode();
        reqObj.put("id", SqlJob.getNewUniqueId());
        reqObj.put("type", "setconfig");
        reqObj.put("tracedest", dest.getValue());
        reqObj.put("tracelevel", level.getValue());

        this.isTracingChannelData = true;

        String result;
        try {
            result = this.send(objectMapper.writeValueAsString(reqObj)).get();
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        SetConfigResult rpy;
        try {
            rpy = objectMapper.readValue(result, SetConfigResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!rpy.getSuccess()) {
            if (rpy.getError() != null) {
                throw new Exception(rpy.getError());
            } else {
                throw new Exception("Failed to set trace options on backend");
            }
        }

        this.tracedest = (rpy.getTracedest().getValue() != null && rpy.getTracedest().getValue().charAt(0) == '/'
                ? rpy.getTracedest().getValue()
                : null);
        return CompletableFuture.completedFuture(rpy);
    }

    /**
     * Create a CL command query.
     * @param cmd The CL command.
     * @return A new Query instance for the command.
     */
    public Query<?> clcommand(String cmd) {
        QueryOptions options = new QueryOptions();
        options.setIsClCommand(true);
        return new Query(this, cmd, options);
    }

    /**
     * Check if the job is under commitment control based on the transaction
     * isolation level.
     * @return Whether the job is under commitment control.
     */
    public boolean underCommitControl() {
        return this.options.getOption(Option.TRANSACTION_ISOLATION) != null
                && this.options.getOption(Option.TRANSACTION_ISOLATION) != TransactionIsolation.NONE.getValue();
    }

    /**
     * Get the count of pending transactions.
     * @return A CompletableFuture that resolves to the count of pending
     *         transactions.
     */
    public CompletableFuture<Integer> getPendingTransactions() {
        // TODO: Fix implementation
        // return CompletableFuture.supplyAsync(() -> {
        // QueryResult rows;
        // try {
        // rows = this.query(this.transactionCountQuery).execute(1).get();

        // if (rows.isSuccess() && rows.getData() != null && rows.getData().size() == 1)
        // {
        // Object row = rows.getData().get(0);
        // }

        // return 0;
        // } catch (Exception e) {
        // e.printStackTrace();
        // return 0;
        // }
        // });
        return CompletableFuture.completedFuture(0);
    }

    /**
     * Ends the current transaction by committing or rolling back.
     * @param type The type of transaction ending (commit or rollback).
     * @return A CompletableFuture that resolves to the result of the transaction
     *         operation.
     */
    public CompletableFuture<QueryResult<JobLogEntry>> endTransaction(TransactionEndType type) throws Exception {
        String query;

        switch (type) {
            case COMMIT:
                query = "COMMIT";
                break;
            case ROLLBACK:
                query = "ROLLBACK";
                break;
            default:
                throw new IllegalArgumentException("TransactionEndType " + type + " not valid");
        }

        return this.query(query).execute();
    }

    /**
     * Get the unique ID assigned to this SqlJob instance.
     * TODO: Currently unused but we will inevitably need a unique ID assigned to
     * each instance since server job names can be reused in some circumstances
     * @return The unique ID assigned to this SqlJob instance
     */
    public String getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Close the socket.
     */
    public void close() {
        this.dispose();
    }

    /**
     * Close the socket and set the status to be ended.
     */
    public void dispose() {
        if (this.socket != null) {
            this.socket.close();
        }
        this.status = JobStatus.Ended;
    }
}

/**
 * Represents a manager that handles which X509 certificates may be used to
 * authenticate the remote side of a secure socket.
 */
class NoAuthTrustManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType)
            throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}