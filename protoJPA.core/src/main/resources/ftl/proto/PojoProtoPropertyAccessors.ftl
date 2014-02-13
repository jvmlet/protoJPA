<#-- // Property accessors -->
<#foreach property in pojo.getAllPropertiesIterator()>
    <#if pojo.getMetaAttribAsBool(property, "gen-property", true)>
        <#if pojo.hasFieldJavaDoc(property)>
        /**
        * ${pojo.getFieldJavaDoc(property, 4)}
        */
        </#if>
        <#include "../../pojo/GetPropertyAnnotation.ftl"/>
    ${pojo.getPropertyGetModifiers(property)} ${pojo.getJavaTypeName(property, jdk5)} ${pojo.getGetterSignature(property)}() {
        <#if exporter.isCollectionType(property)>
            <#include "PojoProtoCollectionPropertyGetter.ftl"/>
        <#else>
            return <#include "protoToJava.ftl"/>;
        </#if>
    }
    ${pojo.getPropertySetModifiers(property)} void set${pojo.getPropertyName(property)}(${pojo.getJavaTypeName(property, jdk5)} ${property.name}) {
    <#if exporter.isCollectionType(property)>
        <#include "PojoProtoCollectionPropertySetter.ftl"/>
    <#else>
         <#include "javaToProto.ftl"/>;
    </#if>
}
</#if>
</#foreach>