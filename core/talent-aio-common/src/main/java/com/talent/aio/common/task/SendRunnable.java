package com.talent.aio.common.task;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.WriteCompletionHandler;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractQueueRunnable;
import com.talent.aio.common.utils.AioUtils;
import com.talent.aio.common.utils.ByteBufferUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class SendRunnable<Ext, P extends Packet, R> extends AbstractQueueRunnable<P>
{

	private static final Logger log = LoggerFactory.getLogger(SendRunnable.class);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}

	private ChannelContext<Ext, P, R> channelContext = null;

	/**
	 * 
	 * @param socketChannelId
	 */
	public SendRunnable(ChannelContext<Ext, P, R> channelContext, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
	}

	/**
	 * 清空消息队列
	 */
	public void clearMsgQueue()
	{
		msgQueue.clear();
	}

	public void sendPacket(P packet)
	{
		GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
		ByteBuffer byteBuffer = getByteBuffer(packet, groupContext, groupContext.getAioHandler());
		int packetCount = 1;
		sendByteBuffer(byteBuffer, packetCount, packet);
	}

	/**
	 * 
	 * @param byteBuffer
	 * @param packetCount
	 * @param packets
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月4日 下午9:10:01
	 *
	 */
	public void sendByteBuffer(ByteBuffer byteBuffer, Integer packetCount, Object packets)
	{
		if (byteBuffer == null)
		{
			log.error("byteBuffer is null");
			return;
		}

		if (!AioUtils.checkBeforeIO(channelContext))
		{
			return;
		}

		byteBuffer.flip();
		AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
		WriteCompletionHandler<Ext, P, R> writeCompletionHandler = channelContext.getWriteCompletionHandler();
		try
		{
			writeCompletionHandler.getWriteSemaphore().acquire();
		} catch (InterruptedException e)
		{
			log.error(e.toString(), e);
		}
		asynchronousSocketChannel.write(byteBuffer, packets, writeCompletionHandler);
		channelContext.getStat().setLatestTimeOfSentPacket(SystemTimer.currentTimeMillis());
	}

	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()).append(":");
		builder.append(channelContext.toString());
		return builder.toString();
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#runTask()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月5日 下午2:57:33
	 * 
	 */
	@Override
	public void runTask()
	{
		int queueSize = msgQueue.size();
		if (queueSize == 0)
		{
			return;
		}
		if (queueSize >= 2000)
		{
			queueSize = 1000;
		}

		P packet = null;
		GroupContext<Ext, P, R> groupContext = this.channelContext.getGroupContext();
		AioHandler<Ext, P, R> aioHandler = groupContext.getAioHandler();

		if (queueSize > 1)
		{
			ByteBuffer[] byteBuffers = new ByteBuffer[queueSize];
			int allBytebufferCapacity = 0;

			int packetCount = 0;
			List<P> packets = new ArrayList<>();
			for (int i = 0; i < queueSize; i++)
			{
				if ((packet = msgQueue.poll()) != null)
				{
					//					ByteBuffer byteBuffer = packet.getPreEncodedByteBuffer();//aioHandler.encode(packet, groupContext, channelContext);
					//					if (byteBuffer != null)
					//					{
					//						byteBuffer = ByteBufferUtils.copy(byteBuffer, 0, byteBuffer.limit());
					//					} else
					//					{
					//						byteBuffer = aioHandler.encode(packet, groupContext, channelContext);
					//					}

					ByteBuffer byteBuffer = getByteBuffer(packet, groupContext, aioHandler);

					packets.add(packet);
					allBytebufferCapacity += byteBuffer.limit();
					packetCount++;
					byteBuffers[i] = byteBuffer;
				} else
				{
					break;
				}
			}

			ByteBuffer allByteBuffer = ByteBuffer.allocate(allBytebufferCapacity);
			byte[] dest = allByteBuffer.array();
			for (ByteBuffer byteBuffer : byteBuffers)
			{
				if (byteBuffer != null)
				{
					int length = byteBuffer.limit();
					int position = allByteBuffer.position();
					System.arraycopy(byteBuffer.array(), 0, dest, position, length);
					allByteBuffer.position(position + length);
				}
			}
			sendByteBuffer(allByteBuffer, packetCount, packets);

		} else
		{
			if ((packet = msgQueue.poll()) != null)
			{
				sendPacket(packet);
			}
		}
	}

	private ByteBuffer getByteBuffer(P packet, GroupContext<Ext, P, R> groupContext, AioHandler<Ext, P, R> aioHandler)
	{
		ByteBuffer byteBuffer = packet.getPreEncodedByteBuffer();
		if (byteBuffer != null)
		{
			byteBuffer = byteBuffer.duplicate();
		} else
		{
			byteBuffer = aioHandler.encode(packet, groupContext, channelContext);
		}
		return byteBuffer;
	}

}
