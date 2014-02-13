package com.lognet.protojpa;

import org.hibernate.mapping.Property;
import org.hibernate.tool.hbm2x.ExporterException;
import org.hibernate.tool.hbm2x.POJOExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;
import org.hibernate.tool.hbm2x.visitor.JavaTypeFromValueVisitor;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alexf on 2/9/14.
 */
public class Pojo2ProtobufExporter extends POJOExporter {

    public static final String PROTO_FILE_NAME_PROPERTY = "protoFileName";
    public static final String RESOURCES_DESTINATION_DIR_PROPERTY = "resourcesDestDir";
    public static final String PROTO_TOOL_FILE_PATH_PROPERTY = "protoToolFilePath";


    private static final Map<Class, String> javaToProtoTypeMapping = new HashMap<Class, String>();
    private static Map<String,Class> primitiveTypes = new HashMap<String,Class>();

    static {
        primitiveTypes.put(Byte.TYPE.getName(), Byte.TYPE);
        primitiveTypes.put(Short.TYPE.getName(), Short.TYPE);
        primitiveTypes.put(Integer.TYPE.getName(), Integer.TYPE);
        primitiveTypes.put(Long.TYPE.getName(), Long.TYPE);
        primitiveTypes.put(Character.TYPE.getName(), Character.TYPE);
        primitiveTypes.put(Float.TYPE.getName(), Float.TYPE);
        primitiveTypes.put(Double.TYPE.getName(), Double.TYPE);
        primitiveTypes.put(Boolean.TYPE.getName(), Boolean.TYPE);



        javaToProtoTypeMapping.put(double.class, "double");
        javaToProtoTypeMapping.put(Double.class, "double");

        javaToProtoTypeMapping.put(float.class, "float");
        javaToProtoTypeMapping.put(Float.class, "float");

        javaToProtoTypeMapping.put(int.class, "sint32");
        javaToProtoTypeMapping.put(Integer.class, "sint32");

        javaToProtoTypeMapping.put(long.class, "sint64");
        javaToProtoTypeMapping.put(Long.class, "sint64");
        javaToProtoTypeMapping.put(BigDecimal.class, "int64");

        javaToProtoTypeMapping.put(boolean.class, "bool");
        javaToProtoTypeMapping.put(Boolean.class, "bool");

        javaToProtoTypeMapping.put(String.class, "string");

        javaToProtoTypeMapping.put(Date.class, "sint64");
        javaToProtoTypeMapping.put(Timestamp.class, "sint64");
        javaToProtoTypeMapping.put(Time.class, "sint64");

    }

    private File originalOutputDir;



    @Override
    protected void setupContext() {
        super.setupContext();

        originalOutputDir = getOutputDirectory();
        String protoFileName = getProtoFileName();
        final String protocFilePath = getProperties().getProperty(PROTO_TOOL_FILE_PATH_PROPERTY);
        if(!new File(protocFilePath).exists()){
            throw  new ExporterException(String.format("File %s doesn't exist",protocFilePath));
        }
        setArtifactCollector(new ProtoFilesConsolidator(new File(getResourcesDestDir(), protoFileName + ".proto"),originalOutputDir.getAbsolutePath(), protocFilePath));
    }



    @Override
    protected void exportComponent(Map additionalContext, POJOClass element) {
        setOutputDirectory(new File(getResourcesDestDir()));
        setTemplateName("ftl/proto/ProtoMessage.ftl");
        setFilePattern("{class-name}.proto");
        super.exportComponent(additionalContext, element);

        setOutputDirectory(originalOutputDir);
        setTemplateName("ftl/proto/Pojo.ftl");
        setFilePattern("{package-name}/{class-name}.java");
        super.exportComponent(additionalContext, element);
    }


    @Override
    protected void exportPersistentClass(Map additionalContext, POJOClass element) {
        // set used package name
        ((ProtoFilesConsolidator)getArtifactCollector()).setPackageName(element.getPackageDeclaration());

        //export proto file
        setOutputDirectory(new File(getResourcesDestDir()));
        setTemplateName("ftl/proto/ProtoMessage.ftl");
        setFilePattern("{package-name}/{class-name}.proto");
        super.exportPersistentClass(additionalContext, element);

        //export java file
        setOutputDirectory(originalOutputDir);
        setTemplateName("ftl/proto/Pojo.ftl");
        setFilePattern("{package-name}/{class-name}.java");
        super.exportPersistentClass(additionalContext, element);
    }

