package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.QueryResult;

class CLTest extends MapepireTest {
    @Test
    void validCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.clcommand("WRKACTJOB");
        QueryResult<Object> result = query.execute().get();
        job.dispose();

        assertTrue(result.getSuccess());
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void invalidCLCommand() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.clcommand("INVALIDCOMMAND");
        QueryResult<Object> result = query.execute().get();
        job.dispose();

        assertFalse(result.getSuccess());
        assertTrue(result.getData().size() > 0);
        assertTrue(result.getError().contains("[CPF0006] Errors occurred in command."));
        assertTrue(result.getId() != null);
        assertTrue(result.getIsDone());
        assertEquals(result.getSqlRc(), -443);
        assertEquals(result.getSqlState(), "38501");
    }
}
