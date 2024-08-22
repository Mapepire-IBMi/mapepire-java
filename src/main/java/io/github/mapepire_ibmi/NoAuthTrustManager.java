package io.github.mapepire_ibmi;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

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
