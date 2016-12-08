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
package com.talent.aio.common;

import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.intf.SendListener;
import com.talent.aio.common.maintain.ClientNodes;
import com.talent.aio.common.maintain.Connections;
import com.talent.aio.common.maintain.Groups;
import com.talent.aio.common.maintain.Users;
import com.talent.aio.common.stat.GroupStat;
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
public abstract class GroupContext<Ext, P extends Packet, R>
{
	static Logger log = LoggerFactory.getLogger(GroupContext.class);
	//	/**
	//	 * 同时接受请求的并发数
	//	 */
	//	private int acceptConcurrentCount = 100;
	//	/**
	//	 * 同时关闭连接的并发数
	//	 */
	//	private int closeConcurrentCount = 100;
	//	/**
	//	 * 同时读的并发数
	//	 */
	//	private int readConcurrentCount = 100;

	//	private Semaphore acceptSemaphore = new Semaphore(acceptConcurrentCount);
	//	private Semaphore closeSemaphore = new Semaphore(closeConcurrentCount);
	//	private Semaphore readSemaphore = new Semaphore(readConcurrentCount);

	public static final int corePoolSize = Runtime.getRuntime().availableProcessors() * 1;

	/**
	 * 心跳超时时间(单位: 毫秒)
	 */
	public static final long HEARTBEAT_TIMEOUT = 1000 * 60;

	public static final int maximumPoolSize = corePoolSize * 4;

	public static final long keepAliveTime = 90L;

	private AioHandler<Ext, P, R> aioHandler;
	
	private SendListener<Ext, P, R> sendListener;

	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
	

	/**
	 * 心跳超时时间(单位: 毫秒)
	 */
	protected long heartbeatTimeout = HEARTBEAT_TIMEOUT;
	/**
	 * 解码线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor = null;

	/**
	 * 关闭连接的线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> closeExecutor = null;

	/**
	 * 高优先级的业务处理线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior = null;
	/**
	 * 低优先级的业务处理线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior = null;

	/**
	 * 高优先级的消息发送线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior = null;
	/**
	 * 低优先级的消息发送线程池
	 */
	private SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior = null;

	protected ClientNodes<Ext, P, R> clientNodes = new ClientNodes<>();
	protected Connections<Ext, P, R> connections = new Connections<>();
	protected Groups<Ext, P, R> groups = new Groups<>();
	protected Users<Ext, P, R> users = new Users<>();
	
	

	protected String id;

	protected GroupContext()
	{
	}

	/**
	 * @param ip
	 * @param port
	 * @param aioHandler
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 上午10:21:58
	 * 
	 */
	public GroupContext(String id, AioHandler<Ext, P, R> aioHandler, SendListener<Ext, P, R> sendListener)
	{
		super();
		this.id = id;
		this.aioHandler = aioHandler;
		this.sendListener = sendListener;
		//		SynchronousQueue<Runnable> decodePoolQueue = new SynchronousQueue<Runnable>();
		//		decodeExecutor = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, maximumPoolSize, keepAliveTime, decodePoolQueue, "t-aio-decode");
		//		decodeExecutor.prestartAllCoreThreads();
		//
		//		SynchronousQueue<Runnable> handlerPoolQueueHighPrior = new SynchronousQueue<Runnable>();
		//		handlerExecutorHighPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, maximumPoolSize, keepAliveTime, handlerPoolQueueHighPrior,
		//				DefaultThreadFactory.getInstance("t-aio-handler-high-prior", Thread.MAX_PRIORITY), "t-aio-handler-high-prior");
		//		handlerExecutorHighPrior.prestartAllCoreThreads();
		//
		//		SynchronousQueue<Runnable> handlerPoolQueueNormPrior = new SynchronousQueue<Runnable>();
		//		handlerExecutorNormPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, maximumPoolSize, keepAliveTime, handlerPoolQueueNormPrior,
		//				"t-aio-handler-low-prior");
		//		handlerExecutorNormPrior.prestartAllCoreThreads();
		//
		//		SynchronousQueue<Runnable> sendPoolQueueHighPrior = new SynchronousQueue<Runnable>();
		//		sendExecutorHighPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, maximumPoolSize, keepAliveTime, sendPoolQueueHighPrior,
		//				DefaultThreadFactory.getInstance("t-aio-send-high-prior", Thread.MAX_PRIORITY), "t-aio-send-high-prior");
		//		sendExecutorHighPrior.prestartAllCoreThreads();
		//
		//		SynchronousQueue<Runnable> sendPoolQueueNormPrior = new SynchronousQueue<Runnable>();
		//		sendExecutorNormPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, maximumPoolSize, keepAliveTime, sendPoolQueueNormPrior,
		//				"t-aio-send-low-prior");
		//		sendExecutorNormPrior.prestartAllCoreThreads();

		LinkedBlockingQueue<Runnable> poolQueueHighPrior = new LinkedBlockingQueue<Runnable>();
		SynThreadPoolExecutor<SynRunnableIntf> executorHighPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, corePoolSize, keepAliveTime, poolQueueHighPrior,
				DefaultThreadFactory.getInstance("t-aio-high-prior", Thread.MAX_PRIORITY), "t-aio-high-prior");
		executorHighPrior.prestartAllCoreThreads();

