
${pojo.getPackageDeclaration()}
// Generated ${date} by Lognet Hibernate Tools extension ${version}

<#assign classbody>

<#--<#include "../../pojo/PojoTypeDeclaration.ftl"/> {-->

    <#assign classAnn =pojo.getMetaAsString("class-header")/>
    <#if (classAnn.trim().length()>0)>
    ${classAnn}
    </#if>

<#include "../../pojo/Ejb3TypeDeclaration.ftl"/>

${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} ${pojo.getExtendsDeclaration()} ${pojo.getImplementsDeclaration()}
{
<#if !pojo.isInterface()>

<#include "../../pojo/PojoFields.ftl"/>



<#include "../../pojo/PojoConstructors.ftl"/>

    <#include "Pojo2Proto.ftl"/>
<#include "../../pojo/PojoPropertyAccessors.ftl"/>



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

