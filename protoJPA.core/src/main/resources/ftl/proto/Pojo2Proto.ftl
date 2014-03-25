

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
public void accept(com.lognet.protojpa.IProtoJpaVisitor visitor){
   <#if !pojo.isComponent()>
   visitor.visit(this);
   </#if>

}

