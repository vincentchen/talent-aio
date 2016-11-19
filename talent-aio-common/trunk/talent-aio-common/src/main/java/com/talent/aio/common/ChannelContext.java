
package com.talent.aio.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.config.AioConfig;
import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.task.DecodeRunnable;
import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.task.SendRunnable;

/**
 * The Class ChannelContext.
 *
 * @author tanyaowu
 * @创建时间 2016年11月14日 下午5:40:25
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月14日 | tanyaowu | 新建类
 */
public class ChannelContext<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(ChannelContext.class);

	private static java.util.concurrent.atomic.AtomicLong idAtomicLong = new AtomicLong();

	private AioConfig<Ext, P, R> aioConfig = null;

	private DecodeRunnable<Ext, P, R> decodeRunnable = new DecodeRunnable<>(this);

	private HandlerRunnable<Ext, P, R> handlerRunnableHighPrior = new HandlerRunnable<>(this);
	private HandlerRunnable<Ext, P, R> handlerRunnableLowPrior = new HandlerRunnable<>(this);

	private SendRunnable<Ext, P, R> sendRunnableHighPrior = new SendRunnable<>(this);
	private SendRunnable<Ext, P, R> sendRunnableLowPrior = new SendRunnable<>(this);

	private ReadCompletionHandler<Ext, P, R> readCompletionHandler = new ReadCompletionHandler<>();

