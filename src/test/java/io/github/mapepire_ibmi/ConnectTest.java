package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.ConnectionResult;

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

        assertEquals(
                "java.sql.SQLException: The application server rejected the connection. (User ID is not known.:FAKE_USER)",
                e.getMessage());
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
