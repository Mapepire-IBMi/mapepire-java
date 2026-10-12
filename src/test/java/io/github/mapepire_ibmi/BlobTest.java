package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.BlobRef;
import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;

/**
 * BLOB support tests for mapepire-java in daemon mode.
 *
 * <p>Mirrors and extends the scenarios in mapepire-server/scripts/test-blob-daemon.js:
 *
 * <ol>
 *   <li>INSERT a BLOB via prepared-statement parameter (Base64 encoded)</li>
 *   <li>SELECT the BLOB column — expect a BlobRef {blob_url, size} in the result</li>
 *   <li>fetchBlob() with correct credentials — returns the raw bytes</li>
 *   <li>fetchBlob() a second time on the same token — throws (single-use, 404)</li>
 *   <li>Direct HTTP GET with wrong credentials — returns 401</li>
 *   <li>SELECT a NULL BLOB — result cell is null</li>
 *   <li>INSERT a large BLOB (&gt;1 MB), SELECT returns BlobRef quickly (async spool),
 *       fetchBlob() returns all bytes with correct content</li>
 *   <li>Binary fidelity — round-trip of all 256 byte values (0x00–0xFF)</li>
 *   <li>Multiple rows in a single SELECT — each row has a distinct blob_url token</li>
 *   <li>Concurrent fetchBlob() of two tokens at the same time — both succeed</li>
 *   <li>Column metadata reports the column type as BLOB</li>
 *   <li>UPDATE BLOB value — SELECT after UPDATE returns new content</li>
 *   <li>fetchBlob() on an unconnected job — throws with "not connected" error</li>
 *   <li>HTTP GET with a completely bogus token — returns 404</li>
 *   <li>blob_url path format — token portion is a valid UUID (RFC 4122)</li>
 *   <li>Terse-results mode — BlobRef is still returned when isTerseResults=true</li>
 * </ol>
 *
 * <p>Prerequisites (run once on the server):
 * <pre>
 *   CREATE TABLE &lt;user&gt;.TEMPBLOB ( ID INTEGER GENERATED ALWAYS AS IDENTITY,
 *                                    JBLOB BLOB(100) )
 * </pre>
 *
 * <p>Tests 7, 8, and 12 create and drop their own tables so no manual DDL is
 * required for those scenarios.
 */
