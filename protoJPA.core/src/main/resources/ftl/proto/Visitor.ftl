package com.lognet.protojpa;
public interface  IProtoJpaVisitor{
<#foreach pj in exporter.getCfg2JavaTool().getPOJOIterator(exporter.getConfiguration().getClassMappings())>
    void visit(${pj.getQualifiedDeclarationName()} visitee);
</#foreach>
}