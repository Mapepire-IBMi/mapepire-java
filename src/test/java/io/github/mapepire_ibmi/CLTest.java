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

import io.github.mapepire_ibmi.types.CLCommandResult;
import io.github.mapepire_ibmi.types.GetClDocResult;
import io.github.mapepire_ibmi.types.SqlCLCommandResult;

class CLTest extends MapepireTest {
    private static final String GET_CL_DOC_UNSUPPORTED = "Unknown request type: getcldoc";
    private static final String RCCL_UNSUPPORTED = "Unknown request type: rccl";

    @Test
    void validCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        CLCommandResult resultCl = job.rcclCommand("CHKOBJ OBJ(QSYS) OBJTYPE(*LIB)").get();
        SqlCLCommandResult resultSql = job.clCommand("CHKOBJ OBJ(QSYS) OBJTYPE(*LIB)").get();
        job.close();

        assertClCommandSucceeded(resultSql);
        assumeRcclSupported(resultCl);
        assertClCommandSucceeded(resultCl);
    }

    @Test
    void invalidCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        CLCommandResult resultCl = job.rcclCommand("SNDMSG").get();
        SqlCLCommandResult resultSql = job.clCommand("SNDMSG").get();
        job.close();

        assumeClMessagesSupported(resultSql);
        assertInvalidClCommand(resultSql);
        assertEquals(-443, resultSql.getSqlRc());
        assertEquals("38501", resultSql.getSqlState());
        assumeRcclSupported(resultCl);
        assertInvalidClCommand(resultCl);
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

    private static void assertClCommandSucceeded(SqlCLCommandResult result) {
        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
        assertEquals(0, result.getData().size());
    }

    private static void assertInvalidClCommand(SqlCLCommandResult result) {
        assertTrue(result.getData().size() >= 2);
        assertFalse(result.getSuccess());
        assertTrue(result.getData().stream()
                .allMatch(message -> message.getId() != null && message.getId().startsWith("CP")),
                String.valueOf(result.getData().stream().map(message -> message.getId())
                        .reduce((left, right) -> left + ", " + right).orElse("")));
    }

    private static void assumeRcclSupported(CLCommandResult result) {
        assumeFalse(result.getError() != null && result.getError().contains(RCCL_UNSUPPORTED),
                "Server does not support rccl");
    }

    private static void assumeClMessagesSupported(SqlCLCommandResult result) {
        boolean legacyMessages = result.getData() != null
                && result.getData().stream().anyMatch(message -> message.getId() == null);
        assumeFalse(legacyMessages, "Server does not return the current CL message format");
    }

    private static void assumeGetClDocSupported(Throwable error) {
        assumeFalse(error instanceof SQLException && error.getMessage() != null
                && error.getMessage().contains(GET_CL_DOC_UNSUPPORTED),
                "Server does not support getcldoc");
    }
}
