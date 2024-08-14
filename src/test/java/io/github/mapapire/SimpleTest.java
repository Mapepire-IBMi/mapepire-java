package io.github.mapapire;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.mapapire.types.DaemonServer;
import io.github.mapapire.types.QueryResult;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class SimpleTest {
    private static DaemonServer creds;

    @BeforeAll
    public static void setup() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = SimpleTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Unable to find config.properties");
            }
            properties.load(input);
        }

        String host = properties.getProperty("IBMI_HOST");
        String user = properties.getProperty("IBMI_USER");
        String password = properties.getProperty("IBMI_PASSWORD");
        int port = Integer.parseInt(properties.getProperty("IBMI_PORT"));

        creds = new DaemonServer(host, port, user, password, true, "");

        // TODO: Get certificate
    }

    @Test
    void simpleTest() {
        SqlJob job = new SqlJob();
        try {
            boolean isConnected = job.connect(creds).get().getSuccess();
            if (!isConnected) {
                return;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        Query<Object> query = job.query("select * from sample.department");
        QueryResult<Object> result;
        try {
            result = query.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        assertTrue(result.getIsDone());
        assertTrue(result.getData().size() > 0);

        job.close();
    }
}