    @Override
    protected void exportPOJO(Map additionalContext, POJOClass element) {

        additionalContext.put("protoNS", getProtoFileName().substring(0,1).toUpperCase()+getProtoFileName().substring(1));
        super.exportPOJO(additionalContext, element);
    }

    private String getProtoFileName(){
       return getProperties().getProperty(PROTO_FILE_NAME_PROPERTY, "default");
    }

    private String getResourcesDestDir(){
        return getProperties().getProperty(RESOURCES_DESTINATION_DIR_PROPERTY, originalOutputDir.getAbsolutePath());
    }



    public String getUsageType(Property property, POJOClass pojo) {
        // required is this property is identifier
        if (property.equals(pojo.getIdentifierProperty())) {
            return "required";
        }
        // all fields of key class (component) are required
        return pojo.isComponent() ? "required" : property.getValue().getType().isCollectionType() ? "repeated " : "optional";

    }

    public String getGenericParamType(String genericType) {
        String type = genericType;
        final int isGeneric = genericType.indexOf('<');
        if(isGeneric>0){
            type= genericType.substring(isGeneric +1,genericType.indexOf('>'));
        }
        return  type;
    }
    public boolean isGeneratedType(String typeName){
        final Iterator pojoIterator = getCfg2JavaTool().getPOJOIterator(getConfiguration().getClassMappings());
        while (pojoIterator.hasNext()){
            final POJOClass pojo= (POJOClass) pojoIterator.next();
            if(pojo.getDeclarationName().equals(typeName)){
                return true;
            }
        }
        return false;
    };
    public boolean isCollectionType(Property property){
        return property.getValue() instanceof org.hibernate.mapping.Collection;
    }

    public boolean isNullableType(Property property){
        try {
            return !getPropertyJavaType(property).isPrimitive();
        } catch (ClassNotFoundException e) {
            return true;
        }
    }
    public String getProtoFieldType(Property property, POJOClass pojo) {
        String fieldType = null;

        try {
            Class<?> javaType = getPropertyJavaType(property);
            fieldType = javaToProtoTypeMapping.get(javaType);
        } catch (ClassNotFoundException e) {

        }
        if(null==fieldType){
            fieldType = getGenericParamType(pojo.getJavaTypeName(property, true));
        }

        return fieldType;
    }

    /**
     * Maps  protobuf-typed expression to java-typed expression according to property java type.
     * Example: <b>int</b> becomes <b>new Integer(int)</b> and <b>dateInMS</b> becomes <b>new Date(dateInMS)</b>
     * @param property  pojo field
     * @param pojo pojo class
     * @param expression protobuf-typed expression to map
     * @return java-typed epression
     */
    public String protoFieldTypeToJavaTypeExpression(Property property, POJOClass pojo,String expression) {
        try {
            Class<?> javaType = getPropertyJavaType(property);
            if(!javaType.isPrimitive() && !javaType.isAssignableFrom(String.class)){
                return String.format("new %s(%s)",javaType.getName(),expression);
            }
            return expression ;
        }catch (ClassNotFoundException e) {
            return String.format("new %s(%s)", pojo.getJavaTypeName(property, true),expression) ;
        }
    }

    /**
     *  Maps java-typed expression to  protobuf-typed expression according to <b>property</b> java type.
     *  Example :<b>someIntExpression</b> becomes <b>someIntExpression.intValue()</b>  and
     *  <b>dateExpression</b> becomes <b>dateExpression.getTime()</b>
     * @param property pojo field
     * @param pojo pojo class
     * @param expression java-typed expression
     * @return    protobuf-typed expression
     */
    public String javaTypeToProtoFieldTypeExpression(Property property, POJOClass pojo,String expression) {
        try {
             Class<?> javaType = getPropertyJavaType(property);
            if(Number.class.isAssignableFrom(javaType)){
                if(javaType.equals(BigDecimal.class)){
                    javaType = Long.class;
                }
                if(javaType.equals(Integer.class)){
                    javaType = int.class;
                }
                return String.format("%s.%sValue()",expression,javaType.getSimpleName().toLowerCase());
            }
            if(Date.class.isAssignableFrom(javaType)){
                return String.format("%s.getTime()",expression);
            }
            return expression ;
        }catch (ClassNotFoundException e) {
            return String.format("%s.detach()",expression );
        }
    }

    private Class<?> getPropertyJavaType(Property property) throws ClassNotFoundException {
        String fieldJavaType = (String) property.getValue().accept(new JavaTypeFromValueVisitor());
        Class<?> primitiveClass = primitiveTypes.get(fieldJavaType);
        return primitiveClass== null?Class.forName(fieldJavaType):primitiveClass;
    }

}
