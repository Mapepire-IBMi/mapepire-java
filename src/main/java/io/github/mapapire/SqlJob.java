package io.github.mapapire;

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
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.mapapire.types.ConnectionResult;
import io.github.mapapire.types.DaemonServer;
import io.github.mapapire.types.ExplainResults;
import io.github.mapapire.types.ExplainType;
import io.github.mapapire.types.GetTraceDataResult;
import io.github.mapapire.types.JDBCOptions;
import io.github.mapapire.types.JobLogEntry;
import io.github.mapapire.types.JobStatus;
import io.github.mapapire.types.QueryOptions;
import io.github.mapapire.types.QueryResult;
import io.github.mapapire.types.ServerTraceDest;
import io.github.mapapire.types.ServerTraceLevel;
import io.github.mapapire.types.SetConfigResult;
import io.github.mapapire.types.TransactionEndType;
import io.github.mapapire.types.VersionCheckResult;
import io.github.mapapire.types.jdbcOptions.Property;
import io.github.mapapire.types.jdbcOptions.TransactionIsolation;

class ReqRespFmt {
    String id;

    public ReqRespFmt(String id) {
        this.id = id;
    }

    String getId() {
        return this.id;
    }
};

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

public class SqlJob {
    String transactionCountQuery = String.join("\n", Arrays.asList(
            "select count(*) as thecount",
            "  from qsys2.db_transaction_info",
            "  where JOB_NAME = qsys2.job_name and",
            "    (local_record_changes_pending = 'YES' or local_object_changes_pending = 'YES')"));

    private static int uniqueIdCounter = 0;
    private WebSocketClient socket;
    // private responseEmitter: EventEmitter = new EventEmitter();
    private JobStatus status = JobStatus.NotStarted;
    private String traceFile;
    private boolean isTracingChannelData = false;
    private String id;
    private JDBCOptions options;
    private final HashMap<String, CompletableFuture<String>> responseMap = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // currently unused but we will inevitably need a unique ID assigned to each
    // instance since server job names can be reused in some circumstances
    private String uniqueId = SqlJob.getNewUniqueId("sqljob");

    public static String getNewUniqueId() {
        return SqlJob.getNewUniqueId("id");
    }

    public static String getNewUniqueId(String prefix) {
        return prefix + (++uniqueIdCounter);
    }

    // TODO:
    public SqlJob() {

    }

    public SqlJob(JDBCOptions _options) {
        this.options = _options;
    }

    // LOGGING -> https://gist.github.com/luketn/4e7595cf39dab63fbcfdb62930fe8f4d
    private CompletableFuture<WebSocketClient> getChannel(DaemonServer db2Server) {
        // return CompletableFuture.supplyAsync(() -> {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { new NoAuthTrustManager() }, new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();

            URI uri = new URI("wss://" + db2Server.getHost() + ":" + db2Server.getPort() + "/db/");
            Map<String, String> httpHeaders = new HashMap<>();
            String auth = db2Server.getUser() + ":" + db2Server.getPassword();
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            httpHeaders.put("Authorization", "Basic " + encodedAuth);

            WebSocketClient wsc = new WebSocketClient(uri, null, httpHeaders, 5000) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Opened connection");
                }

