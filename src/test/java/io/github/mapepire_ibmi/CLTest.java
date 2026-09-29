package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.GetClDocResult;
import io.github.mapepire_ibmi.types.QueryResult;

class CLTest extends MapepireTest {
    private static final String GET_CL_DOC_UNSUPPORTED = "Unknown request type: getcldoc";

    @Test
    void validCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query query = job.clCommand("WRKACTJOB");
        QueryResult<Object> result = query.execute().get();
        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
    }

    @Test
    void invalidCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query query = job.clCommand("INVALIDCOMMAND");
        QueryResult<Object> result = query.execute().get();
        job.close();

        assertFalse(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getData().size() > 0);
        assertEquals(-443, result.getSqlRc());
        assertEquals("38501", result.getSqlState());
        assertEquals("[CPF0006] Errors occurred in command.", result.getError());
    }

    @Test
    void validCLCommandDocumentation() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        GetClDocResult result;
        try {
            result = job.getClDoc("/QSYS.LIB/CRTLIB.CMD").get();
        } catch (ExecutionException e) {
            assumeGetClDocSupported(e.getCause());
            throw e;
        } finally {
            job.close();
        }

        assertTrue(result.getSuccess());
        assertTrue(result.getHtml().contains("<title>Create Library  (CRTLIB)</title>"));
        assertTrue(result.getUim().contains("Help for command CRTLIB"));
    }

    @Test
    void invalidCLCommandDocumentation() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            try {
                job.getClDoc("/QSYS.LIB/INVALID.CMD").get();
            } catch (Exception ex) {
                throw ex.getCause();
            } finally {
                job.close();
            }
        });
        assumeGetClDocSupported(e);

        assertTrue(e.getMessage()
                .contains("CPF9801 Object INVALID in library QSYS not found."));
    }

    private static void assumeGetClDocSupported(Throwable error) {
        assumeFalse(error instanceof SQLException && error.getMessage() != null
                && error.getMessage().contains(GET_CL_DOC_UNSUPPORTED),
                "Server does not support getcldoc");
    }
}
