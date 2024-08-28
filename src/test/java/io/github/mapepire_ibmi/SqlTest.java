package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.exceptions.ClientException;

@SuppressWarnings("unchecked")
class SqlTest extends MapepireTest {
    @Test
    void simpleQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.query("SELECT * FROM SAMPLE.DEPARTMENT");
        QueryResult<Object> result = query.execute().get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void simpleQueryInTerseFormat() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(true, false, null);
        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS", options);
        QueryResult<Object> result = query.execute(5).get();
        ArrayList<String> row = (ArrayList<String>) result.getData().get(0);

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertFalse(result.getIsDone());
        assertEquals(5, result.getData().size());
        assertEquals("NAME", row.get(0));
        assertEquals("DELETEME", row.get(1));
    }

    @Test
    void largeDatasetQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");
        QueryResult<Object> result = query.execute(50).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertFalse(result.getIsDone());
        assertEquals(50, result.getData().size());
    }

    @Test
    void notExistentTableQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query<Object> query = job.query("SELECT * from SCOOBY");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals(
                "[SQL0204] SCOOBY in " + getCreds().getUser().toUpperCase() + " type *FILE not found., 42704, -204",
                e.getMessage());
    }

    @Test
    void emptyQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query<Object> query = job.query("");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals("A string parameter value with zero length was detected., 43617, -99999", e.getMessage());
    }

    @Test
    void invalidTokenQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query<Object> query = job.query("a");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals(
                "[SQL0104] Token A was not valid. Valid tokens: ( CL END GET SET TAG CALL DROP FREE HOLD LOCK OPEN WITH ALTER., 42601, -104",
                e.getMessage());
    }

    @Test
    void invalidRowToFetchQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        ClientException e = assertThrowsExactly(ClientException.class, () -> {
            Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");

            try {
                query.execute(0).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals("Rows to fetch must be greater than 0", e.getMessage());
    }

    @Test
    void dropTableQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.query("DROP TABLE SAMPLE.DELETE IF EXISTS");
        QueryResult<Object> result = query.execute().get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertTrue(result.getId() != null);
        assertTrue(result.getMetadata() != null);
        assertFalse(result.getHasResults());
    }

    @Test
    void fetchMoreQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");
        QueryResult<Object> result = query.execute(5).get();

        while (!result.getIsDone()) {
            assertTrue(result.getData().size() > 0);
            result = query.fetchMore(300).get();
        }

        query.close().get();
        job.close();
    }

    @Test
    void fetchMoreOnPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(false, false, Arrays.asList("N"));
        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE IS_NULLABLE = ?", options);
        QueryResult<Object> result = query.execute(5).get();

        while (!result.getIsDone()) {
            assertTrue(result.getData().size() > 0);
            result = query.fetchMore(300).get();
        }

        query.close().get();
        job.close();
    }

    @Test
    void executeOnPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(false, false, Arrays.asList("LONG_COMMENT"));
        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);
        QueryResult<Object> result = query.execute(10).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void executeOnPreparedQueryInTerseFormat() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(true, false, Arrays.asList("PHONE"));
        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);
        QueryResult<Object> result = query.execute().get();
        ArrayList<String> row = (ArrayList<String>) result.getData().get(0);

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertFalse(result.getIsDone());
        assertEquals(1, result.getData().size());
        assertEquals("PHONE", row.get(0));
        assertEquals("DELETEME", row.get(1));
    }

    @Test
    void executeOnMultipleParameterPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(false, false,
                Arrays.asList("TABLE_NAME", "LONG_COMMENT", "CONSTRAINT_NAME"));
        Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME IN (?, ?, ?)", options);
        QueryResult<Object> result = query.execute(30).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void executeOnNoParameterPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Collections.emptyList());
            Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals(
                "The number of parameter values set or registered does not match the number of parameters., 07001, -99999",
                e.getMessage());
    }

    @Test
    void executeOnWrongParameterCountPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Arrays.asList("A", "B"));
            Query<Object> query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals("Descriptor index not valid. (2>1), 07009, -99999", e.getMessage());
    }

    @Test
    void executeOnInvalidPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Arrays.asList("FAKE_COLUMN"));
            Query<Object> query = job.query("SELECT * FROM FAKE_SCHEMA.FAKE_TABLE WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex;
            }
        });

        assertEquals(
                "[SQL0204] FAKE_TABLE in FAKE_SCHEMA type *FILE not found., 42704, -204",
                e.getMessage());
    }

    @Test
    void simpleJobExecute() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryResult<Object> result = job.execute("SELECT * FROM SAMPLE.DEPARTMENT").get();

        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertTrue(result.getId() != null);
        assertTrue(result.getHasResults());
        assertTrue(result.getMetadata() != null);
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void singleJobMultipleStatements() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryResult<Object> resultA = job.execute("SELECT * FROM SAMPLE.DEPARTMENT").get();
        QueryResult<Object> resultB = job.execute("SELECT * FROM SAMPLE.EMPLOYEE").get();

        job.close();

        assertTrue(resultA.getIsDone());
        assertTrue(resultB.getIsDone());
    }

    @Test
    void singleJobMultipleStatementsInParallel() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        CompletableFuture<QueryResult<Object>> resultAFuture = job.execute("SELECT * FROM SAMPLE.DEPARTMENT");
        CompletableFuture<QueryResult<Object>> resultBFuture = job.execute("SELECT * FROM SAMPLE.EMPLOYEE");
        CompletableFuture.allOf(resultAFuture, resultBFuture).join();
        QueryResult<Object> resultA = resultAFuture.get();
        QueryResult<Object> resultB = resultBFuture.get();

        job.close();

        assertTrue(resultA.getIsDone());
        assertTrue(resultB.getIsDone());
    }
}
