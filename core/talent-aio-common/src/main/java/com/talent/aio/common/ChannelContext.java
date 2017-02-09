
package com.talent.aio.common;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.intf.Packet;
import com.talent.aio.common.maintain.MaintainUtils;
import com.talent.aio.common.task.DecodeRunnable;
import com.talent.aio.common.task.HandlerRunnable;
import com.talent.aio.common.task.SendRunnable;
import com.talent.aio.common.utils.SystemTimer;

/**
 * The Class ChannelContext.
 *
 * @author tanyaowu
 * @创建时间 2016年11月14日 下午5:40:25
 * @操作列表  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月14日 | tanyaowu | 新建类
 */
public abstract class ChannelContext<Ext, P extends Packet, R>
{
	private static Logger log = LoggerFactory.getLogger(ChannelContext.class);

	private static final AtomicLong ID_SEQ = new AtomicLong();
	
	public static final String UNKNOWN_ADDRESS_IP = "$UNKNOWN";
	
	public static final AtomicInteger UNKNOWN_ADDRESS_PORT_SEQ = new AtomicInteger();

	//	private java.util.concurrent.Semaphore sendSemaphore = new Semaphore(1);

	private GroupContext<Ext, P, R> groupContext = null;

	private DecodeRunnable<Ext, P, R> decodeRunnable = null;

//	private CloseRunnable<Ext, P, R> closeRunnable = null;
//	private HandlerRunnable<Ext, P, R> handlerRunnableHighPrior = null;
	private HandlerRunnable<Ext, P, R> handlerRunnableNormPrior = null;

//	private SendRunnable<Ext, P, R> sendRunnableHighPrior = null;
	private SendRunnable<Ext, P, R> sendRunnableNormPrior = null;
	private ReentrantReadWriteLock closeLock = new ReentrantReadWriteLock();
	private ReadCompletionHandler<Ext, P, R> readCompletionHandler = null;//new ReadCompletionHandler<>(this);
	private WriteCompletionHandler<Ext, P, R> writeCompletionHandler = null;//new WriteCompletionHandler<>(this);
	
	private int reconnCount = 0;//连续重连次数，连接成功后，此值会被重置0

	//	private WriteCompletionHandler<Ext, P, R> writeCompletionHandler = new WriteCompletionHandler<>();

	private String userid;

	private boolean isClosed = false;
	
	private boolean isRemoved = false;

	private Stat stat = new Stat();

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

	private long id = ID_SEQ.incrementAndGet();

	private Node clientNode;
	
	private Node serverNode;

	/**
	 * @param groupContext
	 * @param asynchronousSocketChannel
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月16日 下午1:13:56
	 * 
	 */
	public ChannelContext(GroupContext<Ext, P, R> groupContext, AsynchronousSocketChannel asynchronousSocketChannel)
	{
		super();
		this.setGroupContext(groupContext);
		this.setAsynchronousSocketChannel(asynchronousSocketChannel);
		this.readCompletionHandler = new ReadCompletionHandler<>(this);
		this.writeCompletionHandler = new WriteCompletionHandler<>(this);
	}

	/**
	 * 
	 * @param asynchronousSocketChannel
	 * @return
	 * @throws IOException
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月6日 下午12:21:41
	 *
	 */
	public abstract Node createClientNode(AsynchronousSocketChannel asynchronousSocketChannel) throws IOException;

