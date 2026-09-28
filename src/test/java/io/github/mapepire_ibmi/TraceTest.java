package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.GetTraceDataResult;
import io.github.mapepire_ibmi.types.ServerTraceLevel;

@Timeout(100)
class TraceTest extends MapepireTest {
    private String time;
    private String invalidQuery;

    @BeforeEach
    public void beforeEach() {
        time = Long.toString(System.currentTimeMillis());
        invalidQuery = "SELECT * FROM SAMPLE." + time;
    }

    @AfterAll
    public static void afterAll() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        job.setTraceLevel(ServerTraceLevel.OFF).get();

        job.close();
    }

    @Test
    void serverTracingOff() throws Exception {
        assertTraceData(invalidQuery, ServerTraceLevel.OFF, false);
    }

    @Test
    void serverTracingOn() throws Exception {
        assertTraceData(invalidQuery, ServerTraceLevel.ON, true);
    }

    @Test
    void serverTracingErrors() throws Exception {
        assertTraceData(invalidQuery, ServerTraceLevel.ERRORS, true);
    }

    @Test
    void serverTracingDatastream() throws Exception {
        assertTraceData(invalidQuery, ServerTraceLevel.DATASTREAM, true);
    }

    @Test
    void serverTracingInputAndErrors() throws Exception {
        assertTraceData(invalidQuery, ServerTraceLevel.INPUT_AND_ERRORS, true);
    }

    GetTraceDataResult assertTraceData(String sql, ServerTraceLevel level, boolean traceExists) throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        job.setTraceLevel(level).get();

        assertThrowsExactly(SQLException.class, () -> {
            Query query = job.query(sql);

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                throw ex.getCause();
            }
        });

        GetTraceDataResult result = job.getTraceData().get();

        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getId());

        String traceData = result.getTraceData();
        String expectedError = "com.ibm.as400.access.AS400JDBCSQLSyntaxErrorException: [SQL0104] Token ." + time;
        if (traceExists) {
            // Older servers return "<prohibited>" instead of the trace data
            // (https://github.com/Mapepire-IBMi/mapepire-server/pull/120), while newer servers return it again
            assertTrue(traceData.contains("<prohibited>") || traceData.contains(expectedError),
                    "Trace data contains neither \"<prohibited>\" nor the expected error");
        } else {
            assertFalse(traceData.contains(expectedError));
        }

        return result;
    }
}
