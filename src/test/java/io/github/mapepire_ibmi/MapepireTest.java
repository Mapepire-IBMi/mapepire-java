package io.github.mapepire_ibmi;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;

import io.github.mapepire_ibmi.types.DaemonServer;

@Timeout(25)
class MapepireTest {
    private static DaemonServer creds;
    private static DaemonServer invalidCreds;
    private static final String CONFIG_FILE = "config.properties";

    @BeforeAll
    public static void setup() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = MapepireTest.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        }

        List<String> keys = Arrays.asList("IBMI_HOST", "IBMI_USER", "IBMI_PASSWORD", "IBMI_PORT");
        Map<String, String> secrets = new HashMap<>();
        for (String key : keys) {
            String value = properties.getProperty(key);
            if (value.equals("")) {
                throw new ParseException(key + " not set in config.properties", 0);
            }
            secrets.put(key, value);
        }

        String host = secrets.get(keys.get(0));
        String user = secrets.get(keys.get(1));
        String password = secrets.get(keys.get(2));
        int port = Integer.parseInt(secrets.get(keys.get(3)));

        creds = new DaemonServer(host, port, user, password);
        String ca = Tls.getCertificate(creds).get();
        creds.setCa(ca);
        invalidCreds = new DaemonServer(host, port, "FAKE_USER", "FAKE_PASSWORD", false);
    }

    public static DaemonServer getCreds() {
        return creds;
    }

    public static DaemonServer getInvalidCreds() {
        return invalidCreds;
    }
}
