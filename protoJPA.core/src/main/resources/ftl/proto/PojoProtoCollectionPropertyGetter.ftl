<#assign type=exporter.getGenericParamType(pojo.getJavaTypeName(property, jdk5))/>
Set< ${type}> _set = new java.util.HashSet<${type}>();
for(${protoNS}.${type} v: this.builder.${pojo.getGetterSignature(property)}List()){
    _set.add( new ${type}(v));
}
return _set;