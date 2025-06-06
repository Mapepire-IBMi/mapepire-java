package io.github.mapepire_ibmi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapepire_ibmi.types.ConnectionResult;
import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.ExplainResults;
import io.github.mapepire_ibmi.types.ExplainType;
import io.github.mapepire_ibmi.types.GetTraceDataResult;
import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.JobLogEntry;
import io.github.mapepire_ibmi.types.JobStatus;
import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.ServerTraceDest;
import io.github.mapepire_ibmi.types.ServerTraceLevel;
import io.github.mapepire_ibmi.types.SetConfigResult;
import io.github.mapepire_ibmi.types.TransactionEndType;
import io.github.mapepire_ibmi.types.VersionCheckResult;
import io.github.mapepire_ibmi.types.exceptions.UnknownServerException;
import io.github.mapepire_ibmi.types.jdbcOptions.Option;
import io.github.mapepire_ibmi.types.jdbcOptions.TransactionIsolation;

/**
 * Represents a SQL job that manages connections and queries to a database.
 */
public class SqlJob {
    /**
     * A counter to generate unique IDs for each SQLJob instance.
     */
    private static int uniqueIdCounter;

    /**
     * The socket used to communicate with the Mapepire Server component.
     */
    private WebSocketClient socket;

    /**
     * The job status.
     */
    private JobStatus status = JobStatus.NotStarted;

    /**
     * The server trace data destination.
     */
    private String traceDest;

    /**
     * Whether channel data is being traced.
     */
    private boolean isTracingChannelData;

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
    private CompletableFuture<String> openedConnectionFuture;

    /**
     * A map to handle asynchronous socket communication when sending and recieving.
     */
    private final Map<String, CompletableFuture<String>> responseMap = new HashMap<>();

    /**
     * TODO: Currently unused but we will inevitably need a unique ID assigned to
     * each instance since server job names can be reused in some circumstances.
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
     *
     * @param options The JDBC options.
     */
    public SqlJob(JDBCOptions options) {
        this.options = options;
    }

    /**
     * Get a new unique ID with "id" as the prefix.
     *
     * @return The unique ID.
     */
    public static String getNewUniqueId() {
        return SqlJob.getNewUniqueId("id");
    }

    /**
     * Get a new unique ID with a custom prefix.
     *
     * @param prefix The custom prefix.
     * @return The unique ID.
     */
    public static synchronized String getNewUniqueId(String prefix) {
        return prefix + (++uniqueIdCounter);
    }

