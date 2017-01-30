// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: main/resources/chat.proto

package com.talent.aio.examples.im.common.packets;

public interface ChatRespBodyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.talent.aio.examples.im.common.packets.ChatRespBody)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 time = 1;</code>
   */
  long getTime();

  /**
   * <pre>
   *聊天类型
   * </pre>
   *
   * <code>int32 type = 2;</code>
   */
  int getType();

  /**
   * <pre>
   *聊天内容
   * </pre>
   *
   * <code>string text = 3;</code>
   */
  java.lang.String getText();
  /**
   * <pre>
   *聊天内容
   * </pre>
   *
   * <code>string text = 3;</code>
   */
  com.google.protobuf.ByteString
      getTextBytes();

  /**
   * <pre>
   *发送聊天消息的用户id
   * </pre>
   *
   * <code>int32 fromId = 4;</code>
   */
  int getFromId();

  /**
   * <pre>
   *发送聊天消息的用户nick
   * </pre>
   *
   * <code>string fromNick = 5;</code>
   */
  java.lang.String getFromNick();
  /**
   * <pre>
   *发送聊天消息的用户nick
   * </pre>
   *
   * <code>string fromNick = 5;</code>
   */
  com.google.protobuf.ByteString
      getFromNickBytes();

  /**
   * <pre>
   *目标用户id
   * </pre>
   *
   * <code>int32 toId = 6;</code>
   */
  int getToId();

  /**
   * <pre>
   *目标用户nick
   * </pre>
   *
   * <code>string toNick = 7;</code>
   */
  java.lang.String getToNick();
  /**
   * <pre>
   *目标用户nick
   * </pre>
   *
   * <code>string toNick = 7;</code>
   */
  com.google.protobuf.ByteString
      getToNickBytes();

  /**
   * <pre>
   *目标group
   * </pre>
   *
   * <code>string group = 8;</code>
   */
  java.lang.String getGroup();
  /**
   * <pre>
   *目标group
   * </pre>
   *
   * <code>string group = 8;</code>
   */
  com.google.protobuf.ByteString
      getGroupBytes();
}