package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.GetTraceDataResult;
import io.github.mapepire_ibmi.types.ServerTraceDest;
import io.github.mapepire_ibmi.types.ServerTraceLevel;

@Timeout(100)
class TraceTest extends MapepireTest {
    private String time;
    private String validQuery = "SELECT * FROM SAMPLE.DEPARTMENT";
    private String invalidQuery;

    @BeforeEach
    public void beforeEach() {
        time = Long.toString(System.currentTimeMillis());
        invalidQuery = "SELECT * FROM SAMPLE." + time;
    }

    @Test
    void serverTracingOff() throws Exception {
        assertTraceData(invalidQuery, ServerTraceDest.IN_MEM, ServerTraceLevel.OFF, false);
    }

    @Test
    void serverTracingOn() throws Exception {
        assertTraceData(invalidQuery, ServerTraceDest.IN_MEM, ServerTraceLevel.ON, true);
    }

    @Test
    void errorServerTracing() throws Exception {
        assertTraceData(invalidQuery, ServerTraceDest.IN_MEM, ServerTraceLevel.ERRORS, true);
    }

    @Test
    void dataStreamServerTracing() throws Exception {
        assertTraceData(invalidQuery, ServerTraceDest.IN_MEM, ServerTraceLevel.DATASTREAM, true);
    }

    GetTraceDataResult assertTraceData(String sql, ServerTraceDest dest, ServerTraceLevel level,
            boolean traceExists)
            throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        job.setTraceConfig(dest, level).get();

        assertThrowsExactly(SQLException.class, () -> {
            Query<Object> query = job.query(sql);

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                throw ex;
            }
        });

        GetTraceDataResult result = job.getTraceData().get();

        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getId() != null);

        if (traceExists) {
            assertTrue(result.getTracedata()
                    .contains("com.ibm.as400.access.AS400JDBCSQLSyntaxErrorException: [SQL0104] Token ." + time));
        } else {
            assertFalse(result.getTracedata()
                    .contains("com.ibm.as400.access.AS400JDBCSQLSyntaxErrorException: [SQL0104] Token ." + time));
        }

        return result;
    }
}
