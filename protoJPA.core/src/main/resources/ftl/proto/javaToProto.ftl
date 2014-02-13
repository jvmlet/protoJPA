<#if exporter.isNullableType(property)>
  if(null==${property.name}){
        this.builder.clear${pojo.getPropertyName(property)}();
   }else{
</#if>
<#if exporter.isGeneratedType(pojo.getJavaTypeName(property, jdk5))>
    this.builder.set${property.name?capitalize}(${property.name}.detach());
<#else>
    <#assign val=exporter.javaTypeToProtoFieldTypeExpression(property,pojo,property.name)/>
    this.builder.set${pojo.getPropertyName(property)}(${val});
</#if>
<#if exporter.isNullableType(property)>
 }
</#if>