	@Override
	public String toString()
	{
		return this.getClientNode().toString();
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
	public Node getClientNode()
	{
		return clientNode;
	}

	/**
	 * @param asynchronousSocketChannel the asynchronousSocketChannel to set
	 */
	public void setAsynchronousSocketChannel(AsynchronousSocketChannel asynchronousSocketChannel)
	{
		this.asynchronousSocketChannel = asynchronousSocketChannel;

		if (asynchronousSocketChannel != null)
		{
			try
			{
				Node clientNode = createClientNode(asynchronousSocketChannel);
				setClientNode(clientNode);
			} catch (IOException e)
			{
				log.info(e.toString(), e);
				assignAnUnknownClientNode();
			}
		} else
		{
			assignAnUnknownClientNode();
		}
	}
	

	private void assignAnUnknownClientNode()
	{
		Node clientNode = new Node(UNKNOWN_ADDRESS_IP, UNKNOWN_ADDRESS_PORT_SEQ.incrementAndGet());
		setClientNode(clientNode);
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
	private void setClientNode(Node clientNode)
	{
		if (this.clientNode != null)
		{
			try
			{
				groupContext.getClientNodes().remove(this);
			} catch (Exception e1)
			{
				log.error(e1.toString(), e1);
			}
		}
		
		this.clientNode = clientNode;
		
		if (this.clientNode != null)
		{
			try
			{
				groupContext.getClientNodes().put(this);
			} catch (Exception e1)
			{
				log.error(e1.toString(), e1);
			}
		}
	}

	/**
	 * @return the groupContext
	 */
	public GroupContext<Ext, P, R> getGroupContext()
	{
		return groupContext;
	}

	/**
	 * @param groupContext the groupContext to set
	 */
	public void setGroupContext(GroupContext<Ext, P, R> groupContext)
	{
		this.groupContext = groupContext;

		if (groupContext != null)
		{
			decodeRunnable = new DecodeRunnable<>(this, groupContext.getDecodeExecutor());
//			closeRunnable = new CloseRunnable<>(this, null, null, groupContext.getCloseExecutor());

//			handlerRunnableHighPrior = new HandlerRunnable<>(this, groupContext.getHandlerExecutorHighPrior());
			handlerRunnableNormPrior = new HandlerRunnable<>(this, groupContext.getHandlerExecutorNormPrior());

//			sendRunnableHighPrior = new SendRunnable<>(this, groupContext.getSendExecutorHighPrior());
			sendRunnableNormPrior = new SendRunnable<>(this, groupContext.getSendExecutorNormPrior());

			MaintainUtils.addToMaintain(this);
		}
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

//	/**
//	 * @return the handlerRunnableHighPrior
//	 */
//	public HandlerRunnable<Ext, P, R> getHandlerRunnableHighPrior()
//	{
//		return handlerRunnableHighPrior;
//	}

//	/**
//	 * @param handlerRunnableHighPrior the handlerRunnableHighPrior to set
//	 */
//	public void setHandlerRunnableHighPrior(HandlerRunnable<Ext, P, R> handlerRunnableHighPrior)
//	{
//		this.handlerRunnableHighPrior = handlerRunnableHighPrior;
//	}

	/**
	 * @return the handlerRunnableNormPrior
	 */
	public HandlerRunnable<Ext, P, R> getHandlerRunnableNormPrior()
	{
		return handlerRunnableNormPrior;
	}

	/**
	 * @param handlerRunnableNormPrior the handlerRunnableNormPrior to set
	 */
	public void setHandlerRunnableNormPrior(HandlerRunnable<Ext, P, R> handlerRunnableNormPrior)
	{
		this.handlerRunnableNormPrior = handlerRunnableNormPrior;
	}

//	/**
//	 * @return the sendRunnableHighPrior
//	 */
//	public SendRunnable<Ext, P, R> getSendRunnableHighPrior()
//	{
//		return sendRunnableHighPrior;
//	}
//
//	/**
//	 * @param sendRunnableHighPrior the sendRunnableHighPrior to set
//	 */
//	public void setSendRunnableHighPrior(SendRunnable<Ext, P, R> sendRunnableHighPrior)
//	{
//		this.sendRunnableHighPrior = sendRunnableHighPrior;
//	}

	/**
	 * @return the sendRunnableNormPrior
	 */
	public SendRunnable<Ext, P, R> getSendRunnableNormPrior()
	{
		return sendRunnableNormPrior;
	}

	/**
	 * @param sendRunnableNormPrior the sendRunnableNormPrior to set
	 */
	public void setSendRunnableNormPrior(SendRunnable<Ext, P, R> sendRunnableNormPrior)
	{
		this.sendRunnableNormPrior = sendRunnableNormPrior;
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
		if (isClosed)
		{
			if (clientNode == null || (!UNKNOWN_ADDRESS_IP.equals(clientNode.getIp())))
			{
				assignAnUnknownClientNode();
			}
		}
	}

//	/**
//	 * @return the closeRunnable
//	 */
//	public CloseRunnable<Ext, P, R> getCloseRunnable()
//	{
//		return closeRunnable;
//	}
//
//	/**
//	 * @param closeRunnable the closeRunnable to set
//	 */
//	public void setCloseRunnable(CloseRunnable<Ext, P, R> closeRunnable)
//	{
//		this.closeRunnable = closeRunnable;
//	}

	/**
	 * @return the stat
	 */
	public Stat getStat()
	{
		return stat;
	}

	/**
	 * @param stat the stat to set
	 */
	public void setStat(Stat stat)
	{
		this.stat = stat;
	}

	//	/**
	//	 * @return the sendSemaphore
	//	 */
	//	public java.util.concurrent.Semaphore getSendSemaphore()
	//	{
	//		return sendSemaphore;
	//	}

	/**
	 * @return the writeCompletionHandler
	 */
	public WriteCompletionHandler<Ext, P, R> getWriteCompletionHandler()
	{
		return writeCompletionHandler;
	}

	/**
	 * @param writeCompletionHandler the writeCompletionHandler to set
	 */
	public void setWriteCompletionHandler(WriteCompletionHandler<Ext, P, R> writeCompletionHandler)
	{
		this.writeCompletionHandler = writeCompletionHandler;
	}

	/**
	 * @return the reConnCount
	 */
	public int getReconnCount()
	{
		return reconnCount;
	}

	/**
	 * @param reConnCount the reConnCount to set
	 */
	public void setReconnCount(int reconnCount)
	{
		this.reconnCount = reconnCount;
	}

	public static class Stat
	{
		/**
		 * 最近一次收到业务消息包的时间(一个完整的业务消息包，一部分消息不算)
		 */
		private long latestTimeOfReceivedPacket = SystemTimer.currentTimeMillis();

		/**
		 * 最近一次发送业务消息包的时间(一个完整的业务消息包，一部分消息不算)
		 */
		private long latestTimeOfSentPacket = SystemTimer.currentTimeMillis();
		
		/**
		 * 连接关闭的时间
		 */
		private long timeClosed = SystemTimer.currentTimeMillis();

		/**
		 * 本连接已发送的字节数
		 */
		private AtomicLong sentBytes = new AtomicLong();

		/**
		 * 本连接已发送的packet数
		 */
		private AtomicLong sentPackets = new AtomicLong();

		/**
		 * 本连接已处理的字节数
		 */
		private AtomicLong handledBytes = new AtomicLong();

		/**
		 * 本连接已处理的packet数
		 */
		private AtomicLong handledPackets = new AtomicLong();

		/**
		 * 本连接已接收的字节数
		 */
		private AtomicLong receivedBytes = new AtomicLong();

		/**
		 * 本连接已接收的packet数
		 */
		private AtomicLong receivedPackets = new AtomicLong();

		/**
		 * @return the timeLatestReceivedMsg
		 */
		public long getLatestTimeOfReceivedPacket()
		{
			return latestTimeOfReceivedPacket;
		}

		/**
		 * @param timeLatestReceivedMsg the timeLatestReceivedMsg to set
		 */
		public void setLatestTimeOfReceivedPacket(long latestTimeOfReceivedPacket)
		{
			this.latestTimeOfReceivedPacket = latestTimeOfReceivedPacket;
		}

		/**
		 * @return the timeLatestSentMsg
		 */
		public long getLatestTimeOfSentPacket()
		{
			return latestTimeOfSentPacket;
		}

		/**
		 * @param timeLatestSentMsg the timeLatestSentMsg to set
		 */
		public void setLatestTimeOfSentPacket(long latestTimeOfSentPacket)
		{
			this.latestTimeOfSentPacket = latestTimeOfSentPacket;
		}

		/**
		 * @return the countSentByte
		 */
		public AtomicLong getSentBytes()
		{
			return sentBytes;
		}

		/**
		 * @param countSentByte the countSentByte to set
		 */
		public void setSentBytes(AtomicLong sentBytes)
		{
			this.sentBytes = sentBytes;
		}

		/**
		 * @return the countSentPacket
		 */
		public AtomicLong getSentPackets()
		{
			return sentPackets;
		}

		/**
		 * @param countSentPacket the countSentPacket to set
		 */
		public void setSentPackets(AtomicLong sentPackets)
		{
			this.sentPackets = sentPackets;
		}

		/**
		 * @return the countHandledByte
		 */
		public AtomicLong getHandledBytes()
		{
			return handledBytes;
		}

		/**
		 * @param countHandledByte the countHandledByte to set
		 */
		public void setHandledBytes(AtomicLong countHandledByte)
		{
			this.handledBytes = countHandledByte;
		}

		/**
		 * @return the countHandledPacket
		 */
		public AtomicLong getHandledPackets()
		{
			return handledPackets;
		}

		/**
		 * @param countHandledPacket the countHandledPacket to set
		 */
		public void setHandledPackets(AtomicLong handledPackets)
		{
			this.handledPackets = handledPackets;
		}

		/**
		 * @return the countReceivedByte
		 */
		public AtomicLong getReceivedBytes()
		{
			return receivedBytes;
		}

		/**
		 * @param countReceivedByte the countReceivedByte to set
		 */
		public void setReceivedBytes(AtomicLong receivedBytes)
		{
			this.receivedBytes = receivedBytes;
		}

		/**
		 * @return the countReceivedPacket
		 */
		public AtomicLong getReceivedPackets()
		{
			return receivedPackets;
		}

		/**
		 * @param countReceivedPacket the countReceivedPacket to set
		 */
		public void setReceivedPackets(AtomicLong receivedPackets)
		{
			this.receivedPackets = receivedPackets;
		}

		/**
		 * @return the timeClosed
		 */
		public long getTimeClosed()
		{
			return timeClosed;
		}

		/**
		 * @param timeClosed the timeClosed to set
		 */
		public void setTimeClosed(long timeClosed)
		{
			this.timeClosed = timeClosed;
		}

	}

	/**
	 * @return the isRemoved
	 */
	public boolean isRemoved()
	{
		return isRemoved;
	}

	/**
	 * @param isRemoved the isRemoved to set
	 */
	public void setRemoved(boolean isRemoved)
	{
		this.isRemoved = isRemoved;
	}

	/**
	 * @return the serverNode
	 */
	public Node getServerNode()
	{
		return serverNode;
	}

	/**
	 * @param serverNode the serverNode to set
	 */
	public void setServerNode(Node serverNode)
	{
		this.serverNode = serverNode;
	}

	/**
	 * @return the closeLock
	 */
	public ReentrantReadWriteLock getCloseLock()
	{
		return closeLock;
	}

}
