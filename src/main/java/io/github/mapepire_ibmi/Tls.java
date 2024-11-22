package io.github.mapepire_ibmi;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import io.github.mapepire_ibmi.types.DaemonServer;

/**
 * Represents a TLS configuration.
 */
public class Tls {
    /**
     * Get the SSL server certificate for a specified DB2 server.
     * 
     * @param creds The server details for the connection.
     * @return A CompletableFuture that resolves to the SSL server certificate.
     */
    public static CompletableFuture<String> getCertificate(DaemonServer db2Server) throws Exception {
        X509TrustManager noAuthTrustManager = new X509TrustManager() {
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
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { noAuthTrustManager }, new SecureRandom());
        SSLContext.setDefault(sslContext);
        URI uri = new URI("wss://" + db2Server.getHost() + ":" + db2Server.getPort() + "/db/");
        Map<String, String> httpHeaders = new HashMap<>();
        String auth = db2Server.getUser() + ":" + db2Server.getPassword();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        httpHeaders.put("Authorization", "Basic " + encodedAuth);

        CompletableFuture<String> certificateFuture = new CompletableFuture<>();
        WebSocketClient wsc = new WebSocketClient(uri, new Draft_6455(), httpHeaders, 5000) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                try {
                    Certificate[] peerCertificates = this.getSSLSession().getPeerCertificates();
                    byte[] encodedCa = peerCertificates[0].getEncoded();
                    StringBuilder ca = new StringBuilder();
                    ca.append("-----BEGIN CERTIFICATE-----\n");
                    ca.append(Base64.getEncoder().encodeToString(encodedCa));
                    ca.append("\n-----END CERTIFICATE-----\n");
                    certificateFuture.complete(ca.toString());
                } catch (Exception e) {
                    certificateFuture.completeExceptionally(e);
                } finally {
                    this.close();
                }
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
            }

            @Override
            public void onError(Exception e) {
                certificateFuture.completeExceptionally(e);
            }
        };
        SSLSocketFactory factory = sslContext.getSocketFactory();
        wsc.setSocketFactory(factory);
        wsc.connect();

        return certificateFuture;
    }
}