                @Override
                public void onMessage(String message) {
                    if (isTracingChannelData) {
                        System.out.println(message);
                    }

                    try {
                        ReqRespFmt response = objectMapper.readValue(message, ReqRespFmt.class);
                        CompletableFuture<String> future = responseMap.get(response.getId());
                        if (future != null) {
                            future.complete(message);
                        }
                    } catch (JsonProcessingException e) {
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

            if (db2Server.isIgnoreUnauthorized()) {
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
        // });
    }

    public CompletableFuture<String> send(String content) {
        if (this.isTracingChannelData) {
            System.out.println(content);
        }

        try {
            ReqRespFmt req = objectMapper.readValue(content, ReqRespFmt.class);
            CompletableFuture<String> future = new CompletableFuture<>();
            responseMap.put(req.getId(), future);
            this.socket.send(content);
            future.thenApply(message -> {
                responseMap.remove(req.getId());
                return message;
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JobStatus getStatus() {
        return this.getRunningCount() > 0 ? JobStatus.Busy : this.status;
    }

    public int getRunningCount() {
        // TODO:
        return 0;
    }

    // TODO:
    public CompletableFuture<ConnectionResult> connect(DaemonServer db2Server) {
        this.status = JobStatus.Connecting;
        try {
            this.socket = this.getChannel(db2Server).get();
            this.socket.connect();
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
        connectionObject.put("technique", "tcp");// TODO: DOVE does not work in cli mode
        connectionObject.put("application", "Java client");
        connectionObject.put("props", props.length() > 0 ? props : null);

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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (connectResult.isSuccess()) {
            this.status = JobStatus.Ready;
        } else {
            this.dispose();
            this.status = JobStatus.NotStarted;
            if (connectResult.getError() != null) {
                throw new Error(connectResult.getError());
            } else {
                throw new Error("Failed to connect to server.");
            }
        }

        this.id = connectResult.getJob();
        this.isTracingChannelData = false;

        return CompletableFuture.completedFuture(connectResult);
    }

    public <T> Query<T> query(String sql) {
        QueryOptions options = new QueryOptions();
        return this.query(sql, options);
    }

    public <T> Query<T> query(String sql, QueryOptions opts) {
        return new Query(this, sql, opts);
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql) {
        QueryOptions options = new QueryOptions();
        return this.execute(sql, options);
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) {
        Query<T> query = query(sql, opts);
        CompletableFuture<QueryResult<T>> future = query.execute();
        try {
            QueryResult<T> result = future.get();
            query.close().get();

            if (result.getError() != null) {
                throw new Error(result.getError());
            }

            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<VersionCheckResult> getVersion() {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!version.isSuccess()) {
            if (version.getError() != null) {
                throw new Error(version.getError());
            } else {
                throw new Error("Failed to get version from backend");
            }
        }

        return CompletableFuture.completedFuture(version);
    }

    public CompletableFuture<ExplainResults<?>> explain(String statement) {
        return this.explain(statement, ExplainType.Run);
    }

    public CompletableFuture<ExplainResults<?>> explain(String statement, ExplainType type) {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!explainResult.isSuccess()) {
            if (explainResult.getError() != null) {
                throw new Error(explainResult.getError());
            } else {
                throw new Error("Failed to explain.");
            }
        }

        return CompletableFuture.completedFuture(explainResult);
    }

    public String getTraceFilePath() {
        return this.traceFile;
    }

    public CompletableFuture<GetTraceDataResult> getTraceData() {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!rpy.isSuccess()) {
            if (rpy.getError() != null) {
                throw new Error(rpy.getError());
            } else {
                throw new Error("Failed to get trace data from backend");
            }
        }

        return CompletableFuture.completedFuture(rpy);
    }

    public CompletableFuture<SetConfigResult> setTraceConfig(ServerTraceDest dest, ServerTraceLevel level) {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }

        if (!rpy.isSuccess()) {
            if (rpy.getError() != null) {
                throw new Error(rpy.getError());
            } else {
                throw new Error("Failed to set trace options on backend");
            }
        }

        this.traceFile = (rpy.getTracedest().getValue() != null && rpy.getTracedest().getValue().charAt(0) == '/'
                ? rpy.getTracedest().getValue()
                : null);
        return CompletableFuture.completedFuture(rpy);
    }

    public Query<?> clcommand(String cmd) {
        QueryOptions options = new QueryOptions();
        options.setClCommand(true);
        return new Query(this, cmd, options);
    }

    public boolean underCommitControl() {
        return this.options.getOption(Property.TRANSACTION_ISOLATION) != null
                && this.options.getOption(Property.TRANSACTION_ISOLATION) != TransactionIsolation.NONE.getValue();
    }

    // // TODO:
    // public CompletableFuture<Integer> getPendingTransactions() {
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
    // }

    public CompletableFuture<QueryResult<JobLogEntry>> endTransaction(TransactionEndType type) {
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

    public String getUniqueId() {
        return this.uniqueId;
    }

    public void close() {
        this.dispose();
    }

    public void dispose() {
        if (this.socket != null) {
            this.socket.close();
        }
        this.status = JobStatus.Ended;
    }
}