package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.DaemonServer;

class TlsTest extends MapepireTest {
    @Test
    void canGetCertCorrectly() throws Exception {
        String ca = Tls.getCertificate(MapepireTest.getCreds()).get();

        assertNotNull(ca);
    }

    @Test
    void failsToGetCertCorrectly() throws Exception {
        ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> {
            DaemonServer invalidCreds = MapepireTest.getInvalidCreds();
            invalidCreds.setHost("FAKE_HOST");
            Tls.getCertificate(invalidCreds).get();
        });

        assertTrue(e.getMessage().contains("Connection refused: connect"));
    }

    @Test
    void failsHandshakeWithInvalidCa() throws Exception {
        ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> {
            DaemonServer creds = MapepireTest.getCreds();
            creds.setCa(MapepireTest.invalidCA);
            SqlJob job = new SqlJob();
            job.connect(creds).get();
        });

        assertTrue(e.getMessage().contains(
                "PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target"));
    }
}
