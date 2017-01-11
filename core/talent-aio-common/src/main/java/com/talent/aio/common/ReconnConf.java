/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年1月11日 下午7:07:44
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.concurrent.LinkedBlockingQueue;

import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月11日 下午7:07:44
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月11日 | tanyaowu | 新建类
 *
 */
public class ReconnConf<Ext, P extends Packet, R>
{
	/**
	 * @param interval
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:13:28
	 * 
	 */
	public ReconnConf(long interval)
	{
		super();
		this.interval = interval;
	}

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午7:13:51
	 * 
	 */
	public ReconnConf()
	{
		super();
	}

	private long interval = 5000L;
	private LinkedBlockingQueue<ChannelContext<Ext, P, R>> queue = new LinkedBlockingQueue<ChannelContext<Ext, P, R>>();

	/**
	 * @return the interval
	 */
	public long getInterval()
	{
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval)
	{
		this.interval = interval;
	}

	/**
	 * @return the queue
	 */
	public LinkedBlockingQueue<ChannelContext<Ext, P, R>> getQueue()
	{
		return queue;
	}

}
