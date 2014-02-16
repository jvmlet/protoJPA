message ${pojo.getDeclarationName()} {
<#assign c=0/>
<#foreach field in pojo.getAllPropertiesIterator() >
    <#if pojo.getMetaAttribAsBool(field, "gen-property", true)>
       ${exporter.getUsageType(field,pojo)} ${exporter.getProtoFieldType(field,pojo)} ${field.name} = ${field_index+1};<#if pojo.hasMetaAttribute(field, "field-description")>//${pojo.getFieldJavaDoc(field, 0)}</#if>
        <#assign c=field_index+1/>
    </#if>
</#foreach>

<#assign additionalFields =pojo.getMetaAsString("proto-code")/>
<#if (additionalFields.trim().length()>0)>
<#foreach addfield in additionalFields.split(";") >
    ${addfield.replace("%d",(addfield_index+1+c)?string.number)};
</#foreach>
</#if>
}
