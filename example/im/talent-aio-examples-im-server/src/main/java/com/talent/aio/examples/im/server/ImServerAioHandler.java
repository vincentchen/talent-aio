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
package com.talent.aio.examples.im.server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.server.handler.AuthHandler;
import com.talent.aio.examples.im.server.handler.ChatHandler;
import com.talent.aio.examples.im.server.handler.HeartbeatHandler;
import com.talent.aio.examples.im.server.handler.ImBsAioHandlerIntf;
import com.talent.aio.examples.im.server.handler.JoinHandler;
import com.talent.aio.server.intf.ServerAioHandler;

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
public class ImServerAioHandler implements ServerAioHandler<Object, ImPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(ImServerAioHandler.class);
	
	
	private static Map<Short, ImBsAioHandlerIntf> handlerMap = new HashMap<>();
	static
	{
		handlerMap.put(Command.AUTH_REQ, new AuthHandler());
		handlerMap.put(Command.CHAT_REQ, new ChatHandler());
		handlerMap.put(Command.JOIN_GROUP_REQ, new JoinHandler());
		handlerMap.put(Command.HEARTBEAT_REQ, new HeartbeatHandler());
		
	}
	
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public ImServerAioHandler()
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
			Object obj = handler.handler(packet, channelContext);
			CommandStat.getCount(command).handled.incrementAndGet();
			return obj;
		} else
		{
			CommandStat.getCount(command).handled.incrementAndGet();
			log.warn("找不到对应的命令码[{}]处理类", command);
			return null;
		}

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
		if (body != null)
		{
			bodyLen = body.length;
		}

		int allLen = ImPacket.HEADER_LENGHT + bodyLen;
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(channelContext.getGroupContext().getByteOrder());
		

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
	public ImPacket decode(ByteBuffer buffer, ChannelContext<Object, ImPacket, Object> channelContext) throws AioDecodeException
	{
		int readableLength = buffer.limit() - buffer.position();
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
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
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

//		PacketMeta<ImPacket> packetMeta = new PacketMeta<>();
		int neededLength = ImPacket.HEADER_LENGHT + bodyLength;
		int test = readableLength - neededLength;
		if (test < 0) // 不够消息体长度(剩下的buffe组不了消息体)
		{
//			packetMeta.setNeededLength(neededLength);
			return null;
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

//			packetMeta.setPacket(imPacket);
			return imPacket;

		}


	}

}
