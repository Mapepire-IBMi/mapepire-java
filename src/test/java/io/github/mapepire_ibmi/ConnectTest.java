package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.ConnectionResult;
import io.github.mapepire_ibmi.types.DaemonServer;

class ConnectTest extends MapepireTest {
    @Test
    void validConnection() throws Exception {
        SqlJob job = new SqlJob();
        ConnectionResult result = job.connect(MapepireTest.getCreds()).get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getJob().contains("QZDASOINIT"));
    }

    @Test
    void invalidConnection() throws Exception {
        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            SqlJob job = new SqlJob();

            try {
                job.connect(MapepireTest.getInvalidCreds()).get();
            } catch (Exception ex) {
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage()
                .contains("The application server rejected the connection."));
    }

    @Test
    void rejectUnauthorizedFalseConnectsWhenCertificateDoesNotMatchHost() throws Exception {
        DaemonServer creds = MapepireTest.getCreds();
        String host = creds.getHost();
        String mismatchedHost = InetAddress.getByName(host).getHostAddress();
        Assumptions.assumeTrue(!mismatchedHost.equals(host));

        DaemonServer relaxedCreds = new DaemonServer(
                mismatchedHost, creds.getPort(), creds.getUser(), creds.getPassword(), false);
        SqlJob job = new SqlJob();
        ConnectionResult result = job.connect(relaxedCreds).get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getJob().contains("QZDASOINIT"));
    }

    @Test
    void rejectUnauthorizedTrueFailsWhenCertificateDoesNotMatchHost() throws Exception {
        DaemonServer creds = MapepireTest.getCreds();
        String host = creds.getHost();
        String mismatchedHost = InetAddress.getByName(host).getHostAddress();
        Assumptions.assumeTrue(!mismatchedHost.equals(host));

        DaemonServer strictCreds = new DaemonServer(
                mismatchedHost, creds.getPort(), creds.getUser(), creds.getPassword(), true, creds.getCa());

        ExecutionException e = assertThrowsExactly(ExecutionException.class, () -> {
            SqlJob job = new SqlJob();
            try {
                job.connect(strictCreds).get();
            } finally {
                job.close();
            }
        });
        assertTrue(e.getCause().getMessage().contains("No subject alternative"));
    }

    @Test
    void newJobOnSubsequentConnects() throws Exception {
        SqlJob job = new SqlJob();

        ConnectionResult result1 = job.connect(MapepireTest.getCreds()).get();
        String jobId1 = result1.getJob();
        assertTrue(jobId1.contains("QZDASOINIT"));

        ConnectionResult result2 = job.connect(MapepireTest.getCreds()).get();
        String jobId2 = result2.getJob();
        assertTrue(jobId2.contains("QZDASOINIT"));

        assertNotEquals(jobId1, jobId2);
    }
}
