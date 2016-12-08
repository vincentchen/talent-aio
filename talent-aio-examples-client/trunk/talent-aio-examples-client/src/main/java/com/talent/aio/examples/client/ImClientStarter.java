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
package com.talent.aio.examples.client;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.AioClientHandler;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.ClientGroupStat;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.ObjWithReadWriteLock;
import com.talent.aio.examples.common.im.Command;
import com.talent.aio.examples.common.im.CommandStat;
import com.talent.aio.examples.common.im.Const.ChatType;
import com.talent.aio.examples.common.im.ImPacket;
import com.talent.aio.examples.common.im.ImSendListener;
import com.talent.aio.examples.common.im.bs.AuthReqBody;
import com.talent.aio.examples.common.im.bs.ChatReqBody;
import com.talent.aio.examples.common.json.Json;
import com.talent.aio.examples.common.utils.Md5;

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
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImClientStarter()
	{

	}

	static ClientGroupContext<Object, ImPacket, Object> clientGroupContext = null;

	static AioClientHandler<Object, ImPacket, Object> aioClientHandler = null;

	static String ip = "127.0.0.1";

	static int port = 9321;

	static AioClient<Object, ImPacket, Object> aioClient;

	static int clientCount = 500;

	static java.util.concurrent.atomic.AtomicLong SEQ = new AtomicLong();

	public static String groupid = "89889";

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
		aioClientHandler = new ImAioClientHandler();
		ImSendListener imSendListener = new ImSendListener();
		clientGroupContext = new ClientGroupContext<>(ip, port, aioClientHandler, imSendListener);
		aioClient = new AioClient<>(clientGroupContext);

		for (int i = 0; i < clientCount; i++)
		{
			ClientChannelContext<Object, ImPacket, Object> clientChannelContext = aioClient.connect();
			String did = "did-" + i;
			String token = "token-" + i;
			String info = "info-" + i;
			Long seq = SEQ.incrementAndGet();
			ImPacket packet = createAuthPacket(did, token, info, seq);
			Aio.send(clientChannelContext, packet);
		}

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Thread.sleep(10000);

						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> objWithReadWriteLock = clientGroupContext.getGroups().clients(groupid);
						if (objWithReadWriteLock != null)
						{
							ReadLock readLock = objWithReadWriteLock.getLock().readLock();
							try
							{
								readLock.lock();
								Set<ChannelContext<Object, ImPacket, Object>> set = objWithReadWriteLock.getObj();
								int i = 1;
								log.error("send msg to group {}", groupid);
								for (ChannelContext<Object, ImPacket, Object> entry : set)
								{
									String msg = "hello-" + i++;
									ChatReqBody chatReqBody = new ChatReqBody(ChatType.pub, msg, groupid, null, null);
									ImPacket imReqPacket = new ImPacket();
									imReqPacket.setCommand(Command.CHAT_REQ);
									imReqPacket.setBody(Json.toJson(chatReqBody).getBytes(ImPacket.CHARSET));
									Aio.send(entry, imReqPacket);//.sendToGroup(clientGroupContext, groupid, imReqPacket);

								}
							} catch (Throwable e)
							{
								log.error("", e);
							} finally
							{
								readLock.unlock();
							}
						}
						String id = clientGroupContext.getId();
						ObjWithReadWriteLock<Set<ChannelContext<Object, ImPacket, Object>>> connections = clientGroupContext.getConnections().getSet();
						Set<ChannelContext<Object, ImPacket, Object>> set = connections.getObj();
						ClientGroupStat clientGroupStat = clientGroupContext.getClientGroupStat();
						log.error("[{}]: curr:{}, closed:{}, received:({}p)({}b), handled:{}, sent:({}p)({}b)", id, set.size(), clientGroupStat.getClosed().get(),
								clientGroupStat.getReceivedPacket().get(), clientGroupStat.getReceivedBytes().get(), clientGroupStat.getHandledPacket().get(),
								clientGroupStat.getSentPacket().get(), clientGroupStat.getSentBytes().get());

						synchronized (CommandStat.commandAndCount)
						{
							log.error("command stat:{}", Json.toJson(CommandStat.commandAndCount));
						}

					} catch (Throwable e)
					{
						log.error("", e);
					}
				}
			}
		}, "t-send-chatmsg-").start();

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

}
