<#assign type=exporter.getGenericParamType(pojo.getJavaTypeName(property, jdk5))/>
this.builder.clear${property.name?capitalize}();
for(${type} v: ${property.name}){
this.builder.add${property.name?capitalize}(v.detach());
}
