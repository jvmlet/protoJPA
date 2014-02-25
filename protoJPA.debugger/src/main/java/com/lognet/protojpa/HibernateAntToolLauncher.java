package com.lognet.protojpa;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DemuxOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;
import org.hibernate.tool.ant.GenericExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;
import org.hibernate.tool.ant.JDBCConfigurationTask;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by alexf on 2/9/14.
 */
public class HibernateAntToolLauncher {


    private Project project;

    public HibernateAntToolLauncher() {
        project = new Project();
        project.init();
        DefaultLogger logger = new DefaultLogger();
        project.addBuildListener(logger);
        logger.setOutputPrintStream(System.out);
        logger.setErrorPrintStream(System.err);
        logger.setMessageOutputLevel(Project.MSG_INFO);
        System.setOut(new PrintStream(new DemuxOutputStream(project, false)));
        System.setErr(new PrintStream(new DemuxOutputStream(project, true)));


    }

    public void run(File revEngFile, File cfgXmlFile,File destDir,File resourceDestDir,File protocExecutable) {

        project.fireBuildStarted();

        Path revEngPath = new Path(project);
        revEngPath.setLocation(revEngFile);
        final HibernateToolTask hibernateToolTask = new HibernateToolTask();
        hibernateToolTask.setProject(project);


        hibernateToolTask.setDestDir(destDir);
        final JDBCConfigurationTask jdbcConfiguration = hibernateToolTask.createJDBCConfiguration();
        jdbcConfiguration.setPackageName("db.entities");
        jdbcConfiguration.setConfigurationFile(cfgXmlFile);
        jdbcConfiguration.setRevEngFile(revEngPath);
        final GenericExporterTask hbmTemplate = (GenericExporterTask) hibernateToolTask.createHbmTemplate();
        hbmTemplate.setExporterClass(Pojo2ProtobufExporter.class.getCanonicalName());
        hbmTemplate.addConfiguredProperty(createVariable(Pojo2ProtobufExporter.PROTO_FILE_NAME_PROPERTY, "Example"));
        hbmTemplate.addConfiguredProperty(createVariable(Pojo2ProtobufExporter.RESOURCES_DESTINATION_DIR_PROPERTY, resourceDestDir.getAbsolutePath()));
        hbmTemplate.addConfiguredProperty(createVariable(Pojo2ProtobufExporter.PROTO_TOOL_FILE_PATH_PROPERTY, protocExecutable.getAbsolutePath()));
        hbmTemplate.addConfiguredProperty(createVariable("ejb3", "true"));
        hbmTemplate.addConfiguredProperty(createVariable("jdk5", "true"));


//        final Hbm2JavaExporterTask hbm2Java = (Hbm2JavaExporterTask) hibernateToolTask.createHbm2Java();
//        hbm2Java.setEjb3(true);
//        hbm2Java.setJdk5(true);


        hibernateToolTask.init();
        hibernateToolTask.execute();


        project.log("finished");
        project.fireBuildFinished(null);
    }

    private Environment.Variable createVariable(String key, String value) {
        Environment.Variable variable = new Environment.Variable();
        variable.setKey(key);
        variable.setValue(value);
        return variable;
    }


}
