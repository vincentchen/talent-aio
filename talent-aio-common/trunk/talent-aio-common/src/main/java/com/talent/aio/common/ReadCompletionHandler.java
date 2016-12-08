/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 下午1:31:04
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.DecodeRunnable;
import com.talent.aio.common.utils.AioUtils;
import com.talent.aio.common.utils.SystemTimer;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 下午1:31:04
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class ReadCompletionHandler<Ext, P extends Packet, R> implements CompletionHandler<Integer, ChannelContext<Ext, P, R>>
{

	private static Logger log = LoggerFactory.getLogger(ReadCompletionHandler.class);

	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public ReadCompletionHandler()
	{

	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月15日 下午1:31:04
	 * 
	 */
	public static void main(String[] args)
	{

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#completed(java.lang.Object, java.lang.Object)
	 * 
	 * @param result
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void completed(Integer result, ChannelContext<Ext, P, R> channelContext)
	{
		GroupContext<Ext, P, R> groupContext = channelContext.getGroupContext();
		if (result > 0)
		{
			channelContext.getStat().setTimeLatestReceivedMsg(SystemTimer.currentTimeMillis());
			byteBuffer.flip();
			ByteBuffer byteBuffer1 = ByteBuffer.allocate(byteBuffer.limit());
			byteBuffer1.put(byteBuffer);
			byteBuffer.clear();

			//			byteBuffer1.flip();
			DecodeRunnable<Ext, P, R> decodeRunnable = channelContext.getDecodeRunnable();
			decodeRunnable.addMsg(byteBuffer1);
			
			groupContext.getDecodeExecutor().execute(decodeRunnable);

		} else if (result == 0)
		{
			log.error("读到的数据长度为0");
		} else if (result < 0)
		{
			Aio.close(channelContext, null, "读数据时返回" + result);
		}

		if (AioUtils.checkBeforeIO(channelContext))
		{
			AsynchronousSocketChannel asynchronousSocketChannel = channelContext.getAsynchronousSocketChannel();
			asynchronousSocketChannel.read(this.getByteBuffer(), channelContext, this);
		}

	}

	/** 
	 * @see java.nio.channels.CompletionHandler#failed(java.lang.Throwable, java.lang.Object)
	 * 
	 * @param exc
	 * @param attachment
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月16日 下午1:40:59
	 * 
	 */
	@Override
	public void failed(Throwable exc, ChannelContext<Ext, P, R> attachment)
	{
		Aio.close(attachment, exc, "读数据时发生异常");

	}

	/**
	 * @return the byteBuffer
	 */
	public ByteBuffer getByteBuffer()
	{
		return byteBuffer;
	}

	/**
	 * @param byteBuffer the byteBuffer to set
	 */
	public void setByteBuffer(ByteBuffer byteBuffer)
	{
		this.byteBuffer = byteBuffer;
	}

}
