package com.talent.aio.server;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.server.intf.ServerAioListener;

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

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public AcceptCompletionHandler()
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
	 * @param asynchronousSocketChannel
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:28:05
	 * 
	 */
	@Override
	public void completed(AsynchronousSocketChannel asynchronousSocketChannel, AioServer<Ext, P, R> aioServer)
	{
		try
		{
			AsynchronousServerSocketChannel serverSocketChannel = aioServer.getServerSocketChannel();
			serverSocketChannel.accept(aioServer, this);
			
			ServerGroupContext<Ext, P, R> serverGroupContext = aioServer.getServerGroupContext();
			ServerGroupStat serverGroupStat = serverGroupContext.getServerGroupStat();
			serverGroupStat.getAccepted().incrementAndGet();

			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 32 * 1024);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 32 * 1024);
			asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

			ServerChannelContext<Ext, P, R> channelContext = new ServerChannelContext<>(serverGroupContext, asynchronousSocketChannel);
			channelContext.setServerNode(aioServer.getServerNode());
			ServerAioListener<Ext, P, R> serverAioListener = serverGroupContext.getServerAioListener();
			try
			{
				serverAioListener.onAfterConnected(channelContext, true, false);
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			}

			ReadCompletionHandler<Ext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
			ByteBuffer newByteBuffer = ByteBuffer.allocate(channelContext.getGroupContext().getReadBufferSize());
			asynchronousSocketChannel.read(newByteBuffer, newByteBuffer, readCompletionHandler);
		} catch (Exception e)
		{
			log.error("", e);
		} finally
		{
			
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
		AsynchronousServerSocketChannel serverSocketChannel = aioServer.getServerSocketChannel();
		serverSocketChannel.accept(aioServer, this);

		log.error("[" + aioServer.getServerNode() + "]监听出现异常", exc);

	}

}
