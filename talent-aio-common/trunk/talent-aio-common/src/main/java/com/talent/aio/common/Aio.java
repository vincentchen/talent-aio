/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:22:48
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.config.AioConfig;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;
import com.talent.aio.common.utils.AioUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * The Class Aio.
 *
 * @author tanyaowu
 * @创建时间 2016年11月15日 上午11:22:48
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 */
public class Aio
{

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(Aio.class);

	/**
	 * Instantiates a new aio.
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:22:48
	 */
	public Aio()
	{

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 上午11:22:48
	 */
	public static void main(String[] args)
	{

	}

	/**
	 * Close.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param t the t
	 * @param remark the remark
	 */
	public static <Ext, P extends Packet, R> void close(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark)
	{
		if (channelContext == null)
		{
			log.warn("channelContext == null");
			return;
		}
		
		AioConfig<Ext, P, R> aioConfig = channelContext.getAioConfig();
		Semaphore semaphore = aioConfig.getCloseSemaphore();
		try
		{
			long starttime = SystemTimer.currentTimeMillis();
			semaphore.acquire();
			long endtime = SystemTimer.currentTimeMillis();
			long cost = (endtime - starttime);
			if (cost > 100)
			{
				log.error("等关闭权限耗时:{}ms", cost);
			}
			try
			{
				aioConfig.getAioHandler().onClose(channelContext, t, remark);
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
			
			channelContext.setClosed(true);
			
			channelContext.getDecodeRunnable().clearMsgQueue();
			channelContext.getHandlerRunnableLowPrior().clearMsgQueue();
			channelContext.getHandlerRunnableHighPrior().clearMsgQueue();
			channelContext.getSendRunnableLowPrior().clearMsgQueue();
			channelContext.getSendRunnableHighPrior().clearMsgQueue();
			
			channelContext.getDecodeRunnable().setCanceled(true);
			channelContext.getHandlerRunnableLowPrior().setCanceled(true);
			channelContext.getHandlerRunnableHighPrior().setCanceled(true);
			channelContext.getSendRunnableLowPrior().setCanceled(true);
			channelContext.getSendRunnableHighPrior().setCanceled(true);

			try
			{
				AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
				if (asynchronousSocketChannel != null)
				{
					asynchronousSocketChannel.close();
				}
			} catch (Throwable e)
			{
				log.error(e.toString());
			}

			//删除集合中的维护信息 start
			try
			{
				aioConfig.getRemotes().remove(channelContext);
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
			try
			{
				aioConfig.getUsers().remove(channelContext);
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
			try
			{
				aioConfig.getGroups().remove(channelContext);
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			}
			
			
			//删除集合中的维护信息 end

		} catch (Throwable e)
		{
			log.error(e.toString(), e);
		} finally
		{
			semaphore.release();
		}
	}

	public static <Ext, P extends Packet, R> void close(ChannelContext<Ext, P, R> channelContext, String remark)
	{
		close(channelContext, null, remark);
	}
	/**
	 * Close.
	 *
	 * @param remoteip the remoteip
	 * @param remoteport the remoteport
	 * @param t the t
	 * @param remark the remark
	 */
	public static <Ext, P extends Packet, R> void close(AioConfig<Ext, P, R> aioConfig, String remoteip, Integer remoteport, Throwable t, String remark)
	{
		ChannelContext<Ext, P, R> channelContext = aioConfig.getRemotes().find(remoteip, remoteport);
		close(channelContext, t, remark);
	}

//	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByRemote(AioConfig<Ext, P, R> aioConfig, String remoteip, Integer remoteport)
//	{
//		return aioConfig.getRemotes().find(remoteip, remoteport);
//	}

//	/**
//	 * 一个组有哪些客户端
//	 *
//	 * @param groupid the groupid
//	 * @return the obj with read write lock
//	 */
//	public static <Ext, P extends Packet, R> ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> getChannelContextsByGroup(AioConfig<Ext, P, R> aioConfig, String groupid)
//	{
//		return aioConfig.getGroups().clients(groupid);
//	}
//	
//	/**
//	 * 与用户关联上
//	 * @param aioConfig
//	 * @param channelContext
//	 * @param userid
//	 *
//	 * @author: tanyaowu
//	 * @创建时间:　2016年11月17日 下午5:51:43
//	 *
//	 */
//	public static <Ext, P extends Packet, R> void attachUserid(AioConfig<Ext, P, R> aioConfig, ChannelContext<Ext, P, R> channelContext, String userid)
//	{
//		aioConfig.getUsers().put(userid, channelContext);
//	}
//	/**
//	 * 取消与用户的关联
//	 * @param aioConfig
//	 * @param channelContext
//	 *
//	 * @author: tanyaowu
//	 * @创建时间:　2016年11月17日 下午5:54:31
//	 *
//	 */
//	public static <Ext, P extends Packet, R> void removeUserid(AioConfig<Ext, P, R> aioConfig, ChannelContext<Ext, P, R> channelContext)
//	{
//		aioConfig.getUsers().remove(channelContext);//(userid, channelContext);
//	}
//	/**
//	 * 
//	 * @param aioConfig
//	 * @param userid
//	 * @return
//	 *
//	 * @author: tanyaowu
//	 * @创建时间:　2016年11月17日 下午5:43:59
//	 *
//	 */
//	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByUserid(AioConfig<Ext, P, R> aioConfig, String userid)
//	{
//		return aioConfig.getUsers().find(userid);
//	}
	
	public static <Ext, P extends Packet, R> void sendToUser(AioConfig<Ext, P, R> aioConfig, String userid, P packet)
	{
		ChannelContext<Ext, P, R> channelContext = aioConfig.getUsers().find(userid);
		send(channelContext, packet);
	}

	/**
	 * Send.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param packet the packet
	 */
	public static <Ext, P extends Packet, R> void send(ChannelContext<Ext, P, R> channelContext, P packet)
	{
		if (channelContext == null)
		{
			log.warn("channelContext == null");
			return;
		}
		SendRunnable<Ext, P, R> sendRunnable = AioUtils.selectSendRunnable(channelContext, packet);
		sendRunnable.addMsg(packet);
		SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor = AioUtils.selectSendExecutor(channelContext, packet);
		synThreadPoolExecutor.execute(sendRunnable);
	}

	/**
	 * 
	 * @param aioConfig
	 * @param groupid
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:40:24
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToGroup(AioConfig<Ext, P, R> aioConfig, String groupid, P packet)
	{
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = aioConfig.getGroups().clients(groupid);
		if (objWithReadWriteLock == null)
		{
			log.debug("组[{}]不存在", groupid);
			return;
		}

		Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
		if (set.size() == 0)
		{
			log.debug("组[{}]里没有客户端", groupid);
			return;
		}
		Lock lock = objWithReadWriteLock.getLock().readLock();
		try
		{
			lock.lock();
			for (ChannelContext<Ext, P, R> channelContext : set)
			{
				send(channelContext, packet);
			}
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
			lock.unlock();
		}

	}

	/**
	 * Handler.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param packet the packet
	 */
	public static <Ext, P extends Packet, R> void handler(ChannelContext<Ext, P, R> channelContext, P packet)
	{
		HandlerRunnable<Ext, P, R> handlerRunnable = AioUtils.selectHandlerRunnable(channelContext, packet);
		handlerRunnable.addMsg(packet);
		SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor = AioUtils.selectHandlerExecutor(channelContext, packet);
		synThreadPoolExecutor.execute(handlerRunnable);
	}

	/**
	 * 同步发送消息.
	 *
	 * @param <Ext> the generic type
	 * @param <P> the generic type
	 * @param <R> the generic type
	 * @param channelContext the channel context
	 * @param packet the packet
	 * @return the p
	 */
	public static <Ext, P extends Packet, R> P synSend(ChannelContext<Ext, P, R> channelContext, P packet)
	{
		log.error("待实现");
		return null;
	}

}
