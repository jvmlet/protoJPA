package com.lognet.protojpa;

import org.hibernate.internal.util.StringHelper;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.ExporterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by alexf on 2/10/14.
 */
public class ProtoFilesConsolidator extends ArtifactCollector {

    public static final String EXTRA_PROTO_MESSAGES_TXT = "extraProtoMessages.txt";

    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private String outputJavaDir;
    private File outputProtoFile;
    private String protocFilePath;
    private String packageName = null;


    public ProtoFilesConsolidator(File outputProtoFile,String outputJavaDir,String protocFilePath) {
        this.outputProtoFile = outputProtoFile;
        this.outputJavaDir = outputJavaDir;
        this.protocFilePath = protocFilePath;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private String getPackageName() {
        return (StringHelper.isNotEmpty(packageName)?packageName:"//default package")+"\n";
    }

    @Override
    public void formatFiles() {
        FileChannel dest = null;
        try {
            outputProtoFile.delete();
            dest = new FileOutputStream(outputProtoFile, true).getChannel();

            final ByteBuffer pckgBuffer = ByteBuffer.allocate((getPackageName()).length()).put(getPackageName().getBytes());
            pckgBuffer.rewind();
            dest.write(pckgBuffer);

            final File[] protoFiles = getFiles("proto");
            for (File protoFile : protoFiles) {
                FileChannel src = new FileInputStream(protoFile).getChannel();
                dest.transferFrom(src, dest.size(), src.size());
                src.close();
                protoFile.delete();
            }
            final URL extraMessages = ClassLoader.getSystemClassLoader().getResource(EXTRA_PROTO_MESSAGES_TXT);
            if(null!=extraMessages){
                FileChannel ch = new FileInputStream(new File(extraMessages.toURI())).getChannel();
                dest.transferFrom(ch, dest.size(), ch.size());
                ch.close();
            }

        } catch (IOException e) {
            throw new ExporterException("Failed to consolidate files", e);
        } catch (URISyntaxException e) {
            throw new ExporterException("Failed to consolidate files", e);
        }
        finally {
            if(null!=dest){
                try {
                    dest.close();
                } catch (IOException e) {

                }
            }
        }
        runProtoc();



    }


    private void runProtoc(){
        if (null != protocFilePath) {
            try {

                String protobinFilePath = new File(outputProtoFile.getParentFile(), outputProtoFile.getName()+"bin").getAbsolutePath();

                final ProcessBuilder processBuilder = new ProcessBuilder(protocFilePath,
                        "--descriptor_set_out=" + protobinFilePath,
                        "--java_out=" + outputJavaDir,
                        "--proto_path=" + outputProtoFile.getParentFile().getAbsolutePath(),
                        outputProtoFile.getAbsolutePath());
                log.info("Executing" + processBuilder.command().toString());
                processBuilder.redirectErrorStream(true);
                final Process protocProccess = processBuilder.start();
                final BufferedReader inputStreamReader =new BufferedReader ( new InputStreamReader(protocProccess.getInputStream()));
                String line=null;
                while ((line = inputStreamReader.readLine ()) != null) {
                    log.error("protoc says : "+new String(line));
                }
                protocProccess.waitFor();

            } catch (Exception e) {
                throw  new ExporterException("Failed to execute protoc " + protocFilePath,e);
            }
        }
    }
}
