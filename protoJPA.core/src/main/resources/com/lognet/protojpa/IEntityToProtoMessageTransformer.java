package com.lognet.protojpa;

public interface IEntityToProtoMessageTransformer {
    <T extends com.google.protobuf.GeneratedMessage>  T toProtoMessage();
    <T extends com.google.protobuf.GeneratedMessage.Builder>  T toMessageBuilder();

}