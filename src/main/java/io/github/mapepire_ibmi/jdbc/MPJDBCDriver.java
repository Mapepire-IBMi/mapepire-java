package io.github.mapepire_ibmi.jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import io.github.mapepire_ibmi.SqlJob;
import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.exceptions.UnknownServerException;

public class MPJDBCDriver implements Driver {

    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 1;
    private Logger s_parentLogger = Logger.getLogger(MPJDBCDriver.class.getCanonicalName());

    @Override
    public Connection connect(String _url, Properties _info) throws SQLException {
        System.err.println("connection url:" + _url);
        try {

            final Properties p = new Properties();
            for (Object prop : Collections.list(_info.propertyNames())) {
                if (prop instanceof CharSequence) {
                    p.put(prop.toString().toUpperCase(), _info.get(prop));
                } else {
                    p.put(prop, _info.get(prop));
                }
            }
            final String propertiesFromConnectionString = _url.contains(";") ? _url.replaceFirst("^[^;]*;", "") : "";
            Properties connStringProps = new Properties();
            connStringProps.load(new StringReader(propertiesFromConnectionString.replace(';', '\n')));
            for (Object prop : Collections.list(connStringProps.propertyNames())) {
                if (prop instanceof CharSequence) {
                    p.put(prop.toString().toUpperCase(), connStringProps.get(prop));
                } else {
                    p.put(prop, connStringProps.get(prop));
                }
            }
            Object userIdObject = p.remove("UID"); // TODO: null-check
            String userId = userIdObject.toString();
            Object pwdObject = p.remove("PWD");
            String password = pwdObject.toString();
            String hostname = "osshaha";
            int port = 8087;
            DaemonServer server = new DaemonServer(hostname, port, userId, password, true, null);

            JDBCOptions options = new JDBCOptions(p);
            SqlJob job = new SqlJob(options);

            job.connect(server);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | InterruptedException
                | ExecutionException | URISyntaxException | UnknownServerException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean acceptsURL(String _url) throws SQLException {
        return _url.toLowerCase().startsWith("mapepire:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];// TODO: return from AS400JDBCDriver
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;// TODO: how to pass these tests?
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return s_parentLogger;
    }

    public static void main(String[] args) {
        try {
            DriverManager.registerDriver(new MPJDBCDriver());
            Connection c = DriverManager.getConnection(
                    "jdbc:mapepire://oss73dev.rch.stglabs.ibm.com;UID=Linux;PWD=linux1;naming=system;errors=full");
            System.out.println("done");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
