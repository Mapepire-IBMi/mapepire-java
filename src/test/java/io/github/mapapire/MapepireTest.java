package io.github.mapapire;

import io.github.mapapire.types.DaemonServer;

import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;

import com.fasterxml.jackson.databind.ObjectMapper;

@Timeout(5)
class MapepireTest {
    private static final String CONFIG_FILE = "config.properties";
    public static DaemonServer creds;
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = MapepireTest.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        }

        List<String> keys = Arrays.asList("IBMI_HOST", "IBMI_USER", "IBMI_PASSWORD", "IBMI_PORT");
        Map<String, String> secrets = new HashMap<>();
        for (String key : keys) {
            String value = properties.getProperty(key);
            if (value.equals("")) {
                throw new Exception(key + " not set in config.properties");
            }
            secrets.put(key, value);
        }

        String host = secrets.get(keys.get(0));
        String user = secrets.get(keys.get(1));
        String password = secrets.get(keys.get(2));
        int port = Integer.parseInt(secrets.get(keys.get(3)));

        creds = new DaemonServer(host, port, user, password, true, "");

        // TODO: Get certificate
    }
}
