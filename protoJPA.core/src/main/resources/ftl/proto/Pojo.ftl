
${pojo.getPackageDeclaration()}
// Generated ${date} by Lognet Hibernate Tools extension ${version}

<#assign classbody>
<#include "../../pojo/PojoTypeDeclaration.ftl"/> {

<#if !pojo.isInterface()>

<#--<#include "PojoFields.ftl"/>-->
private ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()}.Builder builder;

<#--<#include "../../pojo/PojoConstructors.ftl"/>-->
public ${pojo.getDeclarationName()}(){
    builder = ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()}.newBuilder();
}
public ${pojo.getDeclarationName()}(${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()} from){
    builder = ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()}.newBuilder(from);
}

public ${pojo.getPackageName()}.${protoNS}.${pojo.getDeclarationName()} detach(){
    return builder.build();
}

   
<#--<#include "../../pojo/PojoPropertyAccessors.ftl"/>-->
    <#include "PojoProtoPropertyAccessors.ftl"/>

<#include "../../pojo/PojoToString.ftl"/>

<#include "../../pojo/PojoEqualsHashcode.ftl"/>

<#else>
<#include "../../pojo/PojoInterfacePropertyAccessors.ftl"/>

</#if>
<#include "../../pojo/PojoExtraClassCode.ftl"/>

}
</#assign>




${pojo.generateImports()}
${classbody}

