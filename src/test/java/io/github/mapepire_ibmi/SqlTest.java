package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.exceptions.ClientException;
import io.github.mapepire_ibmi.types.jdbcOptions.Naming;

@SuppressWarnings("unchecked")
class SqlTest extends MapepireTest {
    @BeforeAll
    public static void beforeAll() throws Exception {
        MapepireTest.setupCreds();
        MapepireTest.setupTestSchema();
    }

    @Test
    void simpleQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query query = job.query("SELECT * FROM SAMPLE.DEPARTMENT");
        QueryResult<Object> result = query.execute().get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void simpleQueryWithJDBCOptions() throws Exception {
        JDBCOptions options = new JDBCOptions();
        options.setLibraries(Arrays.asList("SAMPLE"));
        options.setNaming(Naming.SQL);
        SqlJob job = new SqlJob(options);
        job.connect(MapepireTest.getCreds()).get();

        Query queryA = job.query("SELECT * FROM DEPARTMENT");
        QueryResult<Object> result = queryA.execute().get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query queryB = job.query("SELECT * FROM SAMPLE/DEPARTMENT");

            try {
                queryB.execute(1).get();
            } catch (Exception ex) {
                queryB.close().get();
                throw ex.getCause();
            }
        });

        queryA.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertTrue(result.getData().size() > 0);
        assertTrue(e.getMessage().contains("[SQL5016] Qualified object name DEPARTMENT not valid."));
    }

    @Test
    void simpleQueryInTerseFormat() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(true, false, null);
        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS", options);
        QueryResult<Object> result = query.execute(5).get();
        ArrayList<String> row = (ArrayList<String>) result.getData().get(0);

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertFalse(result.getIsDone());
        assertEquals(5, result.getData().size());
        assertNotNull(row.get(0));
    }

    @Test
    void queryWithSpecialCharacters() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query queryA = job.query(
                "CREATE OR REPLACE TABLE " + MapepireTest.getTestSchema()
                        + ".GERMAN_TEXT ( id INTEGER NOT NULL, text VARCHAR(2000) CCSID 273 DEFAULT '' )");
        QueryResult<Object> resultA = queryA.execute().get();

        Query queryB = job.query(
                "DELETE FROM " + MapepireTest.getTestSchema() + ".GERMAN_TEXT");
        QueryResult<Object> resultB = queryB.execute().get();

        Query queryC = job.query("INSERT INTO " + MapepireTest.getTestSchema()
                + ".GERMAN_TEXT (id, text) values(1, 'grün'), (2, 'weiß'), (3, 'Perücke')");
        QueryResult<Object> resultC = queryC.execute().get();

        Query queryD = job.query("SELECT * FROM " + MapepireTest.getTestSchema() + ".GERMAN_TEXT");
        QueryResult<Object> resultD = queryD.execute().get();

        queryA.close().get();
        queryB.close().get();
        queryC.close().get();
        queryD.close().get();
        job.close();

        String value1 = ((LinkedHashMap<String, String>) resultD.getData().get(0)).get("TEXT");
        String value2 = ((LinkedHashMap<String, String>) resultD.getData().get(1)).get("TEXT");
        String value3 = ((LinkedHashMap<String, String>) resultD.getData().get(2)).get("TEXT");

        assertTrue(resultA.getSuccess());
        assertTrue(resultB.getSuccess());
        assertTrue(resultC.getSuccess());
        assertTrue(resultD.getSuccess());
        assertEquals("grün", value1);
        assertEquals("weiß", value2);
        assertEquals("Perücke", value3);
    }

    @Test
    void largeDatasetQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");
        QueryResult<Object> result = query.execute(50).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertFalse(result.getIsDone());
        assertEquals(50, result.getData().size());
    }

    @Test
    void notExistentTableQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query query = job.query("SELECT * from SCOOBY");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage()
                .contains("[SQL0204] SCOOBY in " + getCreds().getUser().toUpperCase() + " type *FILE not found."));
    }

    @Test
    void emptyQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query query = job.query("");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage().contains("A string parameter value with zero length was detected."));
    }

    @Test
    void invalidTokenQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            Query query = job.query("a");

            try {
                query.execute(1).get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage().contains(
                "[SQL0104] Token A was not valid. Valid tokens: ( CL END GET SET TAG CALL DROP FREE HOLD LOCK OPEN WITH ALTER."));
    }

    @Test
    void invalidRowToFetchQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        ClientException e = assertThrowsExactly(ClientException.class, () -> {
            Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");

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

        Query query = job.query("DROP TABLE SAMPLE.DELETE IF EXISTS");
        QueryResult<Object> result = query.execute().get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getId());
    }

    @Test
    void fetchMoreQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS");
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
        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE IS_NULLABLE = ?", options);
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
        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);
        QueryResult<Object> result = query.execute(10).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void executeOnPreparedQueryInTerseFormat() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(true, false, Arrays.asList("TABLE_NAME"));
        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);
        QueryResult<Object> result = query.execute(1).get();
        ArrayList<String> row = (ArrayList<String>) result.getData().get(0);

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertFalse(result.getIsDone());
        assertEquals(1, result.getData().size());
        assertEquals("TABLE_NAME", row.get(0));
    }

    @Test
    void executeOnMultipleParameterPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryOptions options = new QueryOptions(false, false,
                Arrays.asList("TABLE_NAME", "LONG_COMMENT", "CONSTRAINT_NAME"));
        Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME IN (?, ?, ?)", options);
        QueryResult<Object> result = query.execute(30).get();

        query.close().get();
        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
        assertTrue(result.getData().size() > 0);
    }

    @Test
    void executeOnNoParameterPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Collections.emptyList());
            Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage().contains(
                "The number of parameter values set or registered does not match the number of parameters."));
    }

    @Test
    void executeOnWrongParameterCountPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Arrays.asList("A", "B"));
            Query query = job.query("SELECT * FROM SAMPLE.SYSCOLUMNS WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage().contains("Descriptor index not valid. (2>1)"));
    }

    @Test
    void executeOnInvalidPreparedQuery() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        SQLException e = assertThrowsExactly(SQLException.class, () -> {
            QueryOptions options = new QueryOptions(false, false, Arrays.asList("FAKE_COLUMN"));
            Query query = job.query("SELECT * FROM FAKE_SCHEMA.FAKE_TABLE WHERE COLUMN_NAME = ?", options);

            try {
                query.execute().get();
            } catch (Exception ex) {
                query.close().get();
                job.close();
                throw ex.getCause();
            }
        });

        assertTrue(e.getMessage().contains("[SQL0204] FAKE_TABLE in FAKE_SCHEMA type *FILE not found."));
    }

    @Test
    void simpleJobExecute() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        QueryResult<Object> result = job.execute("SELECT * FROM SAMPLE.DEPARTMENT").get();

        job.close();

        assertTrue(result.getSuccess());
        assertTrue(result.getIsDone());
        assertNotNull(result.getId());
        assertTrue(result.getHasResults());
        assertNotNull(result.getMetadata());
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
        CompletableFuture.allOf(resultAFuture, resultBFuture).get();
        QueryResult<Object> resultA = resultAFuture.get();
        QueryResult<Object> resultB = resultBFuture.get();

        job.close();

        assertTrue(resultA.getIsDone());
        assertTrue(resultB.getIsDone());
    }
}
