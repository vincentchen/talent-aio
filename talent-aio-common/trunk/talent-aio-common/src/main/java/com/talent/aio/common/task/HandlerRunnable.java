/**
 * 
 */
package com.talent.aio.common.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.BatchQueueRunnable;

/**
 * 
 * @author 谭耀武
 * @date 2012-08-09
 * 
 */
public class HandlerRunnable<Ext, P extends Packet, R> extends BatchQueueRunnable<P>
{
	private static final Logger log = LoggerFactory.getLogger(HandlerRunnable.class);

	//	private DecodeRunnable decodeRunnable = null;

	// private String msgType = null;

	private ChannelContext<Ext, P, R> channelContext = null;

	public HandlerRunnable(ChannelContext<Ext, P, R> channelContext/**, DecodeRunnable decodeRunnable*/
	)
	{
		this.setChannelContext(channelContext);
		//		this.decodeRunnable = decodeRunnable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	//	@Override
	//	public void runTask()
	//	{
	//		List<Packet> packets = null;
	//		ConcurrentLinkedQueue<List<Packet>> queue = getMsgQueue();
	//		while (queue.size() > 0)
	//		{
	//			if (waitingRunCount() > 0)
	//			{
	//				break;
	//			}
	//
	//			packets = queue.poll();
	//
	//			// packet = msgQueue.poll();
	//			// if (packet == null)
	//			// {
	//			// continue;
	//			// }
	//			try
	//			{
	//				doPackets(packets, decodeRunnable.getChannelContext());
	//
	//			} catch (Exception e)
	//			{
	//				log.error(packets.toString());
	//				throw new RuntimeException(e.toString(), e);
	//			}
	//
	//			if (log.isDebugEnabled())
	//			{
	//				log.debug("total processed[" + StatVo.getAllProcessedPacketCount().get() + "];" + "["
	//						+ decodeRunnable.getChannelContext().getId() + "] processed["
	//						+ decodeRunnable.getProcessedPacketCount().get() + "],waiting["
	//						+ decodeRunnable.getMsgQueue().size() + "]；"
	//						+ channelContext.getAioHandler().getClass().getName() + " processed["
	//						+ this.getProcessedPacketCount().get() + "],waiting[" + queue.size() + "]");
	//			}
	//		}
	//	}

	private int doPacket(P packet) throws Exception
	{
		int ret = 0;
		if (packet != null)
		{
			try
			{
				channelContext.getAioConfig().getAioHandler().handler(packet, channelContext);//.onReceived(packet, channelContext);
				ret++;
			} catch (Exception e)
			{
				log.error(e.toString(), e);
				return ret;
			} finally
			{
				
			}
		}
		return ret;
	}

	/**
	 * 添加要处理的消息
	 * 
	 * @param packet
	 */
	public void addMsg(P packet)
	{
		getMsgQueue().add(packet);
	}

	/**
	 * 清空处理的队列消息
	 */
	public void clearMsgQueue()
	{
		getMsgQueue().clear();
	}

	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	public void setChannelContext(ChannelContext<Ext, P, R> channelContext)
	{
		this.channelContext = channelContext;
	}

	@Override
	public void runBatch(List<P> packetss)
	{
		for (P packet : packetss)
		{
			try
			{
				doPacket(packet);
			} catch (Exception e)
			{
				log.error(packet.toString());
				throw new RuntimeException(e.toString(), e);
			}
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()).append(":");
		builder.append(channelContext.toString());
		return builder.toString();
	}

	// public String getMsgType()
	// {
	// return msgType;
	// }
	//
	// public void setMsgType(String msgType)
	// {
	// this.msgType = msgType;
	// }

}
