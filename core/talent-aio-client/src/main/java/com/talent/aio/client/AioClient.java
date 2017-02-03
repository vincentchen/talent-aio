package com.talent.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ChannelContext.Stat;
import com.talent.aio.common.ObjWithLock;
import com.talent.aio.common.ReadCompletionHandler;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年1月2日 下午6:02:56
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年1月2日 | tanyaowu | 新建类
 *
 */
public class AioClient<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(AioClient.class);
	
	private static final int DEFAULT_TIMEOUT = 2;

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

	private AsynchronousChannelGroup channelGroup;

	private ClientGroupContext<Ext, P, R> clientGroupContext;

	/**
	 * @param serverIp 可以为空
	 * @param serverPort 
	 * @param aioDecoder
	 * @param aioEncoder
	 * @param aioHandler
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月15日 下午1:09:28
	 * 
	 */
	public AioClient(final ClientGroupContext<Ext, P, R> clientGroupContext) throws IOException
	{
		super();
		this.clientGroupContext = clientGroupContext;
		ExecutorService groupExecutor = clientGroupContext.getGroupExecutor();
		this.channelGroup = AsynchronousChannelGroup.withThreadPool(groupExecutor);

		startHeartbeatTask();

		startReconnTask();
	}

	//	/**
	//	 * 
	//	 * @param bindIp
	//	 * @param bindPort
	//	 * @param autoReconnect
	//	 * @return
	//	 * @throws Exception
	//	 *
	//	 * @author: tanyaowu
	//	 * @创建时间:　2016年12月19日 下午5:49:05
	//	 *
	//	 */
	//	public ClientChannelContext<Ext, P, R> connect(String bindIp, Integer bindPort, boolean autoReconnect) throws Exception
	//	{
	//		return connect(bindIp, bindPort, autoReconnect, null);
	//	}

	/**
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月3日 下午10:30:14
	 *
	 */
	public ClientChannelContext<Ext, P, R> connect(String serverIp, Integer serverPort) throws Exception
	{
		return connect(serverIp, serverPort, null, 0, null);
	}
	
	/**
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月2日 下午1:26:17
	 *
	 */
	public ClientChannelContext<Ext, P, R> connect(String serverIp, Integer serverPort, Integer timeout) throws Exception
	{
		return connect(serverIp, serverPort, null, 0, timeout);
	}

	/**
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param bindIp
	 * @param bindPort
	 * @param timeout
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月3日 下午10:27:28
	 *
	 */
	public ClientChannelContext<Ext, P, R> connect(String serverIp, Integer serverPort, String bindIp, Integer bindPort, Integer timeout) throws Exception
	{
		return connect(serverIp, serverPort, bindIp, bindPort, null, timeout);
	}

	
	/**
	 * 
	 * @param serverIp
	 * @param serverPort
	 * @param bindIp
	 * @param bindPort
	 * @param initClientChannelContext
	 * @param timeout 超时时间，单位秒
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月3日 下午10:25:26
	 *
	 */
	private ClientChannelContext<Ext, P, R> connect(String serverIp, Integer serverPort, String bindIp, Integer bindPort, ClientChannelContext<Ext, P, R> initClientChannelContext, Integer timeout)
			throws Exception
	{
		AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open(channelGroup);
		asynchronousSocketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		asynchronousSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

		InetSocketAddress bind = null;
		if (bindPort != null && bindPort > 0)
		{
			if (StringUtils.isNotBlank(bindIp))
			{
				bind = new InetSocketAddress(bindIp, bindPort);
			} else
			{
				bind = new InetSocketAddress(bindPort);
			}
		}

		if (bind != null)
		{
			asynchronousSocketChannel.bind(bind);
		}

		ClientAioListener<Ext, P, R> clientAioListener = clientGroupContext.getClientAioListener();

		boolean isReconnect = false;
		ClientChannelContext<Ext, P, R> channelContext = null;//new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
		//		
		try
		{
			Future<Void> future = asynchronousSocketChannel.connect(new InetSocketAddress(serverIp, serverPort));

			if (initClientChannelContext != null) //表示是重连
			{
				channelContext = initClientChannelContext;
				isReconnect = true;
			} else
			{
				channelContext = new ClientChannelContext<>(clientGroupContext, asynchronousSocketChannel);
				channelContext.setServerIp(serverIp);
				channelContext.setServerPort(serverPort);
			}

			Integer _timeout = timeout;
			if (_timeout == null)
			{
				_timeout = DEFAULT_TIMEOUT;
			}
			future.get(_timeout, TimeUnit.SECONDS);
			
			
			if (isReconnect)
			{
				channelContext.setAsynchronousSocketChannel(asynchronousSocketChannel);
				channelContext.getDecodeRunnable().setCanceled(false);
				channelContext.getHandlerRunnableNormPrior().setCanceled(false);
				//		channelContext.getHandlerRunnableHighPrior().setCanceled(false);
				channelContext.getSendRunnableNormPrior().setCanceled(false);
				//		channelContext.getSendRunnableHighPrior().setCanceled(false);

				clientGroupContext.getCloseds().remove(channelContext);
			}
			clientGroupContext.getConnecteds().add(channelContext);

			channelContext.setBindIp(bindIp);
			channelContext.setBindPort(bindPort);

			channelContext.setReconnCount(0);
			channelContext.setClosed(false);
		} catch (Exception e)
		{
			log.error(e.toString(), e);

			if (!isReconnect)
			{
				clientGroupContext.getCloseds().add(channelContext);
			}

			ReconnConf<Ext, P, R> reconnConf = clientGroupContext.getReconnConf();
			if (reconnConf != null && reconnConf.getInterval() > 0)
			{
				if (reconnConf.getRetryCount() <= 0 || reconnConf.getRetryCount() >= channelContext.getReconnCount())
				{
					reconnConf.getQueue().put(channelContext);
				}
			}

			channelContext.setClosed(true);
			channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());

			if (clientAioListener != null)
			{
//				clientAioListener.onFailConnected(channelContext, isReconnect, e);
				boolean f = clientAioListener.onAfterConnected(channelContext, false, isReconnect);
				if (!f)
				{
					log.warn("不允许连接:{}", channelContext);
					Aio.remove(channelContext, "不允许连接");
					return null;
				}
			}

			return channelContext;
		}
		log.info("connected to {}:{}", serverIp, serverPort);

		if (clientAioListener != null)
		{
			boolean f = clientAioListener.onAfterConnected(channelContext, true, isReconnect);
			if (!f)
			{
				log.warn("不允许连接:{}", channelContext);
				Aio.remove(channelContext, "不允许连接");
				return null;
			}
		}

		ReadCompletionHandler<Ext, P, R> readCompletionHandler = channelContext.getReadCompletionHandler();
		ByteBuffer byteBuffer = ByteBuffer.allocate(channelContext.getGroupContext().getReadBufferSize());
		asynchronousSocketChannel.read(byteBuffer, byteBuffer, readCompletionHandler);

		return channelContext;

	}

	/**
	 * @return the channelGroup
	 */
	public AsynchronousChannelGroup getChannelGroup()
	{
		return channelGroup;
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<Ext, P, R> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * 
	 * @param channelContext
	 * @param timeout
	 * @return
	 * @throws Exception
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年2月3日 下午10:28:50
	 *
	 */
	public ClientChannelContext<Ext, P, R> reconnect(ClientChannelContext<Ext, P, R> channelContext, Integer timeout) throws Exception
	{
		connect(channelContext.getServerIp(), channelContext.getServerPort(), channelContext.getBindIp(), channelContext.getBindPort(), channelContext, timeout);
		ClientGroupContext<Ext, P, R> clientGroupContext = (ClientGroupContext<Ext, P, R>) channelContext.getGroupContext();
		ClientAioListener<Ext, P, R> clientAioListener = clientGroupContext.getClientAioListener();
		if (channelContext.isClosed())
		{
			if (clientAioListener != null)
			{
				clientAioListener.onAfterReconnected(channelContext, false);
			}
			return null;
		}

		
		if (clientAioListener != null)
		{
			clientAioListener.onAfterReconnected(channelContext, true);
		}
		return channelContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<Ext, P, R> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}

	/**
	 * 定时任务：发心跳，重连(待实现)
	 * @author: tanyaowu
	 * @创建时间:　2017年1月2日 下午6:01:06
	 *
	 */
	private void startHeartbeatTask()
	{
		final ClientGroupStat clientGroupStat = clientGroupContext.getClientGroupStat();
		final ClientAioHandler<Ext, P, R> aioHandler = (ClientAioHandler<Ext, P, R>) clientGroupContext.getClientAioHandler();
		final long heartbeatTimeout = clientGroupContext.getHeartbeatTimeout();
		final String id = clientGroupContext.getId();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					ReadLock readLock = null;
					try
					{
						ObjWithLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = clientGroupContext.getConnections().getSetWithLock();
						readLock = objWithReadWriteLock.getLock().readLock();
						readLock.lock();
						Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
						long currtime = SystemTimer.currentTimeMillis();
						for (ChannelContext<Ext, P, R> entry : set)
						{
							ClientChannelContext<Ext, P, R> channelContext = (ClientChannelContext<Ext, P, R>) entry;

							//							if (channelContext.isClosed()) //已经关闭了
							//							{
							//								if (channelContext.isAutoReconnect())
							//								{
							//									AioClient.this.reconnect(channelContext);
							//								}
							//							} else
							//							{
							Stat stat = channelContext.getStat();
							long timeLatestReceivedMsg = stat.getLatestTimeOfReceivedPacket();
							long timeLatestSentMsg = stat.getLatestTimeOfSentPacket();
							long compareTime = Math.max(timeLatestReceivedMsg, timeLatestSentMsg);
							long interval = (currtime - compareTime);
							if (interval >= heartbeatTimeout / 2)
							{
								P packet = aioHandler.heartbeatPacket();
								if (packet != null)
								{
									log.info("{}发送心跳包", channelContext.toString());
									Aio.send(channelContext, packet);
								}
							}
							//							}
						}
						if (log.isInfoEnabled())
						{
							log.info("[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", id, set.size(), clientGroupStat.getClosed().get(),
									clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(), clientGroupStat.getHandledPacket().get(),
									clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());
						}

					} catch (Throwable e)
					{
						log.error("", e);
					} finally
					{
						try
						{
							if (readLock != null)
							{
								readLock.unlock();
							}
							Thread.sleep(heartbeatTimeout / 4);
						} catch (Exception e)
						{
							log.error(e.toString(), e);
						} finally
						{

						}
					}
				}
			}
		}, "t-aio-timer-heartbeat" + id).start();
	}

	
	private static class ReconnRunnable <Ext, P extends Packet, R> implements Runnable
	
	{
		ClientChannelContext<Ext, P, R> channelContext = null;
		AioClient<Ext, P, R> aioClient = null;

		public ReconnRunnable(ClientChannelContext<Ext, P, R> channelContext, AioClient<Ext, P, R> aioClient)
		{
			this.channelContext = channelContext;
			this.aioClient = aioClient;
		}

		/** 
		 * @see java.lang.Runnable#run()
		 * 
		 * @重写人: tanyaowu
		 * @重写时间: 2017年2月2日 下午8:24:40
		 * 
		 */
		@Override
		public void run()
		{
			try
			{
				aioClient.reconnect(channelContext, 1);
			} catch (java.lang.Throwable e)
			{
				log.error(e.toString(), e);
			}
			if (channelContext.isClosed())
			{
				channelContext.setReconnCount(channelContext.getReconnCount() + 1);
				//									continue;
				return;
			}
		}
	}
	/**
	 * 启动重连任务
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2017年1月11日 下午5:48:17
	 *
	 */
	private void startReconnTask()
	{
		final ReconnConf<Ext, P, R> reconnConf = clientGroupContext.getReconnConf();
		if (reconnConf == null || reconnConf.getInterval() <= 0)
		{
			return;
		}

		final String id = clientGroupContext.getId();
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					LinkedBlockingQueue<ChannelContext<Ext, P, R>> queue = reconnConf.getQueue();
					ClientChannelContext<Ext, P, R> channelContext = null;
					try
					{
						channelContext = (ClientChannelContext<Ext, P, R>) queue.take();
					} catch (InterruptedException e1)
					{
						log.error(e1.toString(), e1);
					}
					if (channelContext == null)
					{
						continue;
//						return;
					}
					
					if (channelContext.isRemoved())
					{
						continue;
					}
					
					
					long currtime = SystemTimer.currentTimeMillis();
					long closetime = channelContext.getStat().getTimeClosed();
					long sleeptime = reconnConf.getInterval() - (currtime - closetime);
					if (sleeptime > 0)
					{
						try
						{
							Thread.sleep(sleeptime);
						} catch (InterruptedException e)
						{
							log.error(e.toString(), e);
						}
					}
					
					if (channelContext.isRemoved())
					{
						continue;
					}
					ReconnRunnable<Ext, P, R> runnable = new ReconnRunnable<Ext, P, R>(channelContext, AioClient.this);
					reconnConf.getThreadPoolExecutor().execute(runnable);
				}
			}
		});
		thread.setName("t-aio-timer-reconnect" + id);
		thread.setDaemon(true);
		thread.start();

	}
}
