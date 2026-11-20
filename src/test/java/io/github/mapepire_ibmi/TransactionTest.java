package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.TransactionEndType;
import io.github.mapepire_ibmi.types.jdbcOptions.TransactionIsolation;

@Tag("integration")
class TransactionTest extends MapepireTest {
    private static String table;

    @BeforeAll
    public static void beforeAll() throws Exception {
        MapepireTest.setupCreds();
        MapepireTest.setupTestSchema();
        table = MapepireTest.getTestSchema() + ".TXN_HELPERS";
    }

    @Test
    void pendingCountCommitAndRollback() throws Exception {
        JDBCOptions options = new JDBCOptions();
        options.setTransactionIsolation(TransactionIsolation.READ_COMMITTED);
        SqlJob job = new SqlJob(options);
        job.connect(MapepireTest.getCreds()).get();

        try {
            assertTrue(job.underCommitControl());

            silentExecute(job, "DROP TABLE " + table);
            job.endTransaction(TransactionEndType.COMMIT).get();
            job.execute("CREATE TABLE " + table + " ( ID INTEGER, NAME VARCHAR(20) )").get();
            job.endTransaction(TransactionEndType.COMMIT).get();

            assertEquals(0, job.getPendingTransactions().get());

            job.execute("INSERT INTO " + table + " (ID, NAME) VALUES (1, 'A')").get();
            assertTrue(job.getPendingTransactions().get() >= 1);

            QueryResult<Object> commitResult = job.endTransaction(TransactionEndType.COMMIT).get();
            assertTrue(commitResult.getSuccess());
            assertEquals(0, job.getPendingTransactions().get());
            assertEquals(1, rowCount(job));

            job.execute("INSERT INTO " + table + " (ID, NAME) VALUES (2, 'B')").get();
            assertTrue(job.getPendingTransactions().get() >= 1);

            QueryResult<Object> rollbackResult = job.endTransaction(TransactionEndType.ROLLBACK).get();
            assertTrue(rollbackResult.getSuccess());
            assertEquals(0, job.getPendingTransactions().get());
            assertEquals(1, rowCount(job));
        } finally {
            silentExecute(job, "DROP TABLE " + table);
            silentEndTransaction(job, TransactionEndType.COMMIT);
            job.close();
        }
    }

    @Test
    void helpersDoNotGrowGlobalQueryList() throws Exception {
        JDBCOptions options = new JDBCOptions();
        options.setTransactionIsolation(TransactionIsolation.READ_COMMITTED);
        SqlJob job = new SqlJob(options);
        job.connect(MapepireTest.getCreds()).get();

        try {
            int before = globalQueryListSize();
            for (int i = 0; i < 5; i++) {
                job.getPendingTransactions().get();
                job.endTransaction(TransactionEndType.COMMIT).get();
            }
            assertEquals(before, globalQueryListSize());
        } finally {
            job.close();
        }
    }

    private static int rowCount(SqlJob job) throws Exception {
        QueryResult<Object> result = job.execute("SELECT COUNT(*) AS C FROM " + table).get();
        @SuppressWarnings("unchecked")
        Map<String, Object> row = (Map<String, Object>) result.getData().get(0);
        return ((Number) row.get("C")).intValue();
    }

    private static void silentExecute(SqlJob job, String sql) {
        try {
            job.execute(sql).get();
        } catch (Exception ignored) {
        }
    }

    private static void silentEndTransaction(SqlJob job, TransactionEndType type) {
        try {
            job.endTransaction(type).get();
        } catch (Exception ignored) {
        }
    }

    private static int globalQueryListSize() throws Exception {
        Field field = Query.class.getDeclaredField("globalQueryList");
        field.setAccessible(true);
        return ((List<?>) field.get(null)).size();
    }
}
