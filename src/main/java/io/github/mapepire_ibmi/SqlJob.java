package io.github.mapepire_ibmi;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import io.github.mapepire_ibmi.types.exceptions.ClientException;
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
     * The destination for trace data.
     */
    private String tracedest;

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
    public static String getNewUniqueId(String prefix) {
        return prefix + (++uniqueIdCounter);
    }

    /**
     * Get a WebSocketClient instance which can be used to connect to the specified
     * DB2 server.
     *
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the WebSocketClient instance.
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws URISyntaxException
     */
    private CompletableFuture<WebSocketClient> getChannel(DaemonServer db2Server)
            throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        NoAuthTrustManager trustManager = new NoAuthTrustManager();
        sslContext.init(null, new TrustManager[] { trustManager }, new SecureRandom());
        SSLSocketFactory factory = sslContext.getSocketFactory();

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
                        future.complete(message);
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
        wsc.setSocketFactory(factory);

        // TODO: Implement
        // if (db2Server.getIgnoreUnauthorized()) {
        // }
        // if (db2Server.getCa() != null) {
        // }

        return CompletableFuture.completedFuture(wsc);
    }

    /**
     * Send a message to the connected database server.
     *
     * @param content The message content to send.
     * @return A CompletableFuture that resolves to the server's response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletableFuture<String> send(String content)
            throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
        if (this.isTracingChannelData) {
            System.out.println("\n>> " + content);
        }

        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        Map<String, Object> req = objectMapper.readValue(content, Map.class);
        String id = (String) req.get("id");

        CompletableFuture<String> future = new CompletableFuture<>();
        responseMap.put(id, future);
        synchronized (this.socket) {
            this.socket.send(content + "\n");
        }
        String message = future.get();
        responseMap.remove(id);
        return CompletableFuture.completedFuture(message);
    }

    /**
     * Get the current status of the job.
     *
     * @return The current status of the job.
     */
    public JobStatus getStatus() {
        return this.getRunningCount() > 0 ? JobStatus.Busy : this.status;
    }

    /**
     * Get the count of ongoing requests for the job.
     *
     * @return The number of ongoing requests.
     */
    public int getRunningCount() {
        // TODO:
        return 0;
    }

    /**
     * Connect to the specified DB2 server and initializes the SQL job.
     *
     * @param db2Server The server details for the connection.
     * @return A CompletableFuture that resolves to the connection result.
     * @throws URISyntaxException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public CompletableFuture<ConnectionResult> connect(DaemonServer db2Server) throws KeyManagementException,
            NoSuchAlgorithmException, InterruptedException, ExecutionException, URISyntaxException,
            JsonMappingException, JsonProcessingException, SQLException, UnknownServerException {
        this.status = JobStatus.Connecting;

        this.socket = this.getChannel(db2Server).get();
        openedConnectionFuture = new CompletableFuture<>();
        this.socket.connect();
        openedConnectionFuture.get();
        openedConnectionFuture = null;

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

        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode connectionObject = objectMapper.createObjectNode();
        connectionObject.put("id", SqlJob.getNewUniqueId());
        connectionObject.put("type", "connect");
        connectionObject.put("technique", "tcp");
        connectionObject.put("application", "Java client");
        if (props.length() > 0) {
            connectionObject.put("props", props);
        }

        String result = this.send(objectMapper.writeValueAsString(connectionObject)).get();
        ConnectionResult connectResult = objectMapper.readValue(result, ConnectionResult.class);

        if (connectResult.getSuccess()) {
            this.status = JobStatus.Ready;
        } else {
            this.dispose();
            this.status = JobStatus.NotStarted;

            String error = connectResult.getError();
            if (error != null) {
                throw new SQLException(error, connectResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to connect to server");
            }
        }

        this.id = connectResult.getJob();
        this.isTracingChannelData = false;

        return CompletableFuture.completedFuture(connectResult);
    }

    /**
     * Create a Query object for the specified SQL statement.
     *
     * @param <T> The type of data to be returned.
     * @param sql The SQL query.
     * @return A new Query instance.
     */
    public <T> Query<T> query(String sql) {
        return this.query(sql, new QueryOptions());
    }

    /**
     * Create a Query object for the specified SQL statement.
     *
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
     *
     * @param <T> The type of data to be returned.
     * @param sql The SQL command to execute.
     * @return A CompletableFuture that resolves to the query result.
     * @throws UnknownServerException
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ClientException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql)
            throws JsonMappingException, JsonProcessingException, ClientException, InterruptedException,
            ExecutionException, SQLException, UnknownServerException {
        return this.execute(sql, new QueryOptions());
    }

    /**
     * Execute an SQL command and returns the result.
     *
     * @param <T>  The type of data to be returned.
     * @param sql  The SQL command to execute.
     * @param opts The options for configuring the query.
     * @return A CompletableFuture that resolves to the query result.
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ClientException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws UnknownServerException
     */
    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts)
            throws JsonMappingException, JsonProcessingException, ClientException, InterruptedException,
            ExecutionException, SQLException, UnknownServerException {
        Query<T> query = query(sql, opts);
        CompletableFuture<QueryResult<T>> future = query.execute();
        QueryResult<T> queryResult = future.get();
        query.close().get();

        if (!queryResult.getSuccess()) {
            String error = queryResult.getError();
            if (error != null) {
                throw new SQLException(error, queryResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to execute");
            }
        }

        return CompletableFuture.completedFuture(queryResult);
    }

    /**
     * Get the version information from the database server.
     *
     * @return A CompletableFuture that resolves to the version check result.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public CompletableFuture<VersionCheckResult> getVersion() throws JsonMappingException, JsonProcessingException,
            InterruptedException, ExecutionException, SQLException, UnknownServerException {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode verObj = objectMapper.createObjectNode();
        verObj.put("id", SqlJob.getNewUniqueId());
        verObj.put("type", "getversion");

        String result = this.send(objectMapper.writeValueAsString(verObj)).get();
        VersionCheckResult versionCheckResult = objectMapper.readValue(result, VersionCheckResult.class);

        if (!versionCheckResult.getSuccess()) {
            String error = versionCheckResult.getError();
            if (error != null) {
                throw new SQLException(error, versionCheckResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to get version");
            }
        }

        return CompletableFuture.completedFuture(versionCheckResult);
    }

    /**
     * Explains a SQL statement and returns the results.
     *
     * @param statement The SQL statement to explain.
     * @return A CompletableFuture that resolves to the explain results.
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws UnknownServerException
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement) throws JsonMappingException,
            JsonProcessingException, InterruptedException, ExecutionException, SQLException, UnknownServerException {
        return this.explain(statement, ExplainType.Run);
    }

    /**
     * Explains a SQL statement and returns the results.
     *
     * @param statement The SQL statement to explain.
     * @param type      The type of explain to perform (default is ExplainType.Run).
     * @return A CompletableFuture that resolves to the explain results.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public CompletableFuture<ExplainResults<?>> explain(String statement, ExplainType type) throws JsonMappingException,
            JsonProcessingException, InterruptedException, ExecutionException, SQLException, UnknownServerException {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode explainRequest = objectMapper.createObjectNode();
        explainRequest.put("id", SqlJob.getNewUniqueId());
        explainRequest.put("type", "dove");
        explainRequest.put("sql", statement);
        explainRequest.put("run", type == ExplainType.Run);

        String result = this.send(objectMapper.writeValueAsString(explainRequest)).get();
        ExplainResults<?> explainResult = objectMapper.readValue(result, ExplainResults.class);

        if (!explainResult.getSuccess()) {
            String error = explainResult.getError();
            if (error != null) {
                throw new SQLException(error, explainResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to explain");
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
     *
     * @return A CompletableFuture that resolves to the trace data result.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public CompletableFuture<GetTraceDataResult> getTraceData() throws JsonMappingException, JsonProcessingException,
            InterruptedException, ExecutionException, SQLException, UnknownServerException {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode tracedataReqObj = objectMapper.createObjectNode();
        tracedataReqObj.put("id", SqlJob.getNewUniqueId());
        tracedataReqObj.put("type", "gettracedata");

        String result = this.send(objectMapper.writeValueAsString(tracedataReqObj)).get();
        GetTraceDataResult traceDataResult = objectMapper.readValue(result, GetTraceDataResult.class);

        if (!traceDataResult.getSuccess()) {
            String error = traceDataResult.getError();
            if (error != null) {
                throw new SQLException(error, traceDataResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to get trace data");
            }
        }

        return CompletableFuture.completedFuture(traceDataResult);
    }

    /**
     * Set the trace config on the backend.
     *
     * @param dest  The server trace destination.
     * @param level The server trace level.
     * @return A CompletableFuture that resolves to the set config result.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws SQLException
     * @throws UnknownServerException
     */
    public CompletableFuture<SetConfigResult> setTraceConfig(ServerTraceDest dest, ServerTraceLevel level)
            throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException,
            SQLException, UnknownServerException {
        ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
        ObjectNode reqObj = objectMapper.createObjectNode();
        reqObj.put("id", SqlJob.getNewUniqueId());
        reqObj.put("type", "setconfig");
        reqObj.put("tracedest", dest.getValue());
        reqObj.put("tracelevel", level.getValue());

        this.isTracingChannelData = true;

        String result = this.send(objectMapper.writeValueAsString(reqObj)).get();
        SetConfigResult setConfigResult = objectMapper.readValue(result, SetConfigResult.class);

        if (!setConfigResult.getSuccess()) {
            String error = setConfigResult.getError();
            if (error != null) {
                throw new SQLException(error, setConfigResult.getSqlState());
            } else {
                throw new UnknownServerException("Failed to set trace config");
            }
        }

        this.tracedest = setConfigResult.getTracedest().getValue() != null
                && setConfigResult.getTracedest().getValue().charAt(0) == '/'
                        ? setConfigResult.getTracedest().getValue()
                        : null;
        return CompletableFuture.completedFuture(setConfigResult);
    }

    /**
     * Create a CL command query.
     *
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
     * @throws SQLException
     * @throws ClientException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public CompletableFuture<Integer> getPendingTransactions() throws JsonMappingException, JsonProcessingException,
            InterruptedException, ExecutionException, ClientException, SQLException {
        String transactionCountQuery = String.join("\n", Arrays.asList(
                "select count(*) as thecount",
                "  from qsys2.db_transaction_info",
                "  where JOB_NAME = qsys2.job_name and",
                "    (local_record_changes_pending = 'YES' or local_object_changes_pending = 'YES')"));

        QueryResult<Object> queryResult = this.query(transactionCountQuery).execute(1).get();

        if (queryResult.getSuccess() && queryResult.getData() != null && queryResult.getData().size() == 1) {
            ObjectMapper objectMapper = SingletonObjectMapper.getInstance();
            String data = objectMapper.writeValueAsString(queryResult.getData().get(0));
            Map<String, Object> req = objectMapper.readValue(data, Map.class);
            Integer count = (Integer) req.get("THECOUNT");
            return CompletableFuture.completedFuture(count);
        }

        return CompletableFuture.completedFuture(0);
    }

    /**
     * Ends the current transaction by committing or rolling back.
     *
     * @param type The type of transaction ending (commit or rollback).
     * @return A CompletableFuture that resolves to the result of the transaction
     *         operation.
     * @throws SQLException
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ClientException
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public CompletableFuture<QueryResult<JobLogEntry>> endTransaction(TransactionEndType type)
            throws JsonMappingException, JsonProcessingException, ClientException, InterruptedException,
            ExecutionException, SQLException {
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
     * Close the socket and set the status to be ended.
     */
    public void dispose() {
        if (this.socket != null) {
            this.socket.close();
        }
        this.status = JobStatus.Ended;
    }
}
