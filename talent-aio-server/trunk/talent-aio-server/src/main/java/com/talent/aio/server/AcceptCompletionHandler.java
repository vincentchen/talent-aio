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
package com.talent.aio.server;

import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.config.AioServerConfig;
import com.talent.aio.common.intf.Packet;
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
public class AcceptCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<AsynchronousSocketChannel, AioServer<Ext, P, R>>
{

	private static Logger log = LoggerFactory.getLogger(AioServer.class);

	private AioServerConfig<Ext, P, R> aioServerConfig = null;

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public AcceptCompletionHandler(AioServerConfig<Ext, P, R> aioServerConfig)
	{
		this.aioServerConfig = aioServerConfig;
	}

	//	private static AcceptCompletionHandler<?, ?> instance = null;
	//
	//	/**
	//	 * @return
	//	 */
	//	public static AcceptCompletionHandler<?, ?> getInstance()
	//	{
	//		if (instance == null)
	//		{
	//			synchronized (AcceptCompletionHandler.class)
	//			{
	//				if (instance == null)
	//				{
	//					instance = new AcceptCompletionHandler<>();
	//				}
	//			}
	//		}
	//		return instance;
	//	}

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
	 * @重写时间: 2016年11月16日 下午1:28:05
	 * 
	 */
	@Override
	public void completed(AsynchronousSocketChannel result, AioServer<Ext, P, R> aioServer)
	{
		//		Semaphore semaphore = aioServerConfig.getAcceptSemaphore();
		//		
		//		try
		//		{
		//			long starttime = SystemTimer.currentTimeMillis();
		//			semaphore.acquire();
		//			long endtime = SystemTimer.currentTimeMillis();
		//			long cost = (endtime - starttime);
		//			if (cost > 100)
		//			{
		//				log.error("等接受权限耗时:{}ms", cost);
		//			}
		//			aioServer.getServerSocketChannel().accept(aioServer, this);
		//		} catch (Exception e1)
		//		{
		//			log.error("监听出现异常，无法继续监控Tcp连接", e1);
		//		} finally
		//		{
		//			semaphore.release();
		//		}

		Semaphore semaphore1 = aioServerConfig.getReadSemaphore();
		try
		{
			long starttime = SystemTimer.currentTimeMillis();
			semaphore1.acquire();
			long endtime = SystemTimer.currentTimeMillis();
			long cost = (endtime - starttime);
			if (cost > 100)
			{
				log.error("等读权限耗时:{}ms", cost);
			}

			AioServerConfig<Ext, P, R> aioServerConfig = aioServer.getAioServerConfig();

			result.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			result.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			result.setOption(StandardSocketOptions.SO_SNDBUF, 32 * 1024);
			result.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

			ChannelContext<Ext, P, R> channelContext = new ChannelContext<>(aioServerConfig, result);

			ReadCompletionHandler<Ext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
			result.read(readCompletionHandler.getByteBuffer(), channelContext, readCompletionHandler);
		} catch (Exception e)
		{
			log.error("", e);
		} finally
		{
			semaphore1.release();
		}
	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param aioServer
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:28:05
	 * 
	 */
	@Override
	public void failed(Throwable exc, AioServer<Ext, P, R> aioServer)
	{
		String ip = aioServer.getAioServerConfig().getIp();
		String ipstr = StringUtils.isNotBlank(ip) ? ip : "0.0.0.0";
		ipstr += ":" + aioServer.getAioServerConfig().getPort();
		log.error("[" + ipstr + "]监听出现异常，无法继续监控Tcp连接", exc);
	}

}
