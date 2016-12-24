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
package com.talent.aio.examples.helloworld.client;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.AioClient;
import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.client.ClientGroupContext;
import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.intf.AioListener;
import com.talent.aio.examples.helloworld.common.HelloPacket;

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
public class HelloClientStarter
{
	private static Logger log = LoggerFactory.getLogger(HelloClientStarter.class);

	private static String serverIp = null; //服务器的IP地址

	private static int serverPort = 0; //服务器的PORT

	private static AioClient<Object, HelloPacket, Object> aioClient;

	private static ClientGroupContext<Object, HelloPacket, Object> clientGroupContext = null;

	private static ClientAioHandler<Object, HelloPacket, Object> aioClientHandler = null;

	private static ClientAioListener<Object, HelloPacket, Object> aioListener = null;

	//--------------

	public static String SERVER_IP = "127.0.0.1"; //服务器的IP地址

	public static int SERVER_PORT = 9321; //服务器的PORT

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public HelloClientStarter() throws IOException
	{

	}

	public static AtomicLong SEQ = new AtomicLong();

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
		serverIp = "127.0.0.1";
		serverPort = com.talent.aio.examples.helloworld.common.Const.PORT;
		aioClientHandler = new HelloClientAioHandler();
		aioListener = null;
		clientGroupContext = new ClientGroupContext<>(serverIp, serverPort, aioClientHandler, aioListener);
		aioClient = new AioClient<>(clientGroupContext);

		String bindIp = null;
		int bindPort = 0;
		boolean autoReconnect = false; //暂时不支持自动重连，需要业务自己实现，后续版本会支持此属性为true
		ClientChannelContext<Object, HelloPacket, Object> clientChannelContext = aioClient.connect(bindIp, bindPort, autoReconnect);

		HelloPacket packet = new HelloPacket();
		packet.setBody("hello world".getBytes(HelloPacket.CHARSET));

		Aio.send(clientChannelContext, packet);
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
	public AioClient<Object, HelloPacket, Object> getAioClient()
	{
		return aioClient;
	}

	/**
	 * @param aioClient the aioClient to set
	 */
	public void setAioClient(AioClient<Object, HelloPacket, Object> aioClient)
	{
		this.aioClient = aioClient;
	}

	/**
	 * @return the clientGroupContext
	 */
	public ClientGroupContext<Object, HelloPacket, Object> getClientGroupContext()
	{
		return clientGroupContext;
	}

	/**
	 * @param clientGroupContext the clientGroupContext to set
	 */
	public void setClientGroupContext(ClientGroupContext<Object, HelloPacket, Object> clientGroupContext)
	{
		this.clientGroupContext = clientGroupContext;
	}

	/**
	 * @return the aioClientHandler
	 */
	public ClientAioHandler<Object, HelloPacket, Object> getAioClientHandler()
	{
		return aioClientHandler;
	}

	/**
	 * @param aioClientHandler the aioClientHandler to set
	 */
	public void setAioClientHandler(ClientAioHandler<Object, HelloPacket, Object> aioClientHandler)
	{
		this.aioClientHandler = aioClientHandler;
	}

	/**
	 * @return the aioListener
	 */
	public AioListener<Object, HelloPacket, Object> getAioListener()
	{
		return aioListener;
	}

	/**
	 * @param aioListener the aioListener to set
	 */
	public void setAioListener(ClientAioListener<Object, HelloPacket, Object> aioListener)
	{
		this.aioListener = aioListener;
	}

}
