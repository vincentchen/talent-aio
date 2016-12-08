/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午1:31:04
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.intf.SendListener;
import com.talent.aio.common.stat.GroupStat;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午1:31:04
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class WriteCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<Integer, ChannelContext<Ext, P, R>>
{
	
	private static Logger log = LoggerFactory.getLogger(WriteCompletionHandler.class);
	
	private P packet;
	
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public WriteCompletionHandler(P packet)
	{
		this.packet = packet;
	}


	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public static void main(String[] args)
	{

	}


	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param result
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void completed(Integer result, ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getSendSemaphore().release();
		if (result > 0)
		{
			GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
			GroupStat groupStat = groupContext.getGroupStat();
			SendListener<Ext, P, R> sendListener = groupContext.getSendListener();
			groupStat.getSentPacket().incrementAndGet();
			groupStat.getSentBytes().addAndGet(result);
			channelContext.getStat().setTimeLatestSentMsg(SystemTimer.currentTimeMillis());
			if (sendListener != null)
			{
				sendListener.onAfterSent(channelContext, packet, result);
			}
		} else if (result == 0)
		{
			log.error("发送长度为{}", result);
			Aio.close(channelContext, "写数据返回:" + result);
		} else if (result < 0)
		{
			log.error("发送长度为{}", result);
			Aio.close(channelContext, "写数据返回:" + result);
		}
		
		
	}


	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void failed(Throwable exc, ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getSendSemaphore().release();
		Aio.close(channelContext, exc, "写数据时发生异常");
	}

}
