package com.lognet.protojpa.testdb;


import org.hsqldb.Server;
import org.hsqldb.cmdline.SqlFile;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by alexf on 2/25/14.
 */
public  class TestDB {

    public static void main(String[] args) {
        try {
            if("-populate".equalsIgnoreCase(args[0])){
                populate();
            }else if("-stop".equalsIgnoreCase(args[0])){
                createConnection().prepareCall("SHUTDOWN").execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private static Server hsqlServer = null;

    public static void start() throws Exception {

        Class.forName("org.hsqldb.jdbcDriver");

        hsqlServer = new Server();
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, "xdb");
        hsqlServer.setDatabasePath(0, "file:testdb/db");
        hsqlServer.start();
        populate();

    }
    public static void populate() throws Exception {
        final InputStream sqlIn = ClassLoader.getSystemClassLoader().getResourceAsStream("sampledata.sql");
        try{

            System.setIn(sqlIn);
            final SqlFile sqlFile = new SqlFile("UTF-8",false);

            sqlFile.setAutoClose(true);
            sqlFile.setConnection(createConnection());
            sqlFile.execute();
        }finally {
            System.setIn(System.in);
            if(null!=sqlIn){
                sqlIn.close();
            }

        }
    }
    public static void stop(){
        if (hsqlServer != null) {
            hsqlServer.stop();
        }
    }
    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
    }

}
