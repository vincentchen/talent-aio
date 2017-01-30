// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/resources/chat.proto

package com.talent.aio.examples.im.common.packets;

/**
 * <pre>
 **
 * 聊天请求
 * </pre>
 *
 * Protobuf type {@code com.talent.aio.examples.im.common.packets.ChatReqBody}
 */
public  final class ChatReqBody extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.talent.aio.examples.im.common.packets.ChatReqBody)
    ChatReqBodyOrBuilder {
  // Use ChatReqBody.newBuilder() to construct.
  private ChatReqBody(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ChatReqBody() {
    time_ = 0L;
    type_ = 0;
    text_ = "";
    group_ = "";
    toId_ = 0;
    toNick_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ChatReqBody(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            time_ = input.readInt64();
            break;
          }
          case 16: {

            type_ = input.readInt32();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            text_ = s;
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            group_ = s;
            break;
          }
          case 40: {

            toId_ = input.readInt32();
            break;
          }
          case 50: {
            java.lang.String s = input.readStringRequireUtf8();

            toNick_ = s;
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.talent.aio.examples.im.common.packets.Chat.internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.talent.aio.examples.im.common.packets.Chat.internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.talent.aio.examples.im.common.packets.ChatReqBody.class, com.talent.aio.examples.im.common.packets.ChatReqBody.Builder.class);
  }

  public static final int TIME_FIELD_NUMBER = 1;
  private long time_;
  /**
   * <code>int64 time = 1;</code>
   */
  public long getTime() {
    return time_;
  }

  public static final int TYPE_FIELD_NUMBER = 2;
  private int type_;
  /**
   * <pre>
   *聊天类型
   * </pre>
   *
   * <code>int32 type = 2;</code>
   */
  public int getType() {
    return type_;
  }

  public static final int TEXT_FIELD_NUMBER = 3;
  private volatile java.lang.Object text_;
  /**
   * <pre>
   *聊天内容
   * </pre>
   *
   * <code>string text = 3;</code>
   */
  public java.lang.String getText() {
    java.lang.Object ref = text_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      text_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *聊天内容
   * </pre>
   *
   * <code>string text = 3;</code>
   */
  public com.google.protobuf.ByteString
      getTextBytes() {
    java.lang.Object ref = text_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      text_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int GROUP_FIELD_NUMBER = 4;
  private volatile java.lang.Object group_;
  /**
   * <pre>
   *哪个组
   * </pre>
   *
   * <code>string group = 4;</code>
   */
  public java.lang.String getGroup() {
    java.lang.Object ref = group_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      group_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *哪个组
   * </pre>
   *
   * <code>string group = 4;</code>
   */
  public com.google.protobuf.ByteString
      getGroupBytes() {
    java.lang.Object ref = group_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      group_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TOID_FIELD_NUMBER = 5;
  private int toId_;
  /**
   * <pre>
   *目标用户id，
   * </pre>
   *
   * <code>int32 toId = 5;</code>
   */
  public int getToId() {
    return toId_;
  }

  public static final int TONICK_FIELD_NUMBER = 6;
  private volatile java.lang.Object toNick_;
  /**
   * <pre>
   *目标用户nick，
   * </pre>
   *
   * <code>string toNick = 6;</code>
   */
  public java.lang.String getToNick() {
    java.lang.Object ref = toNick_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      toNick_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *目标用户nick，
   * </pre>
   *
   * <code>string toNick = 6;</code>
   */
  public com.google.protobuf.ByteString
      getToNickBytes() {
    java.lang.Object ref = toNick_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      toNick_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (time_ != 0L) {
      output.writeInt64(1, time_);
    }
    if (type_ != 0) {
      output.writeInt32(2, type_);
    }
    if (!getTextBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, text_);
    }
    if (!getGroupBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, group_);
    }
    if (toId_ != 0) {
      output.writeInt32(5, toId_);
    }
    if (!getToNickBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 6, toNick_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (time_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, time_);
    }
    if (type_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, type_);
    }
    if (!getTextBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, text_);
    }
    if (!getGroupBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, group_);
    }
    if (toId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(5, toId_);
    }
    if (!getToNickBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(6, toNick_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.talent.aio.examples.im.common.packets.ChatReqBody)) {
      return super.equals(obj);
    }
    com.talent.aio.examples.im.common.packets.ChatReqBody other = (com.talent.aio.examples.im.common.packets.ChatReqBody) obj;

    boolean result = true;
    result = result && (getTime()
        == other.getTime());
    result = result && (getType()
        == other.getType());
    result = result && getText()
        .equals(other.getText());
    result = result && getGroup()
        .equals(other.getGroup());
    result = result && (getToId()
        == other.getToId());
    result = result && getToNick()
        .equals(other.getToNick());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + TIME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTime());
    hash = (37 * hash) + TYPE_FIELD_NUMBER;
    hash = (53 * hash) + getType();
    hash = (37 * hash) + TEXT_FIELD_NUMBER;
    hash = (53 * hash) + getText().hashCode();
    hash = (37 * hash) + GROUP_FIELD_NUMBER;
    hash = (53 * hash) + getGroup().hashCode();
    hash = (37 * hash) + TOID_FIELD_NUMBER;
    hash = (53 * hash) + getToId();
    hash = (37 * hash) + TONICK_FIELD_NUMBER;
    hash = (53 * hash) + getToNick().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.talent.aio.examples.im.common.packets.ChatReqBody parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.talent.aio.examples.im.common.packets.ChatReqBody prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   **
   * 聊天请求
   * </pre>
   *
   * Protobuf type {@code com.talent.aio.examples.im.common.packets.ChatReqBody}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.talent.aio.examples.im.common.packets.ChatReqBody)
      com.talent.aio.examples.im.common.packets.ChatReqBodyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.talent.aio.examples.im.common.packets.Chat.internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.talent.aio.examples.im.common.packets.Chat.internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.talent.aio.examples.im.common.packets.ChatReqBody.class, com.talent.aio.examples.im.common.packets.ChatReqBody.Builder.class);
    }

    // Construct using com.talent.aio.examples.im.common.packets.ChatReqBody.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      time_ = 0L;

      type_ = 0;

      text_ = "";

      group_ = "";

      toId_ = 0;

      toNick_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.talent.aio.examples.im.common.packets.Chat.internal_static_com_talent_aio_examples_im_common_packets_ChatReqBody_descriptor;
    }

    public com.talent.aio.examples.im.common.packets.ChatReqBody getDefaultInstanceForType() {
      return com.talent.aio.examples.im.common.packets.ChatReqBody.getDefaultInstance();
    }

    public com.talent.aio.examples.im.common.packets.ChatReqBody build() {
      com.talent.aio.examples.im.common.packets.ChatReqBody result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public com.talent.aio.examples.im.common.packets.ChatReqBody buildPartial() {
      com.talent.aio.examples.im.common.packets.ChatReqBody result = new com.talent.aio.examples.im.common.packets.ChatReqBody(this);
      result.time_ = time_;
      result.type_ = type_;
      result.text_ = text_;
      result.group_ = group_;
      result.toId_ = toId_;
      result.toNick_ = toNick_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.talent.aio.examples.im.common.packets.ChatReqBody) {
        return mergeFrom((com.talent.aio.examples.im.common.packets.ChatReqBody)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.talent.aio.examples.im.common.packets.ChatReqBody other) {
      if (other == com.talent.aio.examples.im.common.packets.ChatReqBody.getDefaultInstance()) return this;
      if (other.getTime() != 0L) {
        setTime(other.getTime());
      }
      if (other.getType() != 0) {
        setType(other.getType());
      }
      if (!other.getText().isEmpty()) {
        text_ = other.text_;
        onChanged();
      }
      if (!other.getGroup().isEmpty()) {
        group_ = other.group_;
        onChanged();
      }
      if (other.getToId() != 0) {
        setToId(other.getToId());
      }
      if (!other.getToNick().isEmpty()) {
        toNick_ = other.toNick_;
        onChanged();
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      com.talent.aio.examples.im.common.packets.ChatReqBody parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (com.talent.aio.examples.im.common.packets.ChatReqBody) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long time_ ;
    /**
     * <code>int64 time = 1;</code>
     */
    public long getTime() {
      return time_;
    }
    /**
     * <code>int64 time = 1;</code>
     */
    public Builder setTime(long value) {
      
      time_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 time = 1;</code>
     */
    public Builder clearTime() {
      
      time_ = 0L;
      onChanged();
      return this;
    }

    private int type_ ;
    /**
     * <pre>
     *聊天类型
     * </pre>
     *
     * <code>int32 type = 2;</code>
     */
    public int getType() {
      return type_;
    }
    /**
     * <pre>
     *聊天类型
     * </pre>
     *
     * <code>int32 type = 2;</code>
     */
    public Builder setType(int value) {
      
      type_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *聊天类型
     * </pre>
     *
     * <code>int32 type = 2;</code>
     */
    public Builder clearType() {
      
      type_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object text_ = "";
    /**
     * <pre>
     *聊天内容
     * </pre>
     *
     * <code>string text = 3;</code>
     */
    public java.lang.String getText() {
      java.lang.Object ref = text_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        text_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *聊天内容
     * </pre>
     *
     * <code>string text = 3;</code>
     */
    public com.google.protobuf.ByteString
        getTextBytes() {
      java.lang.Object ref = text_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        text_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *聊天内容
     * </pre>
     *
     * <code>string text = 3;</code>
     */
    public Builder setText(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      text_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *聊天内容
     * </pre>
     *
     * <code>string text = 3;</code>
     */
    public Builder clearText() {
      
      text_ = getDefaultInstance().getText();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *聊天内容
     * </pre>
     *
     * <code>string text = 3;</code>
     */
    public Builder setTextBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      text_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object group_ = "";
    /**
     * <pre>
     *哪个组
     * </pre>
     *
     * <code>string group = 4;</code>
     */
    public java.lang.String getGroup() {
      java.lang.Object ref = group_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        group_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *哪个组
     * </pre>
     *
     * <code>string group = 4;</code>
     */
    public com.google.protobuf.ByteString
        getGroupBytes() {
      java.lang.Object ref = group_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        group_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *哪个组
     * </pre>
     *
     * <code>string group = 4;</code>
     */
    public Builder setGroup(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      group_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *哪个组
     * </pre>
     *
     * <code>string group = 4;</code>
     */
    public Builder clearGroup() {
      
      group_ = getDefaultInstance().getGroup();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *哪个组
     * </pre>
     *
     * <code>string group = 4;</code>
     */
    public Builder setGroupBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      group_ = value;
      onChanged();
      return this;
    }

    private int toId_ ;
    /**
     * <pre>
     *目标用户id，
     * </pre>
     *
     * <code>int32 toId = 5;</code>
     */
    public int getToId() {
      return toId_;
    }
    /**
     * <pre>
     *目标用户id，
     * </pre>
     *
     * <code>int32 toId = 5;</code>
     */
    public Builder setToId(int value) {
      
      toId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *目标用户id，
     * </pre>
     *
     * <code>int32 toId = 5;</code>
     */
    public Builder clearToId() {
      
      toId_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object toNick_ = "";
    /**
     * <pre>
     *目标用户nick，
     * </pre>
     *
     * <code>string toNick = 6;</code>
     */
    public java.lang.String getToNick() {
      java.lang.Object ref = toNick_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        toNick_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *目标用户nick，
     * </pre>
     *
     * <code>string toNick = 6;</code>
     */
    public com.google.protobuf.ByteString
        getToNickBytes() {
      java.lang.Object ref = toNick_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        toNick_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *目标用户nick，
     * </pre>
     *
     * <code>string toNick = 6;</code>
     */
    public Builder setToNick(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      toNick_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *目标用户nick，
     * </pre>
     *
     * <code>string toNick = 6;</code>
     */
    public Builder clearToNick() {
      
      toNick_ = getDefaultInstance().getToNick();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *目标用户nick，
     * </pre>
     *
     * <code>string toNick = 6;</code>
     */
    public Builder setToNickBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      toNick_ = value;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:com.talent.aio.examples.im.common.packets.ChatReqBody)
  }

  // @@protoc_insertion_point(class_scope:com.talent.aio.examples.im.common.packets.ChatReqBody)
  private static final com.talent.aio.examples.im.common.packets.ChatReqBody DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.talent.aio.examples.im.common.packets.ChatReqBody();
  }

  public static com.talent.aio.examples.im.common.packets.ChatReqBody getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ChatReqBody>
      PARSER = new com.google.protobuf.AbstractParser<ChatReqBody>() {
    public ChatReqBody parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ChatReqBody(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ChatReqBody> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ChatReqBody> getParserForType() {
    return PARSER;
  }

  public com.talent.aio.examples.im.common.packets.ChatReqBody getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
