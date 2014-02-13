<#if exporter.isNullableType(property)>
!this.builder.has${pojo.getPropertyName(property)}() ?null:
</#if>

<#if exporter.isGeneratedType(pojo.getJavaTypeName(property, jdk5))>
new ${pojo.getJavaTypeName(property, jdk5)}(this.builder.${pojo.getGetterSignature(property)}())
<#else>
    <#assign exp >this.builder.${pojo.getGetterSignature(property)}()</#assign>
${exporter.protoFieldTypeToJavaTypeExpression(property,pojo,exp )}
</#if>