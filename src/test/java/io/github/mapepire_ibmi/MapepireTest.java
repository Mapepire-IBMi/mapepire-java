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
    private static String host;
    private static String user;
    private static String password;
    private static int port;
    private static String invalidCA = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDtjCCAp6gAwIBAgIUaZwqO1YXrSGUZ+j2YlefGD+Li3UwDQYJKoZIhvcNAQEL\n" +
            "BQAwcjELMAkGA1UEBhMCZHIxCzAJBgNVBAgMAnNlMQwwCgYDVQQHDAN0eXUxDDAK\n" +
            "BgNVBAoMA3NlcjELMAkGA1UECwwCaGYxDzANBgNVBAMMBmRyZnRnZzEcMBoGCSqG\n" +
            "SIb3DQEJARYNbG9nQGdtYWlsLmNvbTAeFw0yNDEwMDIxODU3MDFaFw0yNTEwMDIx\n" +
            "ODU3MDFaMHQxCzAJBgNVBAYTAkNBMQswCQYDVQQIDAJPTjEQMA4GA1UEBwwHVG9y\n" +
            "b250bzEMMAoGA1UECgwDSUJNMQswCQYDVQQLDAJMTDEMMAoGA1UEAwwDSk9OMR0w\n" +
            "GwYJKoZIhvcNAQkBFg5mYWtlQGdtYWlsLmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\n" +
            "ggEPADCCAQoCggEBAI39BFoVAXIc0HFH7MgDAI53ExKgZkyBXGjFsooyzM/u215h\n" +
            "QLtsfirmsFU8Kq2HOY1UaB1PRq4nrHYrwO7nYpmA14/9EgEXQsSRDH8LO18sSUQb\n" +
            "hOX3622CunKDT77O7z5LakPuVvOsj1XBVTyFONTTzcuWW0mcnupj7j+WF+fldV3y\n" +
            "Z64rfk/wJ2W1FjWNgxtm076KivVrV4RvL7DmGQH5sCENCx4eaGh2LGe1kb5yACQ3\n" +
            "9zeC9aEwogEh2QRV8x3LzsroF3NR/IqzIm6L3kaiyWTsQkVlztmGpXY3WnFgfoBj\n" +
            "e6IZOCRXzA9iTS1dRDGnFSzcRawf+PSIbP88LZ0CAwEAAaNCMEAwHQYDVR0OBBYE\n" +
            "FH1B2+PDJmga5MwzswnukmSEt50OMB8GA1UdIwQYMBaAFPHGkLtIDz/tR3iBLgzU\n" +
            "DjEK+tsoMA0GCSqGSIb3DQEBCwUAA4IBAQCnlEjRBF+IUNRfYVqOW4uHJriaBViu\n" +
            "6zdXG+13pa7La+mAZ0BsoP1pLhrWjDul271MTOYsq429XBtlfxaNJiqHuPNjccKa\n" +
            "wga2NFLAZriHYUvyP4Ld/H0IVAleIem4w2vwqHqayV2GeQCn5H+LknIaTzHKuRZ9\n" +
            "fv6C/V5jBJFAJ29tYh79lioIRIZ6nzYLGWQIXbh9Y8uNIMbU3z4fqRQN65gKCkBB\n" +
            "HaelrFfJI+UCGwOnr4qTKxkEB/lNz47O7kh4vmAk4mU3IsSWDMsydFHCTPLMg/Me\n" +
            "TYn5iFqPQJhDoSiE8W0CeyAUXyhwWg7l9qiBaA+nI+t1Y307ld4T46x4\n" +
            "-----END CERTIFICATE-----";
    private static String configFile = "config.properties";

    @BeforeAll
    public static void setup() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = MapepireTest.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find " + configFile);
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

        host = secrets.get(keys.get(0));
        user = secrets.get(keys.get(1));
        password = secrets.get(keys.get(2));
        port = Integer.parseInt(secrets.get(keys.get(3)));
    }

    public static DaemonServer getCreds() throws Exception {
        DaemonServer creds = new DaemonServer(host, port, user, password);
        String ca = Tls.getCertificate(creds).get();
        creds.setCa(ca);
        return creds;
    }

    public static DaemonServer getInvalidCreds() {
        return new DaemonServer(host, port, "FAKE_USER", "FAKE_PASSWORD", false, invalidCA);
    }
}