//	private WriteCompletionHandler<Ext, P, R> writeCompletionHandler = new WriteCompletionHandler<>();
	
	private String userid;
	
	private boolean isClosed = false; 

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @author: tanyaowu
	 * @创建时间:　2016年11月14日 下午5:40:25
	 */
	public static void main(String[] args)
	{
	}

	/** The asynchronous socket channel. */
	private AsynchronousSocketChannel asynchronousSocketChannel;

	private Ext ext;

	private long id = idAtomicLong.incrementAndGet();

	private RemoteNode remoteNode;

	/**
	 * @param aioConfig
	 * @param asynchronousSocketChannel
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 下午1:13:56
	 * 
	 */
	public ChannelContext(AioConfig<Ext, P, R> aioConfig, AsynchronousSocketChannel asynchronousSocketChannel)
	{
		super();
		this.setAioConfig(aioConfig);
		this.setAsynchronousSocketChannel(asynchronousSocketChannel);
		InetSocketAddress inetSocketAddress = null;
		try
		{
			inetSocketAddress = (InetSocketAddress) asynchronousSocketChannel.getRemoteAddress();
			remoteNode = new RemoteNode(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
			aioConfig.getRemotes().put(this);
		} catch (IOException e)
		{
			log.error(e.toString(), e);
		}

	}
	
	@Override
	public String toString()
	{
		return this.getRemoteNode().toString();
	}

	/**
	 * @return the asynchronousSocketChannel
	 */
	public AsynchronousSocketChannel getAsynchronousSocketChannel()
	{
		return asynchronousSocketChannel;
	}

	/**
	 * @return the ext
	 */
	public Ext getExt()
	{
		return ext;
	}

	/**
	 * @return the id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * @return the remoteNode
	 */
	public RemoteNode getRemoteNode()
	{
		return remoteNode;
	}

	/**
	 * @param asynchronousSocketChannel the asynchronousSocketChannel to set
	 */
	public void setAsynchronousSocketChannel(AsynchronousSocketChannel asynchronousSocketChannel)
	{
		this.asynchronousSocketChannel = asynchronousSocketChannel;
	}

	/**
	 * @param ext the ext to set
	 */
	public void setExt(Ext ext)
	{
		this.ext = ext;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @param remoteNode the remoteNode to set
	 */
	public void setRemoteNode(RemoteNode remoteNode)
	{
		this.remoteNode = remoteNode;
	}

	/**
	 * @return the aioConfig
	 */
	public AioConfig<Ext, P, R> getAioConfig()
	{
		return aioConfig;
	}

	/**
	 * @param aioConfig the aioConfig to set
	 */
	public void setAioConfig(AioConfig<Ext, P, R> aioConfig)
	{
		this.aioConfig = aioConfig;
	}

	/**
	 * @return the readCompletionHandler
	 */
	public ReadCompletionHandler<Ext, P, R> getReadCompletionHandler()
	{
		return readCompletionHandler;
	}

	/**
	 * @param readCompletionHandler the readCompletionHandler to set
	 */
	public void setReadCompletionHandler(ReadCompletionHandler<Ext, P, R> readCompletionHandler)
	{
		this.readCompletionHandler = readCompletionHandler;
	}

	/**
	 * @return the decodeRunnable
	 */
	public DecodeRunnable<Ext, P, R> getDecodeRunnable()
	{
		return decodeRunnable;
	}

	/**
	 * @param decodeRunnable the decodeRunnable to set
	 */
	public void setDecodeRunnable(DecodeRunnable<Ext, P, R> decodeRunnable)
	{
		this.decodeRunnable = decodeRunnable;
	}

	/**
	 * @return the handlerRunnableHighPrior
	 */
	public HandlerRunnable<Ext, P, R> getHandlerRunnableHighPrior()
	{
		return handlerRunnableHighPrior;
	}

	/**
	 * @param handlerRunnableHighPrior the handlerRunnableHighPrior to set
	 */
	public void setHandlerRunnableHighPrior(HandlerRunnable<Ext, P, R> handlerRunnableHighPrior)
	{
		this.handlerRunnableHighPrior = handlerRunnableHighPrior;
	}

	/**
	 * @return the handlerRunnableLowPrior
	 */
	public HandlerRunnable<Ext, P, R> getHandlerRunnableLowPrior()
	{
		return handlerRunnableLowPrior;
	}

	/**
	 * @param handlerRunnableLowPrior the handlerRunnableLowPrior to set
	 */
	public void setHandlerRunnableLowPrior(HandlerRunnable<Ext, P, R> handlerRunnableLowPrior)
	{
		this.handlerRunnableLowPrior = handlerRunnableLowPrior;
	}

	/**
	 * @return the sendRunnableHighPrior
	 */
	public SendRunnable<Ext, P, R> getSendRunnableHighPrior()
	{
		return sendRunnableHighPrior;
	}

	/**
	 * @param sendRunnableHighPrior the sendRunnableHighPrior to set
	 */
	public void setSendRunnableHighPrior(SendRunnable<Ext, P, R> sendRunnableHighPrior)
	{
		this.sendRunnableHighPrior = sendRunnableHighPrior;
	}

	/**
	 * @return the sendRunnableLowPrior
	 */
	public SendRunnable<Ext, P, R> getSendRunnableLowPrior()
	{
		return sendRunnableLowPrior;
	}

	/**
	 * @param sendRunnableLowPrior the sendRunnableLowPrior to set
	 */
	public void setSendRunnableLowPrior(SendRunnable<Ext, P, R> sendRunnableLowPrior)
	{
		this.sendRunnableLowPrior = sendRunnableLowPrior;
	}

//	/**
//	 * @return the writeCompletionHandler
//	 */
//	public WriteCompletionHandler<Ext, P, R> getWriteCompletionHandler()
//	{
//		return writeCompletionHandler;
//	}
//
//	/**
//	 * @param writeCompletionHandler the writeCompletionHandler to set
//	 */
//	public void setWriteCompletionHandler(WriteCompletionHandler<Ext, P, R> writeCompletionHandler)
//	{
//		this.writeCompletionHandler = writeCompletionHandler;
//	}

	/**
	 * @return the userid
	 */
	public String getUserid()
	{
		return userid;
	}

	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	/**
	 * @return the isClosed
	 */
	public boolean isClosed()
	{
		return isClosed;
	}

	/**
	 * @param isClosed the isClosed to set
	 */
	public void setClosed(boolean isClosed)
	{
		this.isClosed = isClosed;
	}

}
