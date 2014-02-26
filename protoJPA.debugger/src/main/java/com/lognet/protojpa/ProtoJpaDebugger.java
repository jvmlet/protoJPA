package com.lognet.protojpa;


import com.lognet.protojpa.testdb.TestDB;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by alexf on 2/13/14.
 */
public class ProtoJpaDebugger {


    @BeforeClass
    public static void startDB() throws  Exception {
        TestDB.start();
    }
     @AfterClass
     public static void shutDownDB(){
         TestDB.stop();
     }

    @Test
    public void test() throws URISyntaxException {
        final Project project = new Project();
        project.init();
        DefaultLogger logger = new DefaultLogger();
        project.addBuildListener(logger);
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
        System.setErr(new PrintStream(new DemuxOutputStream(project, true)));

        final URL buildXML = ClassLoader.getSystemClassLoader().getResource("build.xml");
        ProjectHelper.getProjectHelper().parse(project,new File(buildXML.toURI()));
        project.executeTarget(project.getDefaultTarget());

    }





}
