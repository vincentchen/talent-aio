// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/resources/chat.proto

package com.talent.aio.examples.im.common.packets;

public final class Chat {
  private Chat() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_BaseReqBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_BaseReqBody_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_JoinReqBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_JoinReqBody_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_JoinGroupResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_JoinGroupResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_JoinRespBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_JoinRespBody_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_talent_aio_examples_im_common_packets_ChatRespBody_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_talent_aio_examples_im_common_packets_ChatRespBody_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\031main/resources/chat.proto\022)com.talent." +
      "aio.examples.im.common.packets\"\033\n\013BaseRe" +
      "qBody\022\014\n\004time\030\001 \001(\003\"*\n\013JoinReqBody\022\014\n\004ti" +
      "me\030\001 \001(\003\022\r\n\005group\030\002 \001(\t\",\n\017JoinGroupResu" +
      "lt\022\014\n\004code\030\001 \001(\005\022\013\n\003msg\030\002 \001(\t\"w\n\014JoinRes" +
      "pBody\022\014\n\004time\030\001 \001(\003\022J\n\006result\030\002 \001(\0132:.co" +
      "m.talent.aio.examples.im.common.packets." +
      "JoinGroupResult\022\r\n\005group\030  \001(\t\"d\n\013ChatRe" +
      "qBody\022\014\n\004time\030\001 \001(\003\022\014\n\004type\030\002 \001(\005\022\014\n\004tex" +
      "t\030\003 \001(\t\022\r\n\005group\030\004 \001(\t\022\014\n\004toId\030\005 \001(\005\022\016\n\006",
      "toNick\030\006 \001(\t\"\207\001\n\014ChatRespBody\022\014\n\004time\030\001 " +
      "\001(\003\022\014\n\004type\030\002 \001(\005\022\014\n\004text\030\003 \001(\t\022\016\n\006fromI" +
      "d\030\004 \001(\005\022\020\n\010fromNick\030\005 \001(\t\022\014\n\004toId\030\006 \001(\005\022" +
      "\016\n\006toNick\030\007 \001(\t\022\r\n\005group\030\010 \001(\t*/\n\006ChatOp" +
      "\022\010\n\004JOIN\020\000\022\010\n\004QUIT\020\001\022\010\n\004CHAT\020\002\022\007\n\003ACK\020\003B" +
      "-\n)com.talent.aio.examples.im.common.pac" +
      "ketsP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_talent_aio_examples_im_common_packets_BaseReqBody_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_talent_aio_examples_im_common_packets_BaseReqBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_BaseReqBody_descriptor,
        new java.lang.String[] { "Time", });
    internal_static_com_talent_aio_examples_im_common_packets_JoinReqBody_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_talent_aio_examples_im_common_packets_JoinReqBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_JoinReqBody_descriptor,
        new java.lang.String[] { "Time", "Group", });
    internal_static_com_talent_aio_examples_im_common_packets_JoinGroupResult_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_talent_aio_examples_im_common_packets_JoinGroupResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_JoinGroupResult_descriptor,
        new java.lang.String[] { "Code", "Msg", });
    internal_static_com_talent_aio_examples_im_common_packets_JoinRespBody_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_com_talent_aio_examples_im_common_packets_JoinRespBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_JoinRespBody_descriptor,
        new java.lang.String[] { "Time", "Result", "Group", });
    internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor,
        new java.lang.String[] { "Time", "Type", "Text", "Group", "ToId", "ToNick", });
    internal_static_com_talent_aio_examples_im_common_packets_ChatRespBody_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_com_talent_aio_examples_im_common_packets_ChatRespBody_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_talent_aio_examples_im_common_packets_ChatRespBody_descriptor,
        new java.lang.String[] { "Time", "Type", "Text", "FromId", "FromNick", "ToId", "ToNick", "Group", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}