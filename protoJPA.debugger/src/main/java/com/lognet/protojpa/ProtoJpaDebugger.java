package com.lognet.protojpa;


import com.lognet.protojpa.testdb.TestDB;
import org.junit.*;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by alexf on 2/13/14.
 */
public class ProtoJpaDebugger {

    private Connection connection = null;
    @BeforeClass
    public static void startDB() throws  Exception {
        TestDB.start();
    }
     @AfterClass
     public static void shutDownDB(){
         TestDB.stop();
     }
    @Before
    public void setup() throws ClassNotFoundException, SQLException {
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
    }
    @After
    public void tearDown() throws SQLException {
        connection.close();
    }


    @Test
    public void simpleTest() throws URISyntaxException {
         new HibernateAntToolLauncher().run(getResourceFile("hibernate.reveng.xml"),
                 getResourceFile("hibernate.cfg.xml"),
                 new File("generatedClasses"),
                 new File("generatedResources"),
                 new File("C:\\protoc-2.5.0-win32\\protoc.exe"));
    }
    private static File getResourceFile(String resourceName) throws URISyntaxException {
        return new File(ClassLoader.getSystemClassLoader().getResource(resourceName).toURI());
    }



}
