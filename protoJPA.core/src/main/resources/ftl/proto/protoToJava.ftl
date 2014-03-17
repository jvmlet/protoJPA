<#if exporter.isCollectionType(property)>
    <#assign type=exporter.getGenericParamType(pojo.getJavaTypeName(property, jdk5))/>
Set< ${type}> _set = new java.util.HashSet<${type}>();
for(${protoNS}.${type} v: protoMessage.${pojo.getGetterSignature(property)}List()){
_set.add(  ${type}.fromProtoMessage(v));
}
entity.set${pojo.getPropertyName(property)}(_set);
<#else>
entity.set${pojo.getPropertyName(property)}(
    <#if exporter.isNullableType(property)>
    !protoMessage.has${pojo.getPropertyName(property)}() ?null:
    </#if>
    <#if exporter.isGeneratedType(property,pojo)>
     ${pojo.getJavaTypeName(property, jdk5)}.fromProtoMessage(protoMessage.${pojo.getGetterSignature(property)}())
    <#else>
        <#assign exp >protoMessage.${pojo.getGetterSignature(property)}()</#assign>
    ${exporter.protoFieldTypeToJavaTypeExpression(property,pojo,exp )}
    </#if>
);
</#if>