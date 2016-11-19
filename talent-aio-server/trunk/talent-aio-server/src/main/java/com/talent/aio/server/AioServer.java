/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:35:17
 *
 * **************************************************************************
 */
package com.talent.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.config.AioServerConfig;
import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午11:35:17
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class AioServer<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(AioServer.class);

	private ObjWithReadWriteLock<Map<String, ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>>>> remoteipMap;

	private AioServerConfig<Ext, P, R> aioServerConfig;
	private AsynchronousChannelGroup channelGroup;
	private AsynchronousServerSocketChannel serverSocketChannel;
	private InetSocketAddress inetSocketAddress;

	/**
	 * @return the channelGroup
	 */
	public AsynchronousChannelGroup getChannelGroup()
	{
		return channelGroup;
	}

	/**
	 * @return the serverSocketChannel
	 */
	public AsynchronousServerSocketChannel getServerSocketChannel()
	{
		return serverSocketChannel;
	}

	/**
	 * @return the acceptCompletionHandler
	 */
	public AcceptCompletionHandler<Ext, P, R> getAcceptCompletionHandler()
	{
		return acceptCompletionHandler;
	}

	private AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler;

	/**
	 * @param ip 可以为空
	 * @param port 
	 * @param aioDecoder
	 * @param aioEncoder
	 * @param aioHandler
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:09:28
	 * 
	 */
	public AioServer(AioServerConfig<Ext, P, R> aioServerConfig)
	{
		super();
		this.aioServerConfig = aioServerConfig;
	}

	public void start() throws IOException
	{
		
		
		
		String ip = aioServerConfig.getIp();
		int port = aioServerConfig.getPort();
		ExecutorService groupExecutor = aioServerConfig.getGroupExecutor();
//		if (groupExecutor == null)
//		{
//			SynchronousQueue<Runnable> poolQueue = new SynchronousQueue<Runnable>();
//			groupExecutor = new ThreadPoolExecutor(4, 500, 30, TimeUnit.SECONDS, poolQueue, DefaultThreadFactory.getInstance("t-aio-server-group"));
//			aioServerConfig.setGroupExecutor(groupExecutor);
//		}

		channelGroup = AsynchronousChannelGroup.withCachedThreadPool(groupExecutor, 10);
		serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);

		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 64 * 1024);
//		serverSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, 64 * 1024);
//		serverSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
		

		acceptCompletionHandler = new AcceptCompletionHandler<>(aioServerConfig);

		if (StringUtils.isBlank(ip))
		{
			inetSocketAddress = new InetSocketAddress(port);
		} else
		{
			inetSocketAddress = new InetSocketAddress(ip, port);
		}

		serverSocketChannel.bind(inetSocketAddress, 0);
		
		String ipstr = StringUtils.isNotBlank(ip) ? ip : "0.0.0.0";
		log.info("start listening on " + ipstr + ":" + port);

		
		
//		serverSocketChannel.accept(this, acceptCompletionHandler);
		
		ThreadPoolExecutor threadPoolExecutor = this.getAioServerConfig().getAcceptExecutor();
//		int corePoolSize = threadPoolExecutor.getCorePoolSize();
//		for (int i = 0; i < corePoolSize; i++)
//		{
//			
//		}
		AcceptRunnable<Ext, P, R> acceptRunnable = new AcceptRunnable<>(this);
		threadPoolExecutor.execute(acceptRunnable);
		
		
//		Thread thread = new Thread(new Runnable(){
//			@Override
//			public void run()
//			{
//				Future<AsynchronousSocketChannel> future = serverSocketChannel.accept();
//				try
//				{
//					AsynchronousSocketChannel asynchronousSocketChannel = future.get();
//					acceptCompletionHandler.completed(asynchronousSocketChannel, AioServer.this);
//				} catch (Throwable e)
//				{
//					acceptCompletionHandler.failed(e, AioServer.this);
//				}				
//			}}, "t-aio-server");
//		thread.start();
		

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:35:17
	 * 
	 */
	public static void main(String[] args)
	{
	}

	/**
	 * @return the aioServerConfig
	 */
	public AioServerConfig<Ext, P, R> getAioServerConfig()
	{
		return aioServerConfig;
	}

	/**
	 * @param aioServerConfig the aioServerConfig to set
	 */
	public void setAioServerConfig(AioServerConfig<Ext, P, R> aioServerConfig)
	{
		this.aioServerConfig = aioServerConfig;
	}

	/**
	 * @param acceptCompletionHandler the acceptCompletionHandler to set
	 */
	public void setAcceptCompletionHandler(AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler)
	{
		this.acceptCompletionHandler = acceptCompletionHandler;
	}

}
