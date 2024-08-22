package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.PoolOptions;
import io.github.mapepire_ibmi.types.QueryResult;
import io.github.mapepire_ibmi.types.exceptions.ClientException;

@Timeout(100)
@SuppressWarnings("unchecked")
class PoolTest extends MapepireTest {
    @Test
    void executeOnPool() throws Exception {
        PoolOptions options = new PoolOptions(MapepireTest.getCreds(), 5, 3);
        Pool pool = new Pool(options);
        pool.init().get();

        List<CompletableFuture<QueryResult<Object>>> futures1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            futures1.add(pool.execute("values (job_name)"));
        }
        CompletableFuture<Void> allFutures1 = CompletableFuture.allOf(futures1.toArray(new CompletableFuture[0]));
        allFutures1.join();

        List<String> jobNames1 = new ArrayList<>();
        for (CompletableFuture<QueryResult<Object>> future : futures1) {
            jobNames1.add(((HashMap<String, String>) future.get().getData().get(0)).get("00001"));
        }

        assertEquals(3, jobNames1.size());
        assertEquals(3, pool.getActiveJobCount());

        List<CompletableFuture<QueryResult<Object>>> futures2 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            futures2.add(pool.execute("values (job_name)"));
        }
        CompletableFuture<Void> allFutures2 = CompletableFuture.allOf(futures2.toArray(new CompletableFuture[0]));
        allFutures2.join();

        List<String> jobNames2 = new ArrayList<>();
        for (CompletableFuture<QueryResult<Object>> future : futures2) {
            jobNames2.add(((HashMap<String, String>) future.get().getData().get(0)).get("00001"));
        }

        assertEquals(15, jobNames2.size());
        assertTrue(pool.getActiveJobCount() >= 3);
        assertTrue(pool.getActiveJobCount() <= 5);

        pool.end();
    }

    @Test
    void invalidSizes() throws Exception {
        ClientException e1 = assertThrowsExactly(ClientException.class, () -> {
            PoolOptions options = new PoolOptions(MapepireTest.getCreds(), 1, 10);
            Pool pool = new Pool(options);

            try {
                pool.init().get();
            } catch (Exception e) {
                pool.end();
            }
        });

        ClientException e2 = assertThrowsExactly(ClientException.class, () -> {
            PoolOptions options = new PoolOptions(MapepireTest.getCreds(), 0, 10);
            Pool pool = new Pool(options);

            try {
                pool.init().get();
            } catch (Exception e) {
                pool.end();
            }
        });

        assertEquals("Max size must be greater than starting size", e1.getMessage());
        assertEquals("Max size must be greater than 0", e2.getMessage());
    }

    @Test
    void performance() throws Exception {
        long startPool1 = System.currentTimeMillis();
        PoolOptions options = new PoolOptions(MapepireTest.getCreds(), 5, 3);
        Pool pool = new Pool(options);
        pool.init().get();

        List<CompletableFuture<QueryResult<Object>>> futures1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            futures1.add(pool.execute("SELECT * FROM SAMPLE.SYSCOLUMNS"));
        }
        CompletableFuture<Void> allFutures1 = CompletableFuture.allOf(futures1.toArray(new CompletableFuture[0]));
        allFutures1.join();
        long endPool1 = System.currentTimeMillis();
        pool.end();

        for (CompletableFuture<QueryResult<Object>> future : futures1) {
            assertTrue(future.get().getHasResults());
        }

        long startPool2 = System.currentTimeMillis();
        options = new PoolOptions(MapepireTest.getCreds(), 1, 1);
        pool = new Pool(options);
        pool.init().get();

        List<CompletableFuture<QueryResult<Object>>> futures2 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            futures2.add(pool.execute("SELECT * FROM SAMPLE.SYSCOLUMNS"));
        }
        CompletableFuture<Void> allFutures2 = CompletableFuture.allOf(futures2.toArray(new CompletableFuture[0]));
        allFutures2.join();
        long endPool2 = System.currentTimeMillis();
        pool.end();

        for (CompletableFuture<QueryResult<Object>> future : futures2) {
            assertTrue(future.get().getHasResults());
        }

        long startNoPool = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            SqlJob job = new SqlJob();
            job.connect(MapepireTest.getCreds()).get();
            job.execute("SELECT * FROM SAMPLE.SYSCOLUMNS").get();
            job.close();
        }
        long endNoPool = System.currentTimeMillis();

        assertTrue(endPool2 - startPool2 > endPool1 - startPool1);
        assertTrue(endNoPool - startNoPool > endPool2 - startPool2);
    }
}
