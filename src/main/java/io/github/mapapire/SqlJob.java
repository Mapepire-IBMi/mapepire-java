package io.github.mapapire;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

class ReqRespFmt {
    String id;

    public ReqRespFmt(String _id) {
        this.id = _id;
    }
};

class TrustAllTrustManager implements X509TrustManager {
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
    String query = String.join("\n", Arrays.asList(
            "select count(*) as thecount",
            "  from qsys2.db_transaction_info",
            "  where JOB_NAME = qsys2.job_name and",
            "    (local_record_changes_pending = 'YES' or local_object_changes_pending = 'YES')"));

    private static int uniqueIdCounter = 0;
    private Object socket;
    // private responseEmitter: EventEmitter = new EventEmitter();
    private JobStatus status = JobStatus.NotStarted;
    private String traceFile;
    private boolean isTracingChannelData = false;
    private String id;
    private JDBCOptions options;

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

    // TODO:
    private CompletableFuture<SSLSocket> getChannel(DaemonServer db2Server) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { new TrustAllTrustManager() }, new SecureRandom());

                // TODO: Add logging ->
                // https://gist.github.com/luketn/4e7595cf39dab63fbcfdb62930fe8f4d
                SSLSocketFactory factory = sslContext.getSocketFactory();
                SSLSocket socket = (SSLSocket) factory.createSocket(db2Server.getHost(), db2Server.getPort());

                String auth = db2Server.getUser() + ":" + db2Server.getPassword();
                String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
                        true);
                out.println("Authorization: Basic " + encodedAuth);

                // Set up input and output streams
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // You can use a BufferedReader or other suitable method to read data from the
                // socket

                // Perform any setup or initialization if needed
                // ...

                // Example of reading a message
                String message;
                while ((message = in.readLine()) != null) {
                    if (isTracingChannelData) {
                        System.out.println(message);
                    }
                    // Process message here
                    // ...
                }

                return socket;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<String> send(String content) {
        // TODO:
        return null;
    }

    public JobStatus getStatus() {
        // TODO:
        return null;
    }

    public int getRunningCount() {
        // TODO:
        return 0;
    }

    public CompletableFuture<ConnectionResult> connect(DaemonServer db2Server) {
        // TODO:
        return null;
    }

    public <T> Query<T> query(String sql) {
        //TODO: <T> or <?>
        return null;
    }

    public <T> Query<T> query(String sql, QueryOptions opts) {
        //TODO: <T> or <?>
        return null;
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql) {
        //TODO: <T> or <?>
        return null;
    }

    public <T> CompletableFuture<QueryResult<T>> execute(String sql, QueryOptions opts) {
        //TODO: <T> or <?>
        return null;
    }

    public CompletableFuture<VersionCheckResult> getVersion() {
        //TODO:
        return null;
    }

    public CompletableFuture<ExplainResults<?>> explain(String statement) {
        //TODO: <any> or <?>
        return this.explain(statement, ExplainType.Run);
    }

    public CompletableFuture<ExplainResults<?>> explain(String statement, ExplainType type) {
        //TODO: <any> or <?>
        return null;
    }

    public String getTraceFilePath() {
        //TODO:
        return null;
    }

    public CompletableFuture<GetTraceDataResult> getTraceData() {
        //TODO:
        return null;
    }

    public CompletableFuture<SetConfigResult> setTraceData(ServerTraceDest dest, ServerTraceLevel level) {
        //TODO:
        return null;
    }

    public Query<?> clcommand(String cmd) {
        //TODO: <any> or <?>
        return null;
    }

    public boolean underCommitControl() {
        //TODO:
        return true;
    }

    public CompletableFuture<Integer> getPendingTransactions() {
        //TODO:
        return null;
    }

    public CompletableFuture<QueryResult<JobLogEntry>> endTransaction(TransactionEndType type) {
        //TODO:
        return null;
    }

    public String getUniqueId() {
        //TODO:
        return id;
    }

    public CompletableFuture<Void> close() {
        //TODO:
        return null;
    }

    public void dispose() {
        //TODO:
    }
}

// public DaemonServer UrlToDaemon(String uri) {
//     //TODO:
//     return null;    
// }