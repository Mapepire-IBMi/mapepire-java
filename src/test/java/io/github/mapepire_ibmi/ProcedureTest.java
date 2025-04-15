package io.github.mapepire_ibmi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.mapepire_ibmi.types.QueryOptions;
import io.github.mapepire_ibmi.types.QueryResult;

class ProcedureTest extends MapepireTest {
    @BeforeAll
    public static void beforeAll() throws Exception {
        MapepireTest.setupCreds();
        MapepireTest.setupTestSchema();
    }

    @Test
    void numberParameters() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        String testProc = String.join("\n", Arrays.asList(
                "CREATE OR REPLACE PROCEDURE " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST("
                        + "  IN P1 INTEGER,"
                        + "  INOUT P2 INTEGER,"
                        + "  OUT P3 INTEGER"
                        + ")"
                        + "BEGIN"
                        + "  SET P3 = P1 + P2;"
                        + "  SET P2 = 0;"
                        + "END"));
        Query queryA = job.query(testProc);
        queryA.execute().get();
        queryA.close().get();

        QueryOptions options = new QueryOptions(false, false, Arrays.asList(6, 4, 0));
        Query queryB = job.query("CALL " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST(?, ?, ?)", options);
        QueryResult<Object> result = queryB.execute().get();
        queryB.close().get();

        job.close();

        assertNotNull(result.getMetadata().getParameters());
        assertEquals(3, result.getMetadata().getParameters().size());
        List<String> inParmNames = result.getMetadata().getParameters().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> inParmTypes = result.getMetadata().getParameters().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), inParmNames.toArray());
        assertArrayEquals(Arrays.asList("INTEGER", "INTEGER", "INTEGER").toArray(), inParmTypes.toArray());

        assertTrue(result.getSuccess());
        assertFalse(result.getHasResults());
        assertEquals(3, result.getParameterCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getData().size());