    /**
     * Get a WebSocketClient instance which can be used to connect to the specified
     * DB2 server.
     *
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the WebSocketClient instance.
     */
    private CompletableFuture<WebSocketClient> getChannel(DaemonServer db2Server) throws Exception {
        TrustManagerFactory tmf;
        X509TrustManager customTrustManager = null;
        X509TrustManager jdkTrustManager = null;

        if (db2Server.getCa() != null) {
            // Convert custom CA from string to X509Certificate
            InputStream inputStream = new ByteArrayInputStream(db2Server.getCa().getBytes());
            X509Certificate caCert = (X509Certificate) CertificateFactory.getInstance("X509")
                    .generateCertificate(inputStream);

            // Create a custom key store and load custom certificate
            KeyStore customKeyStore = KeyStore.getInstance("PKCS12");
            customKeyStore.load(null, null);
            customKeyStore.setCertificateEntry("mapepire-ca", caCert);

            // Initialize a TrustManagerFactory with custom key store
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(customKeyStore);

            // Get the custom X509TrustManager
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    customTrustManager = (X509TrustManager) tm;
                    break;
                }
            }
        }

        // Initialize TrustManagerFactory
        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init((KeyStore) null);

        // Get the JDK X509TrustManager
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                jdkTrustManager = (X509TrustManager) tm;
                break;
            }
        }

        final X509TrustManager finalCustomTrustManager = customTrustManager;
        final X509TrustManager finalJdkTrustManager = jdkTrustManager;
        X509TrustManager mapepireTrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                if (finalCustomTrustManager != null) {
                    X509Certificate[] jdkTrust = finalJdkTrustManager.getAcceptedIssuers();
                    X509Certificate[] custTrust = finalCustomTrustManager.getAcceptedIssuers();
                    X509Certificate[] merge = new X509Certificate[custTrust.length + jdkTrust.length];

                    for (int i = 0; i < custTrust.length; i++) {
                        merge[i] = custTrust[i];
                    }

                    for (int i = 0; i < jdkTrust.length; i++) {
                        merge[custTrust.length + i] = jdkTrust[i];
                    }

                    return merge;
                } else {
                    return finalJdkTrustManager.getAcceptedIssuers();
                }
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                if (db2Server.getRejectUnauthorized()) {
                    if (finalCustomTrustManager != null) {
                        try {
                            finalCustomTrustManager.checkServerTrusted(chain, authType);
                        } catch (CertificateException e) {
                            finalJdkTrustManager.checkServerTrusted(chain, authType);
                        }
                    } else {
                        finalJdkTrustManager.checkServerTrusted(chain, authType);
                    }
                }
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { mapepireTrustManager }, new SecureRandom());
        SSLContext.setDefault(sslContext);
        URI uri = new URI("wss://" + db2Server.getHost() + ":" + db2Server.getPort() + "/db/");
        Map<String, String> httpHeaders = new HashMap<>();
        String auth = db2Server.getUser() + ":" + db2Server.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        httpHeaders.put("Authorization", "Basic " + encodedAuth);

        WebSocketClient wsc = new WebSocketClient(uri, new Draft_6455(), httpHeaders, 5000) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if (isTracingChannelData) {
                    System.out.println("Opened connection");
                }
                openedConnectionFuture.complete("Opened connection");
            }

            @Override
            public void onMessage(ByteBuffer byteBuffer) {
                String message = StandardCharsets.UTF_8.decode(byteBuffer).toString();
                this.onMessage(message);
            }

            @Override
            public void onMessage(String message) {
                if (isTracingChannelData) {
                    System.out.println("\n<< " + message);
                }

                try {
                    ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
                    Map<String, Object> response = objectMapper.readValue(message, Map.class);
                    String id = (String) response.get("id");

                    CompletableFuture<String> future = responseMap.get(id);
                    if (future != null) {
                        Thread thread = new Thread(() -> {
                            future.complete(message);
                        });
                        thread.start();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (isTracingChannelData) {
                    System.out.println("Closed connection");
                }
                dispose();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                dispose();

                if (openedConnectionFuture != null) {
                    openedConnectionFuture.completeExceptionally(e);
                }
            }
        };
        SSLSocketFactory factory = sslContext.getSocketFactory();
        wsc.setSocketFactory(factory);

        return CompletableFuture.completedFuture(wsc);
    }

    /**
     * Send a message to the connected database server.
     *
     * @param content The message content to send.
     * @return A CompletableFuture that resolves to the server's response.
     */
    public CompletableFuture<String> send(String content) throws Exception {
        if (this.isTracingChannelData) {
            System.out.println("\n>> " + content);
        }

        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        Map<String, Object> req = objectMapper.readValue(content, Map.class);
        String id = (String) req.get("id");

        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(id, future);

        this.socket.send(content + "\n");
        this.status = JobStatus.Busy;

        return future.whenComplete((message, throwable) -> {
            responseMap.remove(id);
            this.status = this.getRunningCount() == 0 ? JobStatus.Ready : JobStatus.Busy;
        });
    }

    /**
     * Get the current status of the job.
     *
     * @return The current status of the job.
     */
    public JobStatus getStatus() {
        return this.status;
    }

    /**
     * Get the count of ongoing requests for the job.
     *
     * @return The number of ongoing requests.
     */
    public int getRunningCount() {
        return this.responseMap.size();
    }

    /**
     * Connect to the specified DB2 server and initializes the SQL job.
     *
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the connection result.
     */
    public CompletableFuture<ConnectionResult> connect(DaemonServer db2Server) throws Exception {
        this.status = JobStatus.Connecting;
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();

        return this.getChannel(db2Server)
                .thenCompose(socket -> {
                    this.socket = socket;
                    openedConnectionFuture = new CompletableFuture<>();
                    this.socket.connect();
                    return openedConnectionFuture;
                })
                .thenCompose(v -> {
                    openedConnectionFuture = null;

                    String props = String.join(";",
                            this.options.getOptions()
                                    .entrySet()
                                    .stream()
                                    .map(entry -> {
                                        if (entry.getValue() instanceof List) {
                                            return entry.getKey() + "=" + String.join(",", (List) entry.getValue());
                                        } else {
                                            return entry.getKey() + "=" + entry.getValue();
                                        }
                                    })
                                    .collect(Collectors.toList()));

                    ObjectNode connectRequest = objectMapper.createObjectNode();
                    connectRequest.put("id", SqlJob.getNewUniqueId());
                    connectRequest.put("type", "connect");
                    connectRequest.put("technique", "tcp");
                    connectRequest.put("application", "Java client");
                    if (props.length() > 0) {
                        connectRequest.put("props", props);
                    }

                    try {
                        return this.send(objectMapper.writeValueAsString(connectRequest));
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                })
                .thenApply(result -> {
                    ConnectionResult connectResult;
                    try {
                        connectResult = objectMapper.readValue(result, ConnectionResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    if (connectResult.getSuccess()) {
                        this.status = JobStatus.Ready;
                    } else {
                        this.dispose();
                        this.status = JobStatus.NotStarted;

                        String error = connectResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, connectResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to connect to server"));
                        }
                    }

                    this.id = connectResult.getJob();
                    this.isTracingChannelData = false;

                    return connectResult;
                });
    }

    /**
     * Create a Query object for the specified SQL statement.
     *
     * @param sql The SQL query.
     * @return A new Query instance.
     */
    public Query query(String sql) {
        return this.query(sql, new QueryOptions());
    }

    /**
     * Create a Query object for the specified SQL statement.
     *
     * @param sql  The SQL query.
     * @param opts The options for configuring the query.
     * @return A new Query instance.
     */
    public Query query(String sql, QueryOptions opts) {
        return new Query(this, sql, opts);
    }

    /**
     * Execute an SQL command and returns the result.
     *
     * @param <T> The type of data to be returned.
     * @param sql The SQL command to execute.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql) throws Exception {
        return this.execute(sql, new QueryOptions());
    }

    /**
     * Execute an SQL command and returns the result.
     *
     * @param <T>  The type of data to be returned.
     * @param sql  The SQL command to execute.
     * @param opts The options for configuring the query.
     * @return A CompletableFuture that resolves to the query result.
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) throws Exception {
        Query query = query(sql, opts);
        return query.<T>execute()
                .thenCompose(queryResult -> {
                    try {
                        return query.close().thenApply(v -> queryResult);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }).thenApply(queryResult -> {
                    if (!queryResult.getSuccess()) {
                        String error = queryResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, queryResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to execute"));
                        }
                    }

                    return queryResult;
                });
    }

    /**
     * Get the version information from the database server.
     *
     * @return A CompletableFuture that resolves to the version check result.
     */
    public CompletableFuture<VersionCheckResult> getVersion() throws Exception {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode versionRequest = objectMapper.createObjectNode();
        versionRequest.put("id", SqlJob.getNewUniqueId());
        versionRequest.put("type", "getversion");

        return this.send(objectMapper.writeValueAsString(versionRequest))
                .thenApply(result -> {
                    VersionCheckResult versionCheckResult;
                    try {
                        versionCheckResult = objectMapper.readValue(result, VersionCheckResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    if (!versionCheckResult.getSuccess()) {
                        String error = versionCheckResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, versionCheckResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to get version"));
                        }
                    }

                    return versionCheckResult;
                });
    }

    /**
     * Explains a SQL statement and returns the results.
     *
     * @param statement The SQL statement to explain.
     * @return A CompletableFuture that resolves to the explain results.
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement) throws Exception {
        return this.explain(statement, ExplainType.RUN);
    }

    /**
     * Explains a SQL statement and returns the results.
     *
     * @param statement The SQL statement to explain.
     * @param type      The type of explain to perform (default is ExplainType.Run).
     * @return A CompletableFuture that resolves to the explain results.
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement, ExplainType type) throws Exception {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode explainRequest = objectMapper.createObjectNode();
        explainRequest.put("id", SqlJob.getNewUniqueId());
        explainRequest.put("type", "dove");
        explainRequest.put("sql", statement);
        explainRequest.put("run", type == ExplainType.RUN);

        return this.send(objectMapper.writeValueAsString(explainRequest))
                .thenApply(result -> {
                    ExplainResults<?> explainResult;
                    try {
                        explainResult = objectMapper.readValue(result, ExplainResults.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    if (!explainResult.getSuccess()) {
                        String error = explainResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, explainResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to explain"));
                        }
                    }

                    return explainResult;
                });
    }

    /**
     * Get the file path of the trace file, if available.
     * 
     * @return The server trace data destination.
     */
    public String getTraceFilePath() {
        return this.traceDest;
    }

    /**
     * Get trace data from the backend.
     *
     * @return A CompletableFuture that resolves to the trace data result.
     */
    public CompletableFuture<GetTraceDataResult> getTraceData() throws Exception {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode traceDataRequest = objectMapper.createObjectNode();
        traceDataRequest.put("id", SqlJob.getNewUniqueId());
        traceDataRequest.put("type", "gettracedata");

        return this.send(objectMapper.writeValueAsString(traceDataRequest))
                .thenApply(result -> {
                    GetTraceDataResult traceDataResult;
                    try {
                        traceDataResult = objectMapper.readValue(result, GetTraceDataResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    if (!traceDataResult.getSuccess()) {
                        String error = traceDataResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, traceDataResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to get trace data"));
                        }
                    }

                    return traceDataResult;
                });
    }

    /**
     * Set the server trace destination.
     * 
     * @param dest The server trace destination.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setTraceDest(ServerTraceDest dest) throws Exception {
        return setTraceConfig(dest, null, null, null);
    }

    /**
     * Set the server trace level.
     * 
     * @param level The server trace level.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setTraceLevel(ServerTraceLevel level) throws Exception {
        return setTraceConfig(null, level, null, null);
    }

    /**
     * Set the JTOpen trace data destination.
     * 
     * @param jtOpenTraceDest The JTOpen trace data destination.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setJtOpenTraceDest(ServerTraceDest jtOpenTraceDest) throws Exception {
        return setTraceConfig(null, null, jtOpenTraceDest, null);
    }

    /**
     * Set the JTOpen trace level.
     * 
     * @param jtOpenTraceLevel The JTOpen trace level.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setJtOpenTraceLevel(ServerTraceLevel jtOpenTraceLevel) throws Exception {
        return setTraceConfig(null, null, null, jtOpenTraceLevel);
    }

    /**
     * Set the trace config on the backend.
     *
     * @param dest             The server trace destination.
     * @param level            The server trace level.
     * @param jtOpenTraceDest  The JTOpen trace data destination.
     * @param jtOpenTraceLevel The JTOpen trace level.
     * @return A CompletableFuture that resolves to the set config result.
     */
    public CompletableFuture<SetConfigResult> setTraceConfig(ServerTraceDest dest, ServerTraceLevel level,
            ServerTraceDest jtOpenTraceDest, ServerTraceLevel jtOpenTraceLevel) throws Exception {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode setTraceConfigRequest = objectMapper.createObjectNode();
        setTraceConfigRequest.put("id", SqlJob.getNewUniqueId());
        setTraceConfigRequest.put("type", "setconfig");

        if (dest != null) {
            setTraceConfigRequest.put("tracedest", dest.getValue());
        }
        if (level != null) {
            setTraceConfigRequest.put("tracelevel", level.getValue());
        }
        if (jtOpenTraceDest != null) {
            setTraceConfigRequest.put("jtopentracedest", jtOpenTraceDest.getValue());
        }
        if (jtOpenTraceLevel != null) {
            setTraceConfigRequest.put("jtopentracelevel", jtOpenTraceLevel.getValue());
        }

        this.isTracingChannelData = true;

        return this.send(objectMapper.writeValueAsString(setTraceConfigRequest))
                .thenApply(result -> {
                    SetConfigResult setConfigResult;
                    try {
                        setConfigResult = objectMapper.readValue(result, SetConfigResult.class);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    if (!setConfigResult.getSuccess()) {
                        String error = setConfigResult.getError();
                        if (error != null) {
                            throw new CompletionException(new SQLException(error, setConfigResult.getSqlState()));
                        } else {
                            throw new CompletionException(new UnknownServerException("Failed to set trace config"));
                        }
                    }

                    this.traceDest = setConfigResult.getTraceDest() != null
                            && setConfigResult.getTraceDest().charAt(0) == '/'
                                    ? setConfigResult.getTraceDest()
                                    : null;
                    return setConfigResult;
                });
    }

    /**
     * Create a CL command query.
     *
     * @param cmd The CL command.
     * @return A new Query instance for the command.
     */
    public Query clCommand(String cmd) {
        QueryOptions options = new QueryOptions();
        options.setIsClCommand(true);
        return new Query(this, cmd, options);
    }

    /**
     * Check if the job is under commitment control based on the transaction
     * isolation level.
     *
     * @return Whether the job is under commitment control.
     */
    public boolean underCommitControl() {
        return this.options.getOption(Option.TRANSACTION_ISOLATION) != null
                && this.options.getOption(Option.TRANSACTION_ISOLATION) != TransactionIsolation.NONE.getValue();
    }

    /**
     * Get the count of pending transactions.
     *
     * @return A CompletableFuture that resolves to the count of pending
     *         transactions.
     */
    public CompletableFuture<Integer> getPendingTransactions() throws Exception {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        String transactionCountQuery = String.join("\n", Arrays.asList(
                "select count(*) as thecount",
                "  from qsys2.db_transaction_info",
                "  where JOB_NAME = qsys2.job_name and",
                "    (local_record_changes_pending = 'YES' or local_object_changes_pending = 'YES')"));

        return this.query(transactionCountQuery).execute(1)
                .thenApply(queryResult -> {
                    if (queryResult.getSuccess() && queryResult.getData() != null
                            && queryResult.getData().size() == 1) {
                        String data;
                        try {
                            data = objectMapper.writeValueAsString(queryResult.getData().get(0));
                            Map<String, Object> req = objectMapper.readValue(data, Map.class);
                            Integer count = (Integer) req.get("THECOUNT");
                            return count;
                        } catch (Exception e) {
                            throw new CompletionException(e);
                        }
                    }

                    return 0;
                });
    }

    /**
     * Ends the current transaction by committing or rolling back.
     *
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
     *
     * @return The unique ID assigned to this SqlJob instance
     */
    public String getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Enable local tracing of channel data.
     */
    public void enableLocalTrace() {
        this.isTracingChannelData = true;
    }

    /**
     * Close the job.
     */
    public void close() {
        this.dispose();
    }

    /**
     * Close the socket and set the status to be ended.
     */
    private void dispose() {
        if (this.socket != null) {
            this.socket.close();
        }
        this.status = JobStatus.Ended;
    }
}