		LinkedBlockingQueue<Runnable> poolQueueNormPrior = new LinkedBlockingQueue<Runnable>();
		SynThreadPoolExecutor<SynRunnableIntf> executorNormPrior = new SynThreadPoolExecutor<SynRunnableIntf>(corePoolSize, corePoolSize, keepAliveTime, poolQueueNormPrior,
				DefaultThreadFactory.getInstance("t-aio-low-prior", Thread.NORM_PRIORITY), "t-aio-low-prior");
		executorNormPrior.prestartAllCoreThreads();

		decodeExecutor = executorNormPrior;
		closeExecutor = executorHighPrior;
		handlerExecutorHighPrior = executorHighPrior;
		handlerExecutorNormPrior = executorNormPrior;
		sendExecutorHighPrior = executorHighPrior;
		sendExecutorNormPrior = executorNormPrior;

		
	}

//	/**
//	 * @param ip
//	 * @param port
//	 * @param aioHandler
//	 * @param decodeExecutor
//	 * @param handlerExecutorHighPrior
//	 * @param handlerExecutorNormPrior
//	 * @param sendExecutorHighPrior
//	 * @param sendExecutorNormPrior
//	 *
//	 * @author: tanyaowu
//	 * @创建时间:　2016年11月16日 上午10:23:47
//	 * 
//	 */
//	public GroupContext(AioHandler<Ext, P, R> aioHandler, SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor, SynThreadPoolExecutor<SynRunnableIntf> closeExecutor,
//			SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior, SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior,
//			SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior, SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior)
//	{
//		super();
//		this.aioHandler = aioHandler;
//		this.decodeExecutor = decodeExecutor;
//		this.closeExecutor = closeExecutor;
//		this.handlerExecutorHighPrior = handlerExecutorHighPrior;
//		this.handlerExecutorNormPrior = handlerExecutorNormPrior;
//		this.sendExecutorHighPrior = sendExecutorHighPrior;
//		this.sendExecutorNormPrior = sendExecutorNormPrior;
//	}

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
	 * @return the aioHandler
	 */
	public AioHandler<Ext, P, R> getAioHandler()
	{
		return aioHandler;
	}

	/**
	 * @param aioHandler the aioHandler to set
	 */
	public void setAioHandler(AioHandler<Ext, P, R> aioHandler)
	{
		this.aioHandler = aioHandler;
	}

	/**
	 * @return the decodeExecutor
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getDecodeExecutor()
	{
		return decodeExecutor;
	}

	/**
	 * @param decodeExecutor the decodeExecutor to set
	 */
	public void setDecodeExecutor(SynThreadPoolExecutor<SynRunnableIntf> decodeExecutor)
	{
		this.decodeExecutor = decodeExecutor;
	}

	/**
	 * @return the handlerExecutorHighPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getHandlerExecutorHighPrior()
	{
		return handlerExecutorHighPrior;
	}

	/**
	 * @param handlerExecutorHighPrior the handlerExecutorHighPrior to set
	 */
	public void setHandlerExecutorHighPrior(SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorHighPrior)
	{
		this.handlerExecutorHighPrior = handlerExecutorHighPrior;
	}

	/**
	 * @return the handlerExecutorNormPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getHandlerExecutorNormPrior()
	{
		return handlerExecutorNormPrior;
	}

	/**
	 * @param handlerExecutorNormPrior the handlerExecutorNormPrior to set
	 */
	public void setHandlerExecutorNormPrior(SynThreadPoolExecutor<SynRunnableIntf> handlerExecutorNormPrior)
	{
		this.handlerExecutorNormPrior = handlerExecutorNormPrior;
	}

	/**
	 * @return the sendExecutorHighPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getSendExecutorHighPrior()
	{
		return sendExecutorHighPrior;
	}

	/**
	 * @param sendExecutorHighPrior the sendExecutorHighPrior to set
	 */
	public void setSendExecutorHighPrior(SynThreadPoolExecutor<SynRunnableIntf> sendExecutorHighPrior)
	{
		this.sendExecutorHighPrior = sendExecutorHighPrior;
	}

	/**
	 * @return the sendExecutorNormPrior
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getSendExecutorNormPrior()
	{
		return sendExecutorNormPrior;
	}

	/**
	 * @param sendExecutorNormPrior the sendExecutorNormPrior to set
	 */
	public void setSendExecutorNormPrior(SynThreadPoolExecutor<SynRunnableIntf> sendExecutorNormPrior)
	{
		this.sendExecutorNormPrior = sendExecutorNormPrior;
	}

	/**
	 * @return the byteOrder
	 */
	public ByteOrder getByteOrder()
	{
		return byteOrder;
	}

	/**
	 * @param byteOrder the byteOrder to set
	 */
	public void setByteOrder(ByteOrder byteOrder)
	{
		this.byteOrder = byteOrder;
	}

	/**
	 * @return the ipPortMaps
	 */
	public ClientNodes<Ext, P, R> getClientNodes()
	{
		return clientNodes;
	}

	/**
	 * @param remotes the ipPortMaps to set
	 */
	public void setClientNodes(ClientNodes<Ext, P, R> clientNodes)
	{
		this.clientNodes = clientNodes;
	}

	/**
	 * @return the groups
	 */
	public Groups<Ext, P, R> getGroups()
	{
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(Groups<Ext, P, R> groups)
	{
		this.groups = groups;
	}

	/**
	 * @return the users
	 */
	public Users<Ext, P, R> getUsers()
	{
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Users<Ext, P, R> users)
	{
		this.users = users;
	}


	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the heartbeatTimeout
	 */
	public long getHeartbeatTimeout()
	{
		return heartbeatTimeout;
	}

	/**
	 * @param heartbeatTimeout the heartbeatTimeout to set
	 */
	public void setHeartbeatTimeout(long heartbeatTimeout)
	{
		this.heartbeatTimeout = heartbeatTimeout;
	}

	/**
	 * @return the closeExecutor
	 */
	public SynThreadPoolExecutor<SynRunnableIntf> getCloseExecutor()
	{
		return closeExecutor;
	}

	/**
	 * @param closeExecutor the closeExecutor to set
	 */
	public void setCloseExecutor(SynThreadPoolExecutor<SynRunnableIntf> closeExecutor)
	{
		this.closeExecutor = closeExecutor;
	}

	/**
	 * @return the connections
	 */
	public Connections<Ext, P, R> getConnections()
	{
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	public void setConnections(Connections<Ext, P, R> connections)
	{
		this.connections = connections;
	}

	/**
	 * @return the groupStat
	 */
	public abstract GroupStat getGroupStat();

	/**
	 * @return the sendListener
	 */
	public SendListener<Ext, P, R> getSendListener()
	{
		return sendListener;
	}

	/**
	 * @param sendListener the sendListener to set
	 */
	public void setSendListener(SendListener<Ext, P, R> sendListener)
	{
		this.sendListener = sendListener;
	}


}
