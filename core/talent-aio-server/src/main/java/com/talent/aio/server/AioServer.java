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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Node;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

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

	private ServerGroupContext<Ext, P, R> serverGroupContext;

	private AsynchronousServerSocketChannel serverSocketChannel;

	private Node serverNode;

	private boolean isWaitingStop = false;

	/**
	 * 
	 * @param serverGroupContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月2日 下午5:53:06
	 *
	 */
	public AioServer(ServerGroupContext<Ext, P, R> serverGroupContext)
	{
		super();
		this.serverGroupContext = serverGroupContext;
	}

	/**
	 * @return the serverGroupContext
	 */
	public ServerGroupContext<Ext, P, R> getServerGroupContext()
	{
		return serverGroupContext;
	}

	/**
	 * @return the serverSocketChannel
	 */
	public AsynchronousServerSocketChannel getServerSocketChannel()
	{
		return serverSocketChannel;
	}

	/**
	 * @param serverGroupContext the serverGroupContext to set
	 */
	public void setServerGroupContext(ServerGroupContext<Ext, P, R> serverGroupContext)
	{
		this.serverGroupContext = serverGroupContext;
	}

	public void start(String serverIp, int serverPort) throws IOException
	{
		this.serverNode = new Node(serverIp, serverPort);
		ExecutorService groupExecutor = serverGroupContext.getGroupExecutor();

		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(groupExecutor);
		serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);

		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 64 * 1024);

		InetSocketAddress listenAddress = null;

		if (StringUtils.isBlank(serverIp))
		{
			listenAddress = new InetSocketAddress(serverPort);
		} else
		{
			listenAddress = new InetSocketAddress(serverIp, serverPort);
		}

		serverSocketChannel.bind(listenAddress, 0);

		AcceptCompletionHandler<Ext, P, R> acceptCompletionHandler = serverGroupContext.getAcceptCompletionHandler();
		serverSocketChannel.accept(this, acceptCompletionHandler);

		String ipstr = StringUtils.isNotBlank(serverIp) ? serverIp : "0.0.0.0";
		System.out.println("start listening on " + ipstr + ":" + serverPort);
	}

	/**
	 * 
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月11日 上午8:04:04
	 *
	 */
	public boolean stop()
	{
		isWaitingStop = true;
		boolean ret = true;
		ExecutorService groupExecutor = serverGroupContext.getGroupExecutor();
		SynThreadPoolExecutor<SynRunnableIntf> executor = serverGroupContext.getDecodeExecutor();
		groupExecutor.shutdown();
		executor.shutdown();
		try
		{
			ret = ret && groupExecutor.awaitTermination(6000, TimeUnit.SECONDS);
		} catch (InterruptedException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		
		try
		{
			ret = ret && executor.awaitTermination(6000, TimeUnit.SECONDS);
		} catch (InterruptedException e)
		{
			log.error(e.getLocalizedMessage(), e);
		}
		
		return ret;
	}

	/**
	 * @return the serverNode
	 */
	public Node getServerNode()
	{
		return serverNode;
	}

	/**
	 * @return the isWaitingStop
	 */
	public boolean isWaitingStop()
	{
		return isWaitingStop;
	}

	/**
	 * @param isWaitingStop the isWaitingStop to set
	 */
	public void setWaitingStop(boolean isWaitingStop)
	{
		this.isWaitingStop = isWaitingStop;
	}

	//	/**
	//	 * @param serverNode the serverNode to set
	//	 */
	//	public void setServerNode(Node serverNode)
	//	{
	//		this.serverNode = serverNode;
	//	}
}
