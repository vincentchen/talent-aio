/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月8日 下午1:22:27
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.common;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.intf.SendListener;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月8日 下午1:22:27
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月8日 | tanyaowu | 新建类
 *
 */
public class ImSendListener implements SendListener<Object, ImPacket, Object>
{

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:22:27
	 * 
	 */
	public ImSendListener()
	{}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:22:27
	 * 
	 */
	public static void main(String[] args)
	{}



	/** 
	 * @see com.talent.aio.common.intf.SendListener#onAfterSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param sentSize
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月8日 下午1:23:18
	 * 
	 */
	@Override
	public void onAfterSent(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int sentSize)
	{
		com.talent.aio.examples.im.common.CommandStat.getCount(packet.getCommand()).incrementAndGet();
	}

}
