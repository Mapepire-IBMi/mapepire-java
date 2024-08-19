package io.github.mapepire;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.github.mapepire.types.QueryResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleTest extends MapepireTest {
    @Test
    void simpleTest() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.creds).get();

        Query<Object> query = job.query("select * from sample.department");
        QueryResult<Object> result = query.execute().get();

        assertTrue(result.getIsDone());
        assertTrue(result.getData().size() > 0);

        job.close();
    }

    @Test
    void pagingTest() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.creds).get();

        Query<Object> query = job.query("select * from sample.department");
        QueryResult<Object> result = query.execute(5).get();
        while (true) {
            assertTrue(result.getData().size() > 0);

            if (result.getIsDone()) {
                break;
            }
            result = query.fetchMore(5).get();
        }

        query.close().get();
        job.close();
    }

    @Test
    void errorTest() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.creds).get();

        Query<Object> query = job.query("select * from scooby");

        try {
            query.execute(5).get();
            throw new Exception("Exception not hit");
        } catch (Exception e) {
            assertEquals("[SQL0204] SCOOBY in " + creds.getUser().toUpperCase() + " type *FILE not found., 42704, -204",
                    e.getMessage());
        }

        query.close().get();
        job.close();
    }

    @Test
    void multipleStatementsOneJobTest() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.creds).get();

        QueryResult<Object> resultA = job.query("select * from sample.department").execute().get();
        assertTrue(resultA.getIsDone());

        QueryResult<Object> resultB = job.query("select * from sample.employee").execute().get();
        assertTrue(resultB.getIsDone());

        job.close();
    }

    @Test
    void multipleStatementsParallelOneJobTest() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.creds).get();

        CompletableFuture<QueryResult<Object>> resultAFuture = job.query("select * from sample.department").execute();
        CompletableFuture<QueryResult<Object>> resultBFuture = job.query("select * from sample.employee").execute();
        CompletableFuture.allOf(resultAFuture, resultBFuture).join();
        QueryResult<Object> resultA = resultAFuture.get();
        QueryResult<Object> resultB = resultBFuture.get();

        assertTrue(resultA.getIsDone());
        assertTrue(resultB.getIsDone());

        job.close();
    }
}
