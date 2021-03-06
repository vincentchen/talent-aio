// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/resources/chat.proto

package com.talent.aio.examples.im.common.packets;

/**
 * Protobuf enum {@code com.talent.aio.examples.im.common.packets.ChatOp}
 */
public enum ChatOp
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>JOIN = 0;</code>
   */
  JOIN(0),
  /**
   * <code>QUIT = 1;</code>
   */
  QUIT(1),
  /**
   * <code>CHAT = 2;</code>
   */
  CHAT(2),
  /**
   * <code>ACK = 3;</code>
   */
  ACK(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>JOIN = 0;</code>
   */
  public static final int JOIN_VALUE = 0;
  /**
   * <code>QUIT = 1;</code>
   */
  public static final int QUIT_VALUE = 1;
  /**
   * <code>CHAT = 2;</code>
   */
  public static final int CHAT_VALUE = 2;
  /**
   * <code>ACK = 3;</code>
   */
  public static final int ACK_VALUE = 3;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static ChatOp valueOf(int value) {
    return forNumber(value);
  }

  public static ChatOp forNumber(int value) {
    switch (value) {
      case 0: return JOIN;
      case 1: return QUIT;
      case 2: return CHAT;
      case 3: return ACK;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<ChatOp>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      ChatOp> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<ChatOp>() {
          public ChatOp findValueByNumber(int number) {
            return ChatOp.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.talent.aio.examples.im.common.packets.Chat.getDescriptor().getEnumTypes().get(0);
  }

  private static final ChatOp[] VALUES = values();

  public static ChatOp valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private ChatOp(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:com.talent.aio.examples.im.common.packets.ChatOp)
}

