/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:13:15
 *
 * **************************************************************************
 */
package com.talent.aio.examples.server.im;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.task.PacketMeta;
import com.talent.aio.examples.common.im.Command;
import com.talent.aio.examples.common.im.ImPacket;
import com.talent.aio.examples.server.im.handler.AuthHandler;
import com.talent.aio.examples.server.im.handler.ChatHandler;
import com.talent.aio.examples.server.im.handler.ImBsAioHandlerIntf;
import com.talent.aio.examples.server.im.handler.JoinHandler;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:13:15
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class ImAioHandler implements AioHandler<Object, ImPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(ImAioHandler.class);
	
	
	private static Map<Short, ImBsAioHandlerIntf> handlerMap = new HashMap<>();
	static
	{
		handlerMap.put(Command.AUTH_REQ, new AuthHandler());
		handlerMap.put(Command.CHAT_REQ, new ChatHandler());
		handlerMap.put(Command.JOIN_GROUP_REQ, new JoinHandler());
	}
	
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public ImAioHandler()
	{}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public static void main(String[] args)
	{}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#handler(com.talent.aio.common.intf.Packet)
	 * 
	 * @param packet
	 * @return
	 * @throws Exception 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		Short command = packet.getCommand();
		ImBsAioHandlerIntf handler = handlerMap.get(command);
		if (handler != null)
		{
			return handler.handler(packet, channelContext);
		}
		log.warn("找不到对应的命令码[{}]处理类", command);
		return null;
	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#encode(com.talent.aio.common.intf.Packet)
	 * 
	 * @param packet
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public ByteBuffer encode(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext)
	{
		byte[] body = packet.getBody();
		int bodyLen = 0;
		if (body == null)
		{
			bodyLen = 0;
		} else
		{
			bodyLen = body.length;
		}

		int allLen = ImPacket.HEADER_LENGHT + bodyLen;
		ByteBuffer buffer = ByteBuffer.allocate(allLen);//io.netty.buffer.Unpooled.buffer(initialCapacity);
		buffer.order(channelContext.getAioConfig().getByteOrder());
		

		buffer.put(ImPacket.VERSION);

		buffer.putInt(bodyLen);

		buffer.putShort(packet.getCommand());

		if (packet.getSynSeq() != null && packet.getSynSeq() > 0)
		{
			buffer.putInt(packet.getSynSeq());
		} else
		{
			buffer.putInt(0);
		}

		buffer.putInt(0);

		if (body != null)
		{
			buffer.put(body);
		}
		return buffer;
	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#decode(java.nio.ByteBuffer)
	 * 
	 * @param buffer
	 * @return
	 * @throws AioDecodeException
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public PacketMeta<ImPacket> decode(ByteBuffer buffer, ChannelContext<Object, ImPacket, Object> channelContext) throws AioDecodeException
	{
		int needBufferLength = -1;

		int readableLength = buffer.limit();
		if (readableLength < ImPacket.HEADER_LENGHT)
		{
			return null;
		}

		ImPacket imPacket = null;

		@SuppressWarnings("unused")
		byte version = buffer.get();
		
		int bodyLength = buffer.getInt();

		if (bodyLength > ImPacket.MAX_LENGTH_OF_BODY || bodyLength < 0)
		{
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getRemoteNode());
		}

		short command = buffer.getShort();
		
		int seq = buffer.getInt();
		
		@SuppressWarnings("unused")
		int reserve = buffer.getInt();//保留字段

		if (command < 0)
		{
			throw new AioDecodeException("command [" + command + "] is not right");
		}

		Boolean printbodylength = Boolean.getBoolean("tt_nio_printbodylength");
		if (printbodylength == true)
		{
			log.error("command:{}, bodylength:{}", command, bodyLength);
		}

		PacketMeta<ImPacket> packetMeta = new PacketMeta<>();
		needBufferLength = ImPacket.HEADER_LENGHT + bodyLength;
		int test = buffer.limit() - (needBufferLength);
		if (test < 0) // 不够消息体长度(剩下的buffe组不了消息体)
		{
			packetMeta.setNeedLength(needBufferLength);
			return packetMeta;
		} else
		{
			imPacket = new ImPacket();
			imPacket.setBodyLen(bodyLength);
			imPacket.setCommand(command);
			if (seq != 0)
			{
				imPacket.setSynSeq(seq);
			}

			if (bodyLength > 0)
			{
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}

			packetMeta.setPacketLenght(buffer.position());
			packetMeta.setPacket(imPacket);
			return packetMeta;

		}


	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#onClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public void onClose(ChannelContext<Object, ImPacket, Object> channelContext, Throwable throwable, String remark)
	{
		// TODO Auto-generated method stub
		
	}

}
