package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.QueryResult;

class PoolTest extends MapepireTest {
    @Test
    @Timeout(222)
    @SuppressWarnings("unchecked")
    void simplePoolUsingPoolExecute() throws Exception {
        PoolOptions options = new PoolOptions(MapepireTest.creds, 5, 3);
        Pool pool = new Pool(options);
        pool.init().get();

        List<CompletableFuture<QueryResult<Object>>> futuresA = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            futuresA.add(pool.execute("values (job_name)"));
        }
        CompletableFuture<Void> allOfA = CompletableFuture.allOf(futuresA.toArray(new CompletableFuture[0]));
        allOfA.join();

        List<String> jobNamesA = new ArrayList<>();
        for (CompletableFuture<QueryResult<Object>> future : futuresA) {
            jobNamesA.add(((HashMap<String, String>) future.get().getData().get(0)).get("00001"));
        }

        assertEquals(3, jobNamesA.size());
        assertEquals(3, pool.getActiveJobCount());

        List<CompletableFuture<QueryResult<Object>>> futuresB = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            futuresB.add(pool.execute("values (job_name)"));
        }
        CompletableFuture<Void> allOfB = CompletableFuture.allOf(futuresB.toArray(new CompletableFuture[0]));
        allOfB.join();

        List<String> jobNamesB = new ArrayList<>();
        for (CompletableFuture<QueryResult<Object>> future : futuresB) {
            jobNamesB.add(((HashMap<String, String>) future.get().getData().get(0)).get("00001"));
        }

        assertEquals(15, jobNamesB.size());
        assertTrue(pool.getActiveJobCount() >= 3);
        assertTrue(pool.getActiveJobCount() <= 5);

        pool.end();
    }
}
