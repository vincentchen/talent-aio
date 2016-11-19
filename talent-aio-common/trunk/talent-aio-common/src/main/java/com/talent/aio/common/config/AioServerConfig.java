/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月16日 上午10:06:33
 *
 * **************************************************************************
 */
package com.talent.aio.common.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.threadpool.DefaultThreadFactory;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月16日 上午10:06:33
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月16日 | tanyaowu | 新建类
 *
 */
public class AioServerConfig<Ext, P extends Packet, R> extends AioConfig<Ext, P, R>
{

	private String ip;
	private int port;
	private ExecutorService groupExecutor = null;
	private ThreadPoolExecutor acceptExecutor = null;
	//	ThreadPoolExecutor(int corePoolSize,
	//            int maximumPoolSize,
	//            long keepAliveTime,
	//            TimeUnit unit,
	//            BlockingQueue<Runnable> workQueue,
	//            ThreadFactory threadFactory)

	
	/**
	 * Instantiates a new aio server config.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 */
	public AioServerConfig(String ip, int port, AioHandler<Ext, P, R> aioHandler)
	{
		this(ip, port, aioHandler,
				new ThreadPoolExecutor(4, 200, 30, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), DefaultThreadFactory.getInstance("t-aio-server-group")));
	}

	public AioServerConfig(String ip, int port, AioHandler<Ext, P, R> aioHandler, ExecutorService groupExecutor)
	{
		super(aioHandler);
		this.ip = ip;
		this.port = port;
		this.groupExecutor = groupExecutor;
		
		
//		SynchronousQueue<Runnable> decodePoolQueue = new SynchronousQueue<Runnable>();
//		acceptExecutor = new SynThreadPoolExecutor<SynRunnableIntf>(threadPoolInitNum, threadPoolMaxNum, threadPoolKeepAliveTime, decodePoolQueue, "t-aio-accept");
		this.acceptExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                DefaultThreadFactory.getInstance("t-aio-accept"));
	}

	/**
	 * Instantiates a new aio server config.
	 *
	 * @param ip the ip
	 * @param port the port
	 * @param aioHandler the aio handler
	 * @param decodeExecutor the decode executor
	 * @param handlerExecutorHighPrior the handler executor high prior
	 * @param handlerExecutorLowPrior the handler executor low prior
	 * @param sendExecutorHighPrior the send executor high prior
	 * @param sendExecutorLowPrior the send executor low prior
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:23:47
	 */
	public AioServerConfig(String ip, int port, AioHandler<Ext, P, R> aioHandler,
			SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor, SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior,
			SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorLowPrior, SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior,
			SynThreadPoolExecutor<SynRunnableIntf> sendExecutorLowPrior)
	{
		super(aioHandler, decodeExecutor, handlerExecutorHighPrior, handlerExecutorLowPrior, sendExecutorHighPrior, sendExecutorLowPrior);
		this.ip = ip;
		this.port = port;
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:06:33
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * @return the ip
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * @return the groupExecutor
	 */
	public ExecutorService getGroupExecutor()
	{
		return groupExecutor;
	}

	/**
	 * @param groupExecutor the groupExecutor to set
	 */
	public void setGroupExecutor(ExecutorService groupExecutor)
	{
		this.groupExecutor = groupExecutor;
	}

	/**
	 * @return the acceptExecutor
	 */
	public ThreadPoolExecutor getAcceptExecutor()
	{
		return acceptExecutor;
	}

	/**
	 * @param acceptExecutor the acceptExecutor to set
	 */
	public void setAcceptExecutor(ThreadPoolExecutor acceptExecutor)
	{
		this.acceptExecutor = acceptExecutor;
	}

}
