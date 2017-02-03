package com.talent.aio.common.task;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.GroupContext;
import com.talent.aio.common.ReconnConf;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.MaintainUtils;
import com.talent.aio.common.threadpool.AbstractSynRunnable;
import com.talent.aio.common.utils.SystemTimer;

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
public class CloseRunnable<Ext, P extends Packet, R> extends AbstractSynRunnable
{
	private static Logger log = LoggerFactory.getLogger(CloseRunnable.class);

	private ChannelContext<Ext, P, R> channelContext;
	private String remark;
	private Throwable throwable;
	private boolean isRemove = false;
	private boolean isWaitingExecute = false;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private static final AtomicLong closeCount = new AtomicLong();//总的关闭次数

	public CloseRunnable(ChannelContext<Ext, P, R> channelContext, Throwable t, String remark, Executor executor)
	{
		super(executor);
		this.channelContext = channelContext;
		this.throwable = t;
		this.remark = remark;
	}

	@Override
	public void runTask()
	{
//		ReentrantReadWriteLock reentrantReadWriteLock = getLock();
//		WriteLock writeLock = reentrantReadWriteLock.writeLock();
//		if (!writeLock.tryLock())
//		{
//			return;
//		}
		
		try
		{
			closeCount.incrementAndGet();
			if (throwable != null)
			{
				log.error("第{}次关闭连接:{},{}", closeCount.get(), channelContext.toString(), remark);
			} else
			{
				log.info("第{}次关闭连接:{},{}", closeCount.get(), channelContext.toString(), remark);
			}

			GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
			AioListener<Ext, P, R> aioListener = groupContext.getAioListener();

			ReconnConf<Ext, P, R> reconnConf = channelContext.getGroupContext().getReconnConf();
			if (!isRemove)
			{
				if (reconnConf != null && reconnConf.getInterval() > 0)
				{
					if (reconnConf.getRetryCount() <= 0 || reconnConf.getRetryCount() >= channelContext.getReconnCount())
					{
						//需要重连，所以并不删除
					} else
					{
						isRemove = true;
					}
				} else
				{
					isRemove = true;
				}
			}

			try
			{
//				if (aioListener != null)
//				{
//					try
//					{
//						groupContext.getAioListener().onBeforeClose(channelContext, throwable, remark, isRemove);
//					} catch (Throwable e)
//					{
//						log.error(e.toString(), e);
//					}
//				}
				
				channelContext.setClosed(true);
				channelContext.getGroupContext().getGroupStat().getClosed().incrementAndGet();
				channelContext.getStat().setTimeClosed(SystemTimer.currentTimeMillis());

				if (isRemove)
				{
					MaintainUtils.removeFromMaintain(channelContext);
					channelContext.setRemoved(true);
				} else
				{
					groupContext.getCloseds().add(channelContext);
					groupContext.getConnecteds().remove(channelContext);
				}
				
				try
				{
					AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
					if (asynchronousSocketChannel != null)
					{
						if (asynchronousSocketChannel.isOpen())
						{
							try
							{
								asynchronousSocketChannel.shutdownInput();
								asynchronousSocketChannel.shutdownOutput();
							} catch (Exception e)
							{
								log.error(e.toString(), e);
							}
							asynchronousSocketChannel.close();
						}
					}
				} catch (Throwable e)
				{
					log.error(e.toString());
				}
				
				if (aioListener != null)
				{
					try
					{
						aioListener.onAfterClose(channelContext, throwable, remark, isRemove);
					} catch (Throwable e)
					{
						log.error(e.toString(), e);
					}
				}
			} catch (Throwable e)
			{
				log.error(e.toString(), e);
			} finally
			{
				if (!isRemove)
				{
					try
					{
						reconnConf.getQueue().put(channelContext);
					} catch (InterruptedException e)
					{
						log.error(e.toString(), e);
					}
					
					this.setWaitingExecute(false);
				}
			}
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		} finally
		{
//			writeLock.unlock();
		}
	
	}

	@Override
	public boolean isNeededExecute()
	{
		return false;
	}

	/**
	 * @return the channelContext
	 */
	public ChannelContext<Ext, P, R> getChannelContext()
	{
		return channelContext;
	}

	/**
	 * @return the remark
	 */
	public String getRemark()
	{
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	/**
	 * @return the t
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}

	/**
	 * @param t the t to set
	 */
	public void setThrowable(Throwable throwable)
	{
		this.throwable = throwable;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName()).append(":");
		builder.append(channelContext.toString());
		return builder.toString();
	}

	/**
	 * @return the isWaitingExecute
	 */
	public boolean isWaitingExecute()
	{
		return isWaitingExecute;
	}

	/**
	 * @param isWaitingExecute the isWaitingExecute to set
	 */
	public void setWaitingExecute(boolean isWaitingExecute)
	{
		this.isWaitingExecute = isWaitingExecute;
	}

	/**
	 * @return the isRemove
	 */
	public boolean isRemove()
	{
		return isRemove;
	}

	/**
	 * @param isRemove the isRemove to set
	 */
	public void setRemove(boolean isRemove)
	{
		this.isRemove = isRemove;
	}

	/**
	 * @return the lock
	 */
	public ReentrantReadWriteLock getLock()
	{
		return lock;
	}

}
