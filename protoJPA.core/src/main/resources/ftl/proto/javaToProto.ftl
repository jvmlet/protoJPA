<#if exporter.isCollectionType(property)>
   <#assign type=exporter.getGenericParamType(pojo.getJavaTypeName(property, jdk5))/>
messageBuilder.clear${property.name?capitalize}();
    for(${type} v: ${property.name}){
messageBuilder.add${property.name?capitalize}(v.toProtoMessage());
    }
<#else>
    <#if exporter.isNullableType(property)>
    if(null==${property.name}){
    messageBuilder.clear${pojo.getPropertyName(property)}();
    }else{
    </#if>
    <#if exporter.isGeneratedType(property,pojo)>
    messageBuilder.set${property.name?capitalize}(${property.name}.toProtoMessage());
    <#else>
        <#assign val=exporter.javaTypeToProtoFieldTypeExpression(property,pojo,property.name)/>
    messageBuilder.set${pojo.getPropertyName(property)}(${val});
    </#if>
    <#if exporter.isNullableType(property)>
    }
    </#if>
</#if>