/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月17日 下午5:59:24
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.ClientAioHandler;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.ClientGroupStat;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.Const.ChatType;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.bs.AuthReqBody;
import com.talent.aio.examples.im.common.bs.ChatReqBody;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.utils.Md5;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月17日 下午5:59:24
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月17日 | tanyaowu | 新建类
 *
 */
public class ImClientStarter
{
	private static Logger log = LoggerFactory.getLogger(ImClientStarter.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImClientStarter(String serverIp, int serverPort) throws IOException
	{
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		aioClientHandler = new ImClientAioHandler();
		aioListener = new ImClientAioListener();
		clientGroupContext = new ClientGroupContext<>(serverIp, serverPort, aioClientHandler, aioListener);
		aioClient = new AioClient<>(clientGroupContext);
	}

	private String serverIp = null; //服务器的IP地址

	private int serverPort = 0;     //服务器的PORT


	private AioClient<Object, ImPacket, Object> aioClient;

	private ClientGroupContext<Object, ImPacket, Object> clientGroupContext = null;

	private ClientAioHandler<Object, ImPacket, Object> aioClientHandler = null;

	private AioListener<Object, ImPacket, Object> aioListener = null;
	
	//--------------

	public static String SERVER_IP = "127.0.0.1"; //服务器的IP地址

	public static int SERVER_PORT = 9321; //服务器的PORT
	
	public static AtomicLong SEQ = new AtomicLong();

	public static String groupid = "89889_1"; //消息群组id
	
//	private static int clientCount = 16000; //与服务器建立多少个TCP长连接
//
//	/**
//	 * 每秒向组发送多少条消息(如果是测试服务器可以建立多少个TCP长连接，此值设为0，因为客户端会定时发心跳)
//	 * 当此值设为10，clientCount设为10000时，本客户端每10秒就会发送一条群消息，但是服务器收到这10条消息后会分发10*10000条消息到本客户端。如果有很多个客户端，消息量还要成倍增加
//	 */
//	private static int sendCountPerSecond = 4; //


	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		com.talent.aio.examples.im.client.ui.JFrameMain.main(args);
		
		
		
//		ImClientStarter imClientStarter = new ImClientStarter(SERVER_IP, SERVER_PORT);
//		for (int i = 0; i < clientCount; i++)
//		{
//			ClientChannelContext<Object, ImPacket, Object> clientChannelContext = imClientStarter.getAioClient().connect();
//			String did = "did-" + i;
//			String token = "token-" + i;
//			String info = "info-" + i;
//			Long seq = SEQ.incrementAndGet();
//			ImPacket packet = createAuthPacket(did, token, info, seq);
//			Aio.send(clientChannelContext, packet);
//		}
//
//		new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				while (true)
//				{
//					try
//					{
//						Thread.sleep(10000);
//
//						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> objWithReadWriteLock = imClientStarter.getClientGroupContext().getGroups().clients(groupid);
//						if (objWithReadWriteLock != null)
//						{
//							ReadLock readLock = objWithReadWriteLock.getLock().readLock();
//							try
//							{
//								readLock.lock();
//								Set<ChannelContext<Object, ImPacket, Object>> set = objWithReadWriteLock.getObj();
//								int i = 0;
//								//log.error("send msg to group {}", groupid);
//								label_2: for (ChannelContext<Object, ImPacket, Object> entry : set)
//								{
//									if (i >= sendCountPerSecond)
//									{
//										break label_2;
//									}
//									String msg = "hello-" + i++;
//									ChatReqBody chatReqBody = new ChatReqBody(ChatType.pub, msg, groupid, null, null);
//									ImPacket imReqPacket = new ImPacket();
//									imReqPacket.setCommand(Command.CHAT_REQ);
//									imReqPacket.setBody(Json.toJson(chatReqBody).getBytes(ImPacket.CHARSET));
//									Aio.send(entry, imReqPacket);//.sendToGroup(clientGroupContext, groupid, imReqPacket);
//
//								}
//							} catch (Throwable e)
//							{
//								log.error("", e);
//							} finally
//							{
//								readLock.unlock();
//							}
//						}
//						String id = imClientStarter.getClientGroupContext().getId();
//						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> connections = imClientStarter.getClientGroupContext().getConnections().getSet();
//						Set<ChannelContext<Object, ImPacket, Object>> set = connections.getObj();
//						ClientGroupStat clientGroupStat = imClientStarter.getClientGroupContext().getClientGroupStat();
//						log.error("[{}]:[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", SystemTimer.currentTimeMillis(), id, set.size(),
//								clientGroupStat.getClosed().get(), clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(),
//								clientGroupStat.getHandledPacket().get(), clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());
//
//						try
//						{
//							log.error("[{}]: command stat:{}", SystemTimer.currentTimeMillis(), Json.toJson(CommandStat.commandAndCount));
//						} catch (Exception e1)
//						{
//							// may be ConcurrentModificationException,  skip it
//						}
//
//					} catch (Throwable e)
//					{
//						log.error("", e);
//					}
//				}
//			}
//		}, "t-send-chatmsg-").start();

	}

	/**
	 * 构建鉴权包
	 * @return
	 * @throws Exception 
	 */
	public static ImPacket createAuthPacket(String did, String token, String info, Long seq) throws Exception
	{
		ImPacket imReqPacket = new ImPacket();
		imReqPacket.setCommand(Command.AUTH_REQ);

		AuthReqBody authReqBody = new AuthReqBody();
		authReqBody.setDeviceId(did);
		authReqBody.setSeq(seq);
		authReqBody.setDeviceType((byte) 2);
		authReqBody.setDeviceInfo(info);
		authReqBody.setToken(token);

		did = did == null ? "" : did;
		token = token == null ? "" : token;
		info = info == null ? "" : info;
		seq = seq == null ? 0 : seq;

		String data = token + did + info + seq + "fdsfeofa";
		String sign = null;
		try
		{
			sign = Md5.getMD5(data);//DesUtils.encrypt(data, ImClientConfig.getInstance().getString("im.auth.private.key", "fdsfeofa"));
		} catch (Exception e)
		{
			log.error(e.toString(), e);
			throw new RuntimeException(e);
		}
		authReqBody.setSign(sign);

		imReqPacket.setBody(Json.toJson(authReqBody).getBytes(ImPacket.CHARSET));
		return imReqPacket;
	}

	/**
	 * @return the serverIp
	 */
	public String getServerIp()
	{
		return serverIp;
	}

	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp)
	{
		this.serverIp = serverIp;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort()
	{
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	/**
	 * @return the aioClient
	 */
	public AioClient<Object, ImPacket, Object> getAioClient()
	{
		return aioClient;
	}

	/**
	 * @param aioClient the aioClient to set
	 */
	public void setAioClient(AioClient<Object, ImPacket, Object> aioClient)
	{
		this.aioClient = aioClient;
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<Object, ImPacket, Object> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<Object, ImPacket, Object> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}

	/**
	 * @return the aioClientHandler
	 */
	public ClientAioHandler<Object, ImPacket, Object> getAioClientHandler()
	{
		return aioClientHandler;
	}

	/**
	 * @param aioClientHandler the aioClientHandler to set
	 */
	public void setAioClientHandler(ClientAioHandler<Object, ImPacket, Object> aioClientHandler)
	{
		this.aioClientHandler = aioClientHandler;
	}

	/**
	 * @return the aioListener
	 */
	public AioListener<Object, ImPacket, Object> getAioListener()
	{
		return aioListener;
	}

	/**
	 * @param aioListener the aioListener to set
	 */
	public void setAioListener(AioListener<Object, ImPacket, Object> aioListener)
	{
		this.aioListener = aioListener;
	}

}