@SuppressWarnings("unchecked")
@Timeout(60)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlobTest extends MapepireTest {

    // -----------------------------------------------------------------------
    // Constants
    // -----------------------------------------------------------------------

    private static final String TEST_STRING = "Hello from daemon mode";
    private static final String TEST_BASE64 = Base64.getEncoder()
            .encodeToString(TEST_STRING.getBytes(StandardCharsets.UTF_8));

    /**
     * 2 MB of repeating 0x41 ('A') — above the 1 MB async-spool threshold.
     */
    private static final int LARGE_BLOB_SIZE = 2 * 1024 * 1024;
    private static final String LARGE_BLOB_BASE64;

    static {
        byte[] large = new byte[LARGE_BLOB_SIZE];
        Arrays.fill(large, (byte) 0x41);
        LARGE_BLOB_BASE64 = Base64.getEncoder().encodeToString(large);
    }

    /**
     * All 256 byte values in sequence (0x00, 0x01, ..., 0xFF).
     */
    private static final byte[] BINARY_BUF;
    private static final String BINARY_BASE64;

    static {
        BINARY_BUF = new byte[256];
        for (int i = 0; i < 256; i++) {
            BINARY_BUF[i] = (byte) i;
        }
        BINARY_BASE64 = Base64.getEncoder().encodeToString(BINARY_BUF);
    }

    /**
     * RFC 4122 UUID pattern (version 1-5).
     */
    private static final Pattern UUID_RE = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            Pattern.CASE_INSENSITIVE);

    // -----------------------------------------------------------------------
    // Shared state
    // -----------------------------------------------------------------------

    private static DaemonServer creds;
    private static SqlJob job;
    private static String schema;

    private static String smallTable() {
        return schema + ".TEMPBLOB";
    }

    private static String largeTable() {
        return schema + ".TEMPBLOB_LARGE";
    }

    private static String binaryTable() {
        return schema + ".TEMPBLOB_BINARY";
    }

    private static String updateTable() {
        return schema + ".TEMPBLOB_UPDATE";
    }

    // -----------------------------------------------------------------------
    // Setup / teardown
    // -----------------------------------------------------------------------

    @BeforeAll
    public static void beforeAll() throws Exception {
        MapepireTest.setupCreds();
        creds = MapepireTest.getCreds();
        schema = creds.getUser().toUpperCase();

        job = new SqlJob();
        job.connect(creds).get();

        // Drop and recreate the small BLOB table so every run starts clean
        // and we own the schema (no dependency on a pre-existing table).
        silentExecute("DROP TABLE " + smallTable());
        job.execute(
                "CREATE TABLE " + smallTable()
                + " ( ID INTEGER GENERATED ALWAYS AS IDENTITY, JBLOB BLOB(1024) )"
        ).get();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        if (job != null) {
            silentExecute("DROP TABLE " + smallTable());
            job.close();
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /**
     * Execute SQL silently, ignoring any errors (e.g. table does not exist).
     */
    private static void silentExecute(String sql) {
        try {
            job.execute(sql).get();
        } catch (Exception ignored) {
            // intentionally ignored — table may not exist or SQL may fail
        }
    }

    /**
     * Execute a raw HTTPS GET to the mapepire server using the provided password.
     * Returns the HTTP status code.
     */
    private static int httpGetStatus(String path, String password) throws Exception {
        String auth = creds.getUser() + ":" + password;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        URL url = new URL("https://" + creds.getHost() + ":" + creds.getPort() + path);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(30_000);
        conn.connect();

        int status = conn.getResponseCode();
        // Drain response
        InputStream drain = (status == 200) ? conn.getInputStream() : conn.getErrorStream();
        if (drain != null) {
            try (InputStream in = drain) {
                byte[] buf = new byte[1024];
                int bytesRead = in.read(buf);
                while (bytesRead != -1) {
                    bytesRead = in.read(buf);
                }
            }
        }
        return status;
    }

    /**
     * Retrieve a BlobRef from the small table (first non-null row).
     *
     * @return A fresh BlobRef from the first non-null BLOB row.
     */
    private BlobRef freshSmallBlobRef() throws Exception {
        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL FETCH FIRST 1 ROW ONLY"
        ).get();
        assertTrue(res.getSuccess());
        assertTrue(res.getData().size() >= 1, "Expected at least one non-null BLOB row");
        Object raw = toMap(res.getData().get(0)).get("JBLOB");
        assertNotNull(raw, "JBLOB cell should not be null");
        return mapToBlobRef(raw);
    }

    /**
     * Jackson deserialises the BlobRef fields into a Map when the generic type is
     * {@code Object}. Convert it to a proper {@link BlobRef}.
     *
     * @param raw The raw object from the query result.
     * @return A {@link BlobRef} built from the raw map.
     */
    private static BlobRef mapToBlobRef(Object raw) {
        Map<String, Object> map = toMap(raw);
        BlobRef ref = new BlobRef();
        ref.setBlobUrl((String) map.get("blob_url"));
        Object sizeObj = map.get("size");
        if (sizeObj instanceof Number) {
            ref.setSize(((Number) sizeObj).longValue());
        }
        return ref;
    }

    /**
     * Cast an arbitrary object to a typed map — helper to avoid raw-type and
     * {@code LinkedHashMap} checkstyle warnings.
     *
     * @param obj The object to cast.
     * @return The object cast as a {@code Map<String, Object>}.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Object obj) {
        return (Map<String, Object>) obj;
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    /** 1. INSERT BLOB via prepared-statement parameter */
    @Test
    @Order(1)
    void test1_insertBlobViaPreparedStatement() throws Exception {
        QueryOptions opts = new QueryOptions(false, false, Arrays.asList(TEST_BASE64));
        QueryResult<Object> res = job.execute(
                "INSERT INTO " + smallTable() + " (JBLOB) VALUES (?)", opts).get();

        assertTrue(res.getSuccess());
        assertEquals(1, res.getUpdateCount());
    }

    /** 2. SELECT BLOB column returns BlobRef with blob_url and size */
    @Test
    @Order(2)
    void test2_selectBlobColumnReturnsBlobRef() throws Exception {
        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL").get();

        assertTrue(res.getSuccess());
        assertTrue(res.getData().size() >= 1);

        Object raw = toMap(res.getData().get(0)).get("JBLOB");
        assertNotNull(raw);
        BlobRef ref = mapToBlobRef(raw);

        assertNotNull(ref.getBlobUrl());
        assertTrue(ref.getBlobUrl().startsWith("/blob/"),
                "blob_url should start with /blob/ but was: " + ref.getBlobUrl());
        assertEquals(TEST_STRING.length(), ref.getSize());
    }

    /** 3. fetchBlob() returns correct raw bytes */
    @Test
    @Order(3)
    void test3_fetchBlobReturnsCorrectBytes() throws Exception {
        BlobRef ref = freshSmallBlobRef();
        byte[] bytes = job.fetchBlob(ref).get();

        assertNotNull(bytes);
        assertEquals(TEST_STRING.length(), bytes.length);
        assertEquals(TEST_STRING, new String(bytes, StandardCharsets.UTF_8));
    }

    /** 4. fetchBlob() a second time on the same token throws 404 (single-use) */
    @Test
    @Order(4)
    void test4_fetchBlobSecondTimeThrows404() throws Exception {
        BlobRef ref = freshSmallBlobRef();

        // First fetch — succeeds
        job.fetchBlob(ref).get();

        // Second fetch — must fail with a 404 message
        try {
            job.fetchBlob(ref).get();
            fail("Expected ExecutionException for 404");
        } catch (ExecutionException ex) {
            String msg = ex.getCause().getMessage();
            assertTrue(msg.contains("404"), "Expected 404 in error message but got: " + msg);
        }
    }

    /** 5. HTTP GET with wrong password returns 401 */
    @Test
    @Order(5)
    void test5_httpGetWithWrongPasswordReturns401() throws Exception {
        BlobRef ref = freshSmallBlobRef();
        int status = httpGetStatus(ref.getBlobUrl(), "wrongpassword");
        assertEquals(401, status);
    }

    /** 6. SELECT NULL BLOB column returns null */
    @Test
    @Order(6)
    void test6_selectNullBlobReturnsNull() throws Exception {
        // Insert a NULL row
        job.execute("INSERT INTO " + smallTable() + " (JBLOB) VALUES (NULL)").get();

        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NULL").get();

        assertTrue(res.getSuccess());
        assertTrue(res.getData().size() >= 1);

        Object nullField = toMap(res.getData().get(0)).get("JBLOB");
        assertNull(nullField, "Expected JBLOB to be null");
    }

    /** 7. Large BLOB (&gt;1 MB) — async spool: blob_url returned quickly, bytes correct */
    @Test
    @Order(7)
    @Timeout(120)
    void test7_largeBlobRoundTrip() throws Exception {
        silentExecute("DROP TABLE " + largeTable());
        job.execute("CREATE TABLE " + largeTable() + " ( JBLOB BLOB(10485760) )").get();

        try {
            // INSERT the large BLOB
            QueryOptions insOpts = new QueryOptions(false, false, Arrays.asList(LARGE_BLOB_BASE64));
            QueryResult<Object> insRes = job.execute(
                    "INSERT INTO " + largeTable() + " (JBLOB) VALUES (?)", insOpts).get();
            assertTrue(insRes.getSuccess());
            assertEquals(1, insRes.getUpdateCount());

            // SELECT — the response should arrive before all bytes are spooled
            long t0 = System.currentTimeMillis();
            QueryResult<Object> selRes = job.execute(
                    "SELECT JBLOB FROM " + largeTable() + " WHERE JBLOB IS NOT NULL").get();
            long selElapsed = System.currentTimeMillis() - t0;

            assertTrue(selRes.getSuccess());
            assertTrue(selRes.getData().size() >= 1);

            BlobRef ref = mapToBlobRef(
                    toMap(selRes.getData().get(0)).get("JBLOB"));
            assertNotNull(ref);
            assertTrue(ref.getBlobUrl().startsWith("/blob/"));
            assertEquals(LARGE_BLOB_SIZE, ref.getSize());

            // The SELECT response should be "quick" — under 10 s even with async spool
            assertTrue(selElapsed < 10_000,
                    "SELECT took too long (" + selElapsed + " ms); expected < 10 000 ms");

            // Fetch the raw bytes — fetchBlob waits for spool to finish if needed
            byte[] bytes = job.fetchBlob(ref).get();

            assertEquals(LARGE_BLOB_SIZE, bytes.length);
            for (int i = 0; i < bytes.length; i++) {
                assertEquals((byte) 0x41, bytes[i],
                        "Byte at index " + i + " should be 0x41 but was " + bytes[i]);
            }
        } finally {
            silentExecute("DROP TABLE " + largeTable());
        }
    }

    /** 8. Binary fidelity — all 256 byte values (0x00–0xFF) round-trip correctly */
    @Test
    @Order(8)
    void test8_binaryFidelity() throws Exception {
        silentExecute("DROP TABLE " + binaryTable());
        job.execute("CREATE TABLE " + binaryTable() + " ( JBLOB BLOB(1024) )").get();

        try {
            QueryOptions insOpts = new QueryOptions(false, false, Arrays.asList(BINARY_BASE64));
            QueryResult<Object> insRes = job.execute(
                    "INSERT INTO " + binaryTable() + " (JBLOB) VALUES (?)", insOpts).get();
            assertTrue(insRes.getSuccess());
            assertEquals(1, insRes.getUpdateCount());

            QueryResult<Object> selRes = job.execute(
                    "SELECT JBLOB FROM " + binaryTable() + " WHERE JBLOB IS NOT NULL").get();
            assertTrue(selRes.getSuccess());
            assertEquals(1, selRes.getData().size());

            BlobRef ref = mapToBlobRef(
                    toMap(selRes.getData().get(0)).get("JBLOB"));
            assertNotNull(ref);
            assertEquals(BINARY_BUF.length, ref.getSize());

            byte[] bytes = job.fetchBlob(ref).get();
            assertEquals(BINARY_BUF.length, bytes.length);

            // Verify every byte value 0x00–0xFF is preserved exactly
            for (int i = 0; i < 256; i++) {
                assertEquals((byte) i, bytes[i],
                        "Byte at index " + i + " should be " + i + " but was " + (bytes[i] & 0xFF));
            }
        } finally {
            silentExecute("DROP TABLE " + binaryTable());
        }
    }

    /** 9. Multiple rows in a single SELECT — each row has a distinct blob_url */
    @Test
    @Order(9)
    void test9_multipleRowsHaveDistinctBlobUrls() throws Exception {
        // Insert two more rows with different content
        String second = Base64.getEncoder()
                .encodeToString("second blob value".getBytes(StandardCharsets.UTF_8));
        String third  = Base64.getEncoder()
                .encodeToString("third blob value".getBytes(StandardCharsets.UTF_8));

        job.execute("INSERT INTO " + smallTable() + " (JBLOB) VALUES (?)",
                new QueryOptions(false, false, Arrays.asList(second))).get();
        job.execute("INSERT INTO " + smallTable() + " (JBLOB) VALUES (?)",
                new QueryOptions(false, false, Arrays.asList(third))).get();

        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL").get();

        assertTrue(res.getSuccess());
        assertTrue(res.getData().size() >= 3);

        List<String> urls = res.getData().stream()
                .map(row -> {
                    Object raw = toMap(row).get("JBLOB");
                    assertNotNull(raw);
                    BlobRef ref = mapToBlobRef(raw);
                    assertNotNull(ref.getBlobUrl());
                    assertTrue(ref.getBlobUrl().length() > 0);
                    return ref.getBlobUrl();
                })
                .collect(java.util.stream.Collectors.toList());

        // All URLs must be unique
        long uniqueCount = urls.stream().distinct().count();
        assertEquals(urls.size(), uniqueCount, "All blob_url tokens should be unique");
    }

    /** 10. Concurrent fetchBlob() of two tokens — both succeed independently */
    @Test
    @Order(10)
    void test10_concurrentFetchBlob() throws Exception {
        // Obtain two fresh tokens from two separate SELECTs
        CompletableFuture<QueryResult<Object>> sel1Future = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL FETCH FIRST 1 ROW ONLY");
        CompletableFuture<QueryResult<Object>> sel2Future = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL FETCH FIRST 1 ROW ONLY");
        CompletableFuture.allOf(sel1Future, sel2Future).get();

        BlobRef ref1 = mapToBlobRef(
                toMap(sel1Future.get().getData().get(0)).get("JBLOB"));
        BlobRef ref2 = mapToBlobRef(
                toMap(sel2Future.get().getData().get(0)).get("JBLOB"));

        // Two different tokens may point to the same logical blob but must have distinct URLs
        assertNotEquals(ref1.getBlobUrl(), ref2.getBlobUrl());

        // Fetch both concurrently
        CompletableFuture<byte[]> fetch1 = job.fetchBlob(ref1);
        CompletableFuture<byte[]> fetch2 = job.fetchBlob(ref2);
        CompletableFuture.allOf(fetch1, fetch2).get();

        byte[] bytes1 = fetch1.get();
        byte[] bytes2 = fetch2.get();

        assertNotNull(bytes1);
        assertNotNull(bytes2);
        assertTrue(bytes1.length > 0);
        assertTrue(bytes2.length > 0);
    }

    /** 11. Column metadata reports the BLOB column type correctly */
    @Test
    @Order(11)
    void test11_columnMetadataReportsBlobType() throws Exception {
        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL FETCH FIRST 1 ROW ONLY").get();

        assertTrue(res.getSuccess());
        assertNotNull(res.getMetadata());
        assertNotNull(res.getMetadata().getColumns());
        assertTrue(res.getMetadata().getColumns().size() > 0);

        String colName = res.getMetadata().getColumns().get(0).getName().toUpperCase();
        String colType = res.getMetadata().getColumns().get(0).getType().toUpperCase();

        assertEquals("JBLOB", colName);
        assertTrue(colType.contains("BLOB"),
                "Column type should contain 'BLOB' but was: " + colType);

        // Consume the token so it doesn't linger in the store
        try {
            Object raw = toMap(res.getData().get(0)).get("JBLOB");
            if (raw != null) {
                job.fetchBlob(mapToBlobRef(raw)).get();
            }
        } catch (Exception ignored) {
        }
    }

    /** 12. UPDATE BLOB value — SELECT after UPDATE returns new content */
    @Test
    @Order(12)
    void test12_updateBlobValue() throws Exception {
        String origContent = "original blob content";
        String origBase64  = Base64.getEncoder()
                .encodeToString(origContent.getBytes(StandardCharsets.UTF_8));
        String newContent  = "updated blob content";
        String newBase64   = Base64.getEncoder()
                .encodeToString(newContent.getBytes(StandardCharsets.UTF_8));

        silentExecute("DROP TABLE " + updateTable());
        job.execute("CREATE TABLE " + updateTable()
                + " ( ID INTEGER GENERATED ALWAYS AS IDENTITY, JBLOB BLOB(1024) )").get();

        try {
            // INSERT the original row
            job.execute("INSERT INTO " + updateTable() + " (JBLOB) VALUES (?)",
                    new QueryOptions(false, false, Arrays.asList(origBase64))).get();

            // Retrieve the auto-generated ID
            QueryResult<Object> idRes = job.execute(
                    "SELECT ID FROM " + updateTable() + " FETCH FIRST 1 ROW ONLY").get();
            assertTrue(idRes.getSuccess());
            int rowId = ((Number) toMap(idRes.getData().get(0)).get("ID")).intValue();

            // UPDATE the BLOB
            QueryResult<Object> updRes = job.execute(
                    "UPDATE " + updateTable() + " SET JBLOB = ? WHERE ID = ?",
                    new QueryOptions(false, false, Arrays.asList(newBase64, rowId))).get();
            assertTrue(updRes.getSuccess());
            assertEquals(1, updRes.getUpdateCount());

            // SELECT the updated row
            QueryResult<Object> selRes = job.execute(
                    "SELECT ID, JBLOB FROM " + updateTable() + " WHERE ID = ?",
                    new QueryOptions(false, false, Arrays.asList(rowId))).get();
            assertTrue(selRes.getSuccess());
            assertEquals(1, selRes.getData().size());

            BlobRef ref = mapToBlobRef(
                    toMap(selRes.getData().get(0)).get("JBLOB"));
            assertNotNull(ref);
            assertEquals(newContent.length(), ref.getSize());

            byte[] bytes = job.fetchBlob(ref).get();
            assertEquals(newContent, new String(bytes, StandardCharsets.UTF_8));
        } finally {
            silentExecute("DROP TABLE " + updateTable());
        }
    }

    /** 13. fetchBlob() on an unconnected job throws a "not connected" error */
    @Test
    @Order(13)
    void test13_fetchBlobOnUnconnectedJobThrows() throws Exception {
        SqlJob disconnectedJob = new SqlJob();
        // Do NOT call connect() — db2Server remains null

        BlobRef fakeRef = new BlobRef("/blob/fake-token", 1);
        try {
            disconnectedJob.fetchBlob(fakeRef).get();
            fail("Expected ExecutionException for unconnected job");
        } catch (ExecutionException ex) {
            String msg = ex.getCause().getMessage().toLowerCase();
            assertTrue(msg.contains("not connected"),
                    "Expected 'not connected' in error message but got: " + ex.getCause().getMessage());
        }
    }

    /** 14. HTTP GET with a completely bogus token returns 404 */
    @Test
    @Order(14)
    void test14_httpGetWithBogusTokenReturns404() throws Exception {
        int status = httpGetStatus("/blob/00000000-0000-0000-0000-000000000000",
                creds.getPassword());
        assertEquals(404, status);
    }

    /** 15. blob_url token portion is a valid UUID (RFC 4122) */
    @Test
    @Order(15)
    void test15_blobUrlTokenIsValidUuid() throws Exception {
        BlobRef ref = freshSmallBlobRef();
        assertTrue(ref.getBlobUrl().startsWith("/blob/"),
                "blob_url should start with /blob/ but was: " + ref.getBlobUrl());

        String token = ref.getBlobUrl().substring("/blob/".length());
        assertTrue(UUID_RE.matcher(token).matches(),
                "Token should be a valid RFC 4122 UUID but was: " + token);

        // Consume the token
        try {
            job.fetchBlob(ref).get();
        } catch (Exception ignored) {
            // token already consumed or expired — ignore
        }
    }

    /** 16. BlobRef is returned correctly in terse-results mode. */
    @Test
    @Order(16)
    void test16_blobRefInTerseResultsMode() throws Exception {
        QueryOptions opts = new QueryOptions(true, false, null);
        QueryResult<Object> res = job.execute(
                "SELECT JBLOB FROM " + smallTable() + " WHERE JBLOB IS NOT NULL FETCH FIRST 1 ROW ONLY",
                opts).get();

        assertTrue(res.getSuccess());
        assertTrue(res.getData().size() >= 1);

        // In terse mode data is a list of arrays; get the first element of the first row
        Object rowValue;
        Object firstRow = res.getData().get(0);
        if (firstRow instanceof List) {
            rowValue = ((List<?>) firstRow).get(0);
        } else {
            rowValue = toMap(firstRow).get("JBLOB");
        }

        assertNotNull(rowValue, "BlobRef should not be null in terse mode");
        BlobRef ref = mapToBlobRef(rowValue);
        assertTrue(ref.getBlobUrl().startsWith("/blob/"),
                "blob_url should start with /blob/ but was: " + ref.getBlobUrl());
        assertTrue(ref.getSize() > 0, "BlobRef size should be > 0");

        // Consume the token
        try {
            job.fetchBlob(ref).get();
        } catch (Exception ignored) {
            // token already consumed or expired — ignore
        }
    }
}
