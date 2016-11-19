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
@Deprecated
public class WriteCompletionHandler<Ext, T extends Packet, R> implements CompletionHandler<Integer, ChannelContext<Ext, T, R>>
{
	
	private static Logger log = LoggerFactory.getLogger(WriteCompletionHandler.class);
	
	
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public WriteCompletionHandler()
	{
		
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
	public void completed(Integer result, ChannelContext<Ext, T, R> channelContext)
	{
		log.debug("数据写完成:{}", result);
		if (result > 0)
		{
			
		} else if (result == 0)
		{
			
		} else if (result < 0)
		{
			
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
	public void failed(Throwable exc, ChannelContext<Ext, T, R> attachment)
	{
		Aio.close(attachment, exc, "写数据时发生异常");
		
	}

}
