package com.talent.aio.examples.im.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月30日 下午6:08:49
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月30日 | tanyaowu | 新建类
 *
 */
public enum Command
{
	/**
	 * 鉴权请求
	 */
	AUTH_REQ((byte) 1, "鉴权请求"),
	/**
	 * 鉴权响应
	 */
	AUTH_RESP((byte) -1, "鉴权响应"),
	/**
	 * 申请进入群组
	 */
	JOIN_GROUP_REQ((byte) 2, "申请进入群组"),
	/**
	 * 申请进入群组响应
	 */
	JOIN_GROUP_RESP((byte) -2, "申请进入群组响应"),
	/**
	 * 进入房间通知，成员进入房间后，要向其它成员发送通知
	 */
	JOIN_GROUP_NOTIFY_RESP((byte) -3, "进入房间通知，成员进入房间后，要向其它成员发送通知"),
	/**
	 * 聊天请求
	 */
	CHAT_REQ((byte) 4, "聊天请求"),
	/**
	 * 聊天响应
	 */
	CHAT_RESP((byte) -4, "聊天响应"),
	/**
	 * 开播请求
	 */
	STARTSHOW_REQ((byte) 5, "开播请求"),
	/**
	 * 开播响应
	 */
	STARTSHOW_RESP((byte) -5, "开播响应"),
	/**
	 * 停播请求
	 */
	ENDSHOW_REQ((byte) 6, "停播请求"),
	/**
	 * 停播通知
	 */
	ENDSHOW_NOTIFY_RESP((byte) -6, "停播通知"),
	/**
	 * 心跳请求
	 */
	HEARTBEAT_REQ((byte) 99, "心跳请求");

	private byte code;
	private String name;

	private Command(byte code, String name)
	{
		this.setCode(code);
		this.setName(name);
	}

	/**
	 * @return the code
	 */
	public byte getCode()
	{
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(byte code)
	{
		this.code = code;
	}

	private static final Map<Byte, Command> map = new HashMap<>();
	static
	{
		for (Command command : values())
		{
			map.put(command.getCode(), command);
		}
	}

	public static Command valueOf(byte code)
	{
		return map.get(code);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
