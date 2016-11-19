/**
 * 
 */
package com.talent.aio.common.threadpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.talent.aio.common.threadpool.intf.QueueRunnableIntf;

/**
 * The Class AbstractQueueRunnable.
 *
 * @author 谭耀武
 * @param <T> 队列中存的数据类型
 * @date 2012-1-4
 */
public abstract class AbstractQueueRunnable<T> extends AbstractSynRunnable implements QueueRunnableIntf<T>
{

	/**
	 * Instantiates a new abstract queue runnable.
	 */
	public AbstractQueueRunnable()
	{
		runnableName = this.getClass().getSimpleName();
	}

	/** 本任务已经被提交的执行次数. */
	private AtomicLong submitCount = new AtomicLong();

	/** 本任务处理过的消息条数(单位：条). */
	private AtomicLong processedPacketCount = new AtomicLong();

	/** 本任务处理的消息量(单位：字节). */
	private AtomicLong processedMsgByteCount = new AtomicLong();

	/** The rejected count. */
	private AtomicLong rejectedCount = new AtomicLong();

	/** The msg queue. */
	private ConcurrentLinkedQueue<T> msgQueue = new ConcurrentLinkedQueue<T>();

	/** 本任务名字. */
	private String runnableName = null;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args)
	{

	}

	

	/**
	 * Sets the runnable name.
	 *
	 * @param runnableName the new runnable name
	 */
	public void setRunnableName(String runnableName)
	{
		this.runnableName = runnableName;
	}

	/**
	 * Sets the submit count.
	 *
	 * @param submitCount the new submit count
	 */
	public void setSubmitCount(AtomicLong submitCount)
	{
		this.submitCount = submitCount;
	}

	/**
	 * Sets the processed packet count.
	 *
	 * @param processedPacketCount the new processed packet count
	 */
	public void setProcessedPacketCount(AtomicLong processedPacketCount)
	{
		this.processedPacketCount = processedPacketCount;
	}

	/**
	 * Gets the processed msg byte count.
	 *
	 * @return the processed msg byte count
	 */
	public AtomicLong getProcessedMsgByteCount()
	{
		return processedMsgByteCount;
	}

	/**
	 * Sets the processed msg byte count.
	 *
	 * @param processedMsgByteCount the new processed msg byte count
	 */
	public void setProcessedMsgByteCount(AtomicLong processedMsgByteCount)
	{
		this.processedMsgByteCount = processedMsgByteCount;
	}

	/** 
	 * @see com.talent.aio.common.threadpool.intf.QueueRunnableIntf#getMsgQueue()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月15日 上午9:07:00
	 * 
	 */
	@Override
	public ConcurrentLinkedQueue<T> getMsgQueue()
	{
		return msgQueue;
	}

	/**
	 * Sets the msg queue.
	 *
	 * @param msgQueue the new msg queue
	 */
	public void setMsgQueue(ConcurrentLinkedQueue<T> msgQueue)
	{
		this.msgQueue = msgQueue;
	}

	/**
	 * Gets the rejected count.
	 *
	 * @return the rejected count
	 */
	public AtomicLong getRejectedCount()
	{
		return rejectedCount;
	}

}
