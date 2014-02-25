package com.lognet.protojpa.testdb;


import org.hsqldb.Server;
import org.hsqldb.cmdline.SqlFile;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by alexf on 2/25/14.
 */
public  class TestDB {
    private static Server hsqlServer = null;
    public static void start() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");

        hsqlServer = new Server();
        //    hsqlServer.setLogWriter(null);
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, "xdb");
        hsqlServer.setDatabasePath(0, "file:testdb/db");
        hsqlServer.start();

        final SqlFile sqlFile = new SqlFile(getResourceFile("sampledata.sql"));
        sqlFile.setAutoClose(true);
        sqlFile.setConnection(createConnection());
        sqlFile.execute();
    }
    public static void stop(){
        if (hsqlServer != null) {
            hsqlServer.stop();
        }
    }
    private static Connection createConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
    }
    private static File getResourceFile(String resourceName) throws URISyntaxException {
        return new File(ClassLoader.getSystemClassLoader().getResource(resourceName).toURI());
    }
}
