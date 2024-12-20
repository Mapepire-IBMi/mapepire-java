package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.QueryResult;

class CLTest extends MapepireTest {
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
}
