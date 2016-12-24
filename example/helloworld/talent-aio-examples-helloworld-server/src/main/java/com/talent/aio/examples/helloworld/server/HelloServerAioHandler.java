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
package com.talent.aio.examples.helloworld.server;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.exception.AioDecodeException;
import com.talent.aio.examples.helloworld.common.HelloPacket;
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
public class HelloServerAioHandler implements ServerAioHandler<Object, HelloPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(HelloServerAioHandler.class);



	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public HelloServerAioHandler()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public static void main(String[] args)
	{
	}

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
	public Object handler(HelloPacket packet, ChannelContext<Object, HelloPacket, Object> channelContext) throws Exception
	{
		byte[] body = packet.getBody();
		if (body != null)
		{
			String str = new String(body, HelloPacket.CHARSET);
			System.out.println("收到消息：" + str);
			
			HelloPacket resppacket = new HelloPacket();
			packet.setBody(("收到了你的消息，你的消息是:" + str).getBytes(HelloPacket.CHARSET));
			Aio.send(channelContext, packet);
		}
		
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
	public ByteBuffer encode(HelloPacket packet, ChannelContext<Object, HelloPacket, Object> channelContext)
	{
		byte[] body = packet.getBody();
		int bodyLen = 0;
		if (body != null)
		{
			bodyLen = body.length;
		}

		int allLen = HelloPacket.HEADER_LENGHT + bodyLen;
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(channelContext.getGroupContext().getByteOrder());

		buffer.putInt(bodyLen);

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
	public HelloPacket decode(ByteBuffer buffer, ChannelContext<Object, HelloPacket, Object> channelContext) throws AioDecodeException
	{
		int readableLength = buffer.limit() - buffer.position();
		if (readableLength < HelloPacket.HEADER_LENGHT)
		{
			return null;
		}

		HelloPacket imPacket = null;


		int bodyLength = buffer.getInt();

		if (bodyLength < 0)
		{
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}



		//		PacketMeta<HelloWorldPacket> packetMeta = new PacketMeta<>();
		int neededLength = HelloPacket.HEADER_LENGHT + bodyLength;
		int test = readableLength - neededLength;
		if (test < 0) // 不够消息体长度(剩下的buffe组不了消息体)
		{
			//			packetMeta.setNeededLength(neededLength);
			return null;
		} else
		{
			imPacket = new HelloPacket();
//			imPacket.setBodyLen(bodyLength);


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
