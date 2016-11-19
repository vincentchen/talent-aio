/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月19日 下午1:44:39
 *
 * **************************************************************************
 */
package com.talent.aio.server;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.AbstractSynRunnable;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月19日 下午1:44:39
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月19日 | tanyaowu | 新建类
 *
 */
public class AcceptRunnable <Ext, P extends Packet, R> extends AbstractSynRunnable
{

	private AioServer<Ext, P, R> aioServer;
	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#runTask()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月19日 下午1:46:31
	 * 
	 */
	@Override
	public void runTask()
	{
		AsynchronousServerSocketChannel serverSocketChannel = aioServer.getServerSocketChannel();
		AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler = aioServer.getAcceptCompletionHandler();

		Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();
		AsynchronousSocketChannel asynchronousSocketChannel = null;
		try
		{
			asynchronousSocketChannel = future.get();
		} catch (Throwable e)
		{
			acceptCompletionHandler.failed(e, aioServer);
		}
		
//		AcceptRunnable<Ext, P, R> acceptRunnable = new AcceptRunnable<>(aioServer);
		aioServer.getAioServerConfig().getAcceptExecutor().execute(this);
		
		if (asynchronousSocketChannel != null)
		{
			acceptCompletionHandler.completed(asynchronousSocketChannel, aioServer);
		}
	}
	/**
	 * @param aioServer
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月19日 下午1:47:04
	 * 
	 */
	public AcceptRunnable(AioServer<Ext, P, R> aioServer)
	{
		super();
		this.aioServer = aioServer;
	}


}
