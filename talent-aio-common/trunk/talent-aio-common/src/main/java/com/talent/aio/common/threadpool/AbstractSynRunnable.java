/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.threadpool.intf.SynRunnableIntf;
import com.talent.aio.common.utils.SystemTimer;

/**
 * The Class AbstractSynRunnable.
 *
 * @filename:  com.talent.threadpool.AbstractSynRunnable
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2012-5-18 下午3:30:30
 * @record <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2012-5-18</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public abstract class AbstractSynRunnable implements SynRunnableIntf
{

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(AbstractSynRunnable.class);

	/** 正在运行该任务的线程. */
	private java.util.concurrent.atomic.AtomicInteger currThreads = new java.util.concurrent.atomic.AtomicInteger();

	/**
	 * Instantiates a new abstract syn runnable.
	 */
	protected AbstractSynRunnable()
	{

	}

	/**
	 * 等待运行本任务的线程个数.
	 *
	 * @return the int
	 */
	public int waitingRunCount()
	{
		int size = getCurrThreads().get() - 1;
		if (size > 0)
		{
			log.info("{} threads wait for me, i am {}", size, this);
		}
		return size;
	}

	/** 
	 * @see java.lang.Runnable#run()
	 * 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public final void run()
	{
		//		Thread currthread = Thread.currentThread();
		getCurrThreads().incrementAndGet();
		long starttime = SystemTimer.currentTimeMillis();
		long starttime1 = SystemTimer.currentTimeMillis();
		long endtime1 = SystemTimer.currentTimeMillis();
		synchronized (this)
		{
			boolean nextRunning = false;
			try
			{
				starttime1 = SystemTimer.currentTimeMillis();
				setRunning(true);

				if (waitingRunCount() > 0)
				{
					nextRunning = false;
					return;
				}

				runTask();
			} catch (Exception e)
			{
				log.error(e.toString(), e);
			} finally
			{
				if (waitingRunCount() > 0)
				{
					nextRunning = false;
				}
				setRunning(nextRunning);
				getCurrThreads().decrementAndGet();
				this.notify();
			}
			endtime1 = SystemTimer.currentTimeMillis();
		}
		long endtime = SystemTimer.currentTimeMillis();
		long timecost = endtime - starttime;
		long timecost1 = endtime1 - starttime1;
		if (timecost > 500L)
		{
			log.warn("任务耗时:{}ms,{}ms, {}", timecost, timecost1, this);
		}

	}

	/** 是否正在执行，true:正在执行，false：不是正在执行. */
	private boolean isRunning = false;

	/** 是否在执行列表中，true:是的，false:没有纳入计划执行列表. */
	private boolean isInSchedule = false;

	private boolean isCanceled = false;

	public boolean isCanceled()
	{
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled)
	{
		this.isCanceled = isCanceled;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#setRunning(boolean)
	 * 
	 * @param isRunning
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public void setRunning(boolean isRunning)
	{
		this.isRunning = isRunning;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#isRunning()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public boolean isRunning()
	{
		return isRunning;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#setInSchedule(boolean)
	 * 
	 * @param isInSchedule
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public void setInSchedule(boolean isInSchedule)
	{
		this.isInSchedule = isInSchedule;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#isInSchedule()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public boolean isInSchedule()
	{
		return isInSchedule;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.SynRunnableIntf#getCurrThreads()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:01
	 * 
	 */
	@Override
	public AtomicInteger getCurrThreads()
	{
		return currThreads;
	}

	//	public void setCurrThreads(Set<Thread> currThreads)
	//	{
	//		this.currThreads = currThreads;
	//	}
}
