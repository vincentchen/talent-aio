/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:15:03
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:15:03
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class ImPacket extends Packet
{

	private static Logger log = LoggerFactory.getLogger(ImPacket.class);

	/**
	 * 心跳字节
	 */
	public static final byte HEARTBEAT_BYTE = -128;

	public static final String CHARSET = "utf-8";

	/**
	 * 协议版本号
	 */
	public final static byte VERSION = 1;

	/**
	 * 消息体最多为多少
	 */
	public static final int MAX_LENGTH_OF_BODY = (int) (1024 * 1024 * 2.1); //只支持多少M数据

	/**
	 * 消息头最少为多少个字节
	 */
	public static final int LEAST_HEADER_LENGHT = 4;//1+1+2 + (2+4)

	/**
	 * 压缩标识位mask，1为压缩，否则不压缩
	 */
	public static final byte FIRST_BYTE_MASK_COMPRESS = 0B01000000;

	/**
	 * 是否有同步序列号标识位mask，如果有同步序列号，则消息头会带有同步序列号，否则不带
	 */
	public static final byte FIRST_BYTE_MASK_HAS_SYNSEQ = 0B00100000;

	/**
	 * 是否是用4字节来表示消息体的长度
	 */
	public static final byte FIRST_BYTE_MASK_4_BYTE_LENGTH = 0B00010000;

	/**
	 * 版本号mask
	 */
	public static final byte FIRST_BYTE_MASK_VERSION = 0B00001111;

	/**
	 * 是否压缩消息体
	 */
	private boolean isCompress = false;

	/**
	 * 是否带有同步序列号
	 */
	private boolean hasSynSeq = false;

	/**
	 * 是否用4字节来表示消息体的长度，false:2字节，true:4字节
	 */
	private boolean is4ByteLength = false;

	public static boolean decodeCompress(byte version)
	{
		return (FIRST_BYTE_MASK_COMPRESS & version) != 0;
	}

	public static byte encodeCompress(byte bs, boolean isCompress)
	{
		if (isCompress)
		{
			return (byte) (bs | FIRST_BYTE_MASK_COMPRESS);
		} else
		{
			return (byte) (bs & (FIRST_BYTE_MASK_COMPRESS ^ 0b01111111));
		}
	}

	public static boolean decodeHasSynSeq(byte firstByte)
	{
		return (FIRST_BYTE_MASK_HAS_SYNSEQ & firstByte) != 0;
	}

	public static byte encodeHasSynSeq(byte bs, boolean isCompress)
	{
		if (isCompress)
		{
			return (byte) (bs | FIRST_BYTE_MASK_HAS_SYNSEQ);
		} else
		{
			return (byte) (bs & (FIRST_BYTE_MASK_HAS_SYNSEQ ^ 0b01111111));
		}
	}

	public static boolean decode4ByteLength(byte version)
	{
		return (FIRST_BYTE_MASK_4_BYTE_LENGTH & version) != 0;
	}

	public static byte encode4ByteLength(byte bs, boolean is4ByteLength)
	{
		if (is4ByteLength)
		{
			return (byte) (bs | FIRST_BYTE_MASK_4_BYTE_LENGTH);
		} else
		{
			return (byte) (bs & (FIRST_BYTE_MASK_4_BYTE_LENGTH ^ 0b01111111));
		}
	}

	public static byte decodeVersion(byte version)
	{
		return (byte) (FIRST_BYTE_MASK_VERSION & version);
	}

	public int calcHeaderLength()
	{
		int ret = LEAST_HEADER_LENGHT;
		if (this.is4ByteLength)
		{
			ret += 2;
		}
		if (this.hasSynSeq)
		{
			ret += 4;
		}
		return ret;
	}

	//	public static byte encodeVersion(byte firstByte, byte version)
	//	{
	//		if (isCompress)
	//		{
	//			return (byte) (firstByte | FIRST_BYTE_HAS_SYNSEQ_COMPRESS);
	//		} else
	//		{
	//			return (byte) (firstByte & (FIRST_BYTE_HAS_SYNSEQ_COMPRESS ^ 0b01111111));
	//		}
	//	}

	private byte[] body;

	public ImPacket(byte[] body, Command command)
	{
		super();
		this.body = body;
		this.setCommand(command);
	}

	public ImPacket(Command command)
	{
		super();
		this.setCommand(command);
	}

	public ImPacket()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int xx2x = Integer.MAX_VALUE;
		byte b = Byte.MAX_VALUE;
		short fd = Short.MAX_VALUE;
		final int fdsfd = (int) Math.pow(2, 23);
		System.out.println("Math.pow(2, 23):" + Math.pow(2, 23));
		System.out.println("Math.pow(2, 24):" + Math.pow(2, 24));
		System.out.println("Math.pow(2, 8):" + Math.pow(2, 8));
		System.out.println("Math.pow(2, 7):" + Math.pow(2, 7));

		byte xx = FIRST_BYTE_MASK_COMPRESS ^ 0b01111111;
		String xxx = Integer.toBinaryString(xx);
		System.out.println(xxx);

		byte yy = 0b01111111;
		boolean isCompress = true;
		xxx = Integer.toBinaryString(encodeCompress(yy, isCompress));
		System.out.println(xxx);

		isCompress = false;
		xxx = Integer.toBinaryString(encodeCompress(yy, isCompress));
		System.out.println(xxx);
	}

	@JSONField(serialize = false)
	private int bodyLen;

	private Command command;

	public int getBodyLen()
	{
		return bodyLen;
	}

	public Command getCommand()
	{
		return command;
	}

	//	public final static AtomicInteger seq = new AtomicInteger();
	//
	//	private Integer seqNo = null;
	//
	//	@Override
	//	public String getSeqNo()
	//	{
	//		if (this.seqNo == null)
	//		{
	//			return null;
	//		}
	//		return String.valueOf(this.seqNo);
	//	}
	//
	//	@Override
	//	public void setSeqNo(String seqNo)
	//	{
	//		this.seqNo = seqNo;
	//	}

	public void setBodyLen(int bodyLen)
	{
		this.bodyLen = bodyLen;
	}

	public void setCommand(Command type)
	{
		this.command = type;
		//		if (com.talent.im.common.command.Command.PRIORITY_HANDLER_COMMANDS.contains(type))
		//		{
		//			this.setPriority(PRIORITY_MAX);
		//		}
	}

	/**
	 * @return the body
	 */
	public byte[] getBody()
	{
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body)
	{
		this.body = body;
	}

	/**
	 * @return the isCompress
	 */
	public boolean isCompress()
	{
		return isCompress;
	}

	/**
	 * @param isCompress the isCompress to set
	 */
	public void setCompress(boolean isCompress)
	{
		this.isCompress = isCompress;
	}

	/**
	 * @return the hasSynSeq
	 */
	public boolean isHasSynSeq()
	{
		return hasSynSeq;
	}

	/**
	 * @param hasSynSeq the hasSynSeq to set
	 */
	public void setHasSynSeq(boolean hasSynSeq)
	{
		this.hasSynSeq = hasSynSeq;
	}

	/**
	 * @return the is4byteLength
	 */
	public boolean isIs4ByteLength()
	{
		return is4ByteLength;
	}

	/**
	 * @param is4ByteLength the is4byteLength to set
	 */
	public void setIs4byteLength(boolean is4ByteLength)
	{
		this.is4ByteLength = is4ByteLength;
	}

}