        assertNotNull(result.getOutputParms());
        assertEquals(3, result.getOutputParms().size());
        List<String> outParmNames = result.getOutputParms().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> outParmTypes = result.getOutputParms().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        List<Object> outParmValues = result.getOutputParms().stream().map(p -> p.getValue())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), outParmNames.toArray());
        assertArrayEquals(Arrays.asList("INTEGER", "INTEGER", "INTEGER").toArray(), outParmTypes.toArray());
        assertArrayEquals(Arrays.asList(null, 0, 10).toArray(), outParmValues.toArray());
    }

    @Test
    void charParameters() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        String testProc = String.join("\n", Arrays.asList(
                "CREATE OR REPLACE PROCEDURE " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST_CHAR("
                        + "  IN P1 CHAR(5),"
                        + "  INOUT P2 CHAR(6),"
                        + "  OUT P3 CHAR(7)"
                        + ")"
                        + "BEGIN"
                        + "  SET P3 = RTRIM(P1) concat RTRIM(P2);"
                        + "  SET P2 = '';"
                        + "END"));
        Query queryA = job.query(testProc);
        queryA.execute().get();
        queryA.close().get();

        QueryOptions options = new QueryOptions(false, false, Arrays.asList("a", "b", ""));
        Query queryB = job.query("CALL " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST_CHAR(?, ?, ?)", options);
        QueryResult<Object> result = queryB.execute().get();
        queryB.close().get();

        job.close();

        assertNotNull(result.getMetadata().getParameters());
        assertEquals(3, result.getMetadata().getParameters().size());
        List<String> inParmNames = result.getMetadata().getParameters().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> inParmTypes = result.getMetadata().getParameters().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        List<Integer> inParmPrecisions = result.getMetadata().getParameters().stream().map(p -> p.getPrecision())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), inParmNames.toArray());
        assertArrayEquals(Arrays.asList("CHAR", "CHAR", "CHAR").toArray(), inParmTypes.toArray());
        assertArrayEquals(Arrays.asList(5, 6, 7).toArray(), inParmPrecisions.toArray());

        assertTrue(result.getSuccess());
        assertFalse(result.getHasResults());
        assertEquals(3, result.getParameterCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getData().size());

        assertNotNull(result.getOutputParms());
        assertEquals(3, result.getOutputParms().size());
        List<String> outParmNames = result.getOutputParms().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> outParmTypes = result.getOutputParms().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        List<Object> outParmValues = result.getOutputParms().stream().map(p -> p.getValue())
                .collect(Collectors.toList());
        List<Object> outParmPrecisions = result.getOutputParms().stream().map(p -> p.getPrecision())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), outParmNames.toArray());
        assertArrayEquals(Arrays.asList("CHAR", "CHAR", "CHAR").toArray(), outParmTypes.toArray());
        assertArrayEquals(Arrays.asList(null, "", "ab").toArray(), outParmValues.toArray());
        assertArrayEquals(Arrays.asList(5, 6, 7).toArray(), outParmPrecisions.toArray());
    }

    @Test
    void varcharParameters() throws Exception {
        SqlJob job = new SqlJob();
        job.connect(MapepireTest.getCreds()).get();

        String testProc = String.join("\n", Arrays.asList(
                "CREATE OR REPLACE PROCEDURE " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST_VARCHAR("
                        + "  IN P1 VARCHAR(5),"
                        + "  INOUT P2 VARCHAR(6),"
                        + "  OUT P3 VARCHAR(7)"
                        + ")"
                        + "BEGIN"
                        + "  SET P3 = P1 concat P2;"
                        + "  SET P2 = '';"
                        + "END"));
        Query queryA = job.query(testProc);
        queryA.execute().get();
        queryA.close().get();

        QueryOptions options = new QueryOptions(false, false, Arrays.asList("a", "b", "c"));
        Query queryB = job.query("CALL " + MapepireTest.getTestSchema() + ".PROCEDURE_TEST_VARCHAR(?, ?, ?)", options);
        QueryResult<Object> result = queryB.execute().get();
        queryB.close().get();

        job.close();

        assertNotNull(result.getMetadata().getParameters());
        assertEquals(3, result.getMetadata().getParameters().size());
        List<String> inParmNames = result.getMetadata().getParameters().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> inParmTypes = result.getMetadata().getParameters().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        List<Integer> inParmPrecisions = result.getMetadata().getParameters().stream().map(p -> p.getPrecision())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), inParmNames.toArray());
        assertArrayEquals(Arrays.asList("VARCHAR", "VARCHAR", "VARCHAR").toArray(), inParmTypes.toArray());
        assertArrayEquals(Arrays.asList(5, 6, 7).toArray(), inParmPrecisions.toArray());

        assertTrue(result.getSuccess());
        assertFalse(result.getHasResults());
        assertEquals(3, result.getParameterCount());
        assertEquals(0, result.getUpdateCount());
        assertEquals(0, result.getData().size());

        assertNotNull(result.getOutputParms());
        assertEquals(3, result.getOutputParms().size());
        List<String> outParmNames = result.getOutputParms().stream().map(p -> p.getName())
                .collect(Collectors.toList());
        List<String> outParmTypes = result.getOutputParms().stream().map(p -> p.getType())
                .collect(Collectors.toList());
        List<Object> outParmValues = result.getOutputParms().stream().map(p -> p.getValue())
                .collect(Collectors.toList());
        List<Object> outParmPrecisions = result.getOutputParms().stream().map(p -> p.getPrecision())
                .collect(Collectors.toList());
        assertArrayEquals(Arrays.asList("P1", "P2", "P3").toArray(), outParmNames.toArray());
        assertArrayEquals(Arrays.asList("VARCHAR", "VARCHAR", "VARCHAR").toArray(), outParmTypes.toArray());
        assertArrayEquals(Arrays.asList(null, "", "ab").toArray(), outParmValues.toArray());
        assertArrayEquals(Arrays.asList(5, 6, 7).toArray(), outParmPrecisions.toArray());
    }
}
