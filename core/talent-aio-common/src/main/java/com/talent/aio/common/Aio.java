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

import java.util.Set;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.CloseRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.threadpool.SynThreadPoolExecutor;
import com.talent.aio.common.threadpool.intf.SynRunnableIntf;
import com.talent.aio.common.utils.AioUtils;

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

		channelContext.getDecodeRunnable().clearMsgQueue();
		channelContext.getHandlerRunnableNormPrior().clearMsgQueue();
		//		channelContext.getHandlerRunnableHighPrior().clearMsgQueue();
		channelContext.getSendRunnableNormPrior().clearMsgQueue();
		//		channelContext.getSendRunnableHighPrior().clearMsgQueue();

		channelContext.getDecodeRunnable().setCanceled(true);
		channelContext.getHandlerRunnableNormPrior().setCanceled(true);
		//		channelContext.getHandlerRunnableHighPrior().setCanceled(true);
		channelContext.getSendRunnableNormPrior().setCanceled(true);
		//		channelContext.getSendRunnableHighPrior().setCanceled(true);

		synchronized (channelContext)
		{
			CloseRunnable<Ext, P, R> closeRunnable = channelContext.getCloseRunnable();
			closeRunnable.setRemark(remark);
			closeRunnable.setT(t);
			closeRunnable.getExecutor().execute(closeRunnable);
		}
		//		closeRunnable.runTask();
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
	public static <Ext, P extends Packet, R> void close(GroupContext<Ext, P, R> groupContext, String remoteip, Integer remoteport, Throwable t, String remark)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getClientNodes().find(remoteip, remoteport);
		close(channelContext, t, remark);
	}

	/**
	 * 
	 * @param groupContext
	 * @param remoteip
	 * @param remoteport
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:42:25
	 *
	 */
	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByRemote(GroupContext<Ext, P, R> groupContext, String remoteip, Integer remoteport)
	{
		return groupContext.getClientNodes().find(remoteip, remoteport);
	}

	/**
	 * 一个组有哪些客户端
	 *
	 * @param groupid the groupid
	 * @return the obj with read write lock
	 */
	public static <Ext, P extends Packet, R> ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> getChannelContextsByGroup(GroupContext<Ext, P, R> groupContext, String groupid)
	{
		return groupContext.getGroups().clients(groupid);
	}

	/**
	 * 绑定用户
	 * @param groupContext
	 * @param channelContext
	 * @param userid
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:51:43
	 *
	 */
	public static <Ext, P extends Packet, R> void bindUser(ChannelContext<Ext, P, R> channelContext, String userid)
	{
		channelContext.getGroupContext().getUsers().bind(userid, channelContext);
	}

	/**
	 * 解绑用户
	 * @param groupContext
	 * @param channelContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:54:31
	 *
	 */
	public static <Ext, P extends Packet, R> void unbindUser(ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getGroupContext().getUsers().unbind(channelContext);
	}

	/**
	 * 绑定群组
	 * @param channelContext
	 * @param groupid
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:51:43
	 *
	 */
	public static <Ext, P extends Packet, R> void bindGroup(ChannelContext<Ext, P, R> channelContext, String groupid)
	{
		channelContext.getGroupContext().getGroups().bind(groupid, channelContext);
	}

	/**
	 * 解绑群组
	 * @param groupContext
	 * @param channelContext
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:54:31
	 *
	 */
	public static <Ext, P extends Packet, R> void unbindGroup(ChannelContext<Ext, P, R> channelContext)
	{
		channelContext.getGroupContext().getUsers().unbind(channelContext);
	}

	/**
	 * 
	 * @param groupContext
	 * @param userid
	 * @return
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:43:59
	 *
	 */
	public static <Ext, P extends Packet, R> ChannelContext<Ext, P, R> getChannelContextByUserid(GroupContext<Ext, P, R> groupContext, String userid)
	{
		return groupContext.getUsers().find(userid);
	}

	/**
	 * 
	 * @param groupContext
	 * @param userid
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:42:33
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToUser(GroupContext<Ext, P, R> groupContext, String userid, P packet)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getUsers().find(userid);
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
			log.error("channelContext == null");
			return;
		}
		SendRunnable<Ext, P, R> sendRunnable = AioUtils.selectSendRunnable(channelContext, packet);
		sendRunnable.addMsg(packet);
		SynThreadPoolExecutor<SynRunnableIntf> synThreadPoolExecutor = AioUtils.selectSendExecutor(channelContext, packet);
		synThreadPoolExecutor.execute(sendRunnable);
	}

	/**
	 * 发送到指定的ip和port
	 * @param groupContext
	 * @param ip
	 * @param port
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月29日 下午2:28:42
	 *
	 */
	public static <Ext, P extends Packet, R> void send(GroupContext<Ext, P, R> groupContext, String ip, int port, P packet)
	{
		ChannelContext<Ext, P, R> channelContext = groupContext.getClientNodes().find(ip, port);
		if (channelContext != null)
		{
			send(channelContext, packet);
		}
	}

	/**
	 * 
	 * @param groupContext
	 * @param groupid
	 * @param packet
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:40:24
	 *
	 */
	public static <Ext, P extends Packet, R> void sendToGroup(GroupContext<Ext, P, R> groupContext, String groupid, P packet)
	{
		ObjWithReadWriteLock<Set<ChannelContext<Ext, P, R>>> objWithReadWriteLock = groupContext.getGroups().clients(groupid);
		if (objWithReadWriteLock == null)
		{
			log.debug("组[{}]不存在", groupid);
			return;
		}

		Lock lock = objWithReadWriteLock.getLock().readLock();
		try
		{
			lock.lock();
			Set<ChannelContext<Ext, P, R>> set = objWithReadWriteLock.getObj();
			if (set.size() == 0)
			{
				log.debug("组[{}]里没有客户端", groupid);
				return;
			}
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
