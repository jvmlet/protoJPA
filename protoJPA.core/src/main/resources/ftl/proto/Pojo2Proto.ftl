<#-- // Property accessors -->

<#--
public ${pojo.getDeclarationName()}(){
    builder = ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()}.newBuilder();
}
public ${pojo.getDeclarationName()}(${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()} from){
    builder = ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()}.newBuilder(from);
}



-->

<#assign protoTypeDeclaration=pojo.getPackageName().concat("."+protoNS+".").concat(pojo.getDeclarationName())/>
<#assign jpaTypeDeclaration=pojo.getPackageName().concat("."+pojo.getDeclarationName())/>

public static ${jpaTypeDeclaration} fromProtoMessage(${protoTypeDeclaration} protoMessage){
    ${jpaTypeDeclaration} entity = new ${jpaTypeDeclaration}();
    <#foreach property in pojo.getAllPropertiesIterator()>
        <#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
            <#include "protoToJava.ftl"/>
        </#if>
    </#foreach>
    return entity;
}
private ${protoTypeDeclaration}.Builder messageBuilder = null;

public ${protoTypeDeclaration}.Builder   toMessageBuilder(){
    if(null==messageBuilder){
    messageBuilder =  ${protoTypeDeclaration}.newBuilder();
        <#foreach property in pojo.getAllPropertiesIterator()>
            <#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
                <#include "javaToProto.ftl"/>
            </#if>
        </#foreach>
    }
    return messageBuilder;
};



public ${protoTypeDeclaration} toProtoMessage(){

    return toMessageBuilder().build();
}


<#--<#foreach property in pojo.getAllPropertiesIterator()>-->
    <#--<#if pojo.getMetaAttribAsBool(property, "gen-property", true)>-->





    <#--${pojo.getPropertySetModifiers(property)} void set${pojo.getPropertyName(property)}(${pojo.getJavaTypeName(property, jdk5)} ${property.name}) {-->
        <#--<#if exporter.isCollectionType(property)>-->
            <#--<#include "PojoProtoCollectionPropertySetter.ftl"/>-->
        <#--<#else>-->
            <#--<#include "javaToProto.ftl"/>;-->
        <#--</#if>-->
    <#--}-->
    <#--</#if>-->
<#--</#foreach>-->