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
package com.talent.aio.examples.im.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.server.AioServer;
import com.talent.aio.server.ServerGroupContext;
import com.talent.aio.server.intf.ServerAioHandler;
import com.talent.aio.server.intf.ServerAioListener;

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
public class ImServerStarter
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(ImServerStarter.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public ImServerStarter()
	{

	}

	static ServerGroupContext<Object, ImPacket, Object> serverGroupContext = null;

	static AioServer<Object, ImPacket, Object> aioServer = null;

	static ServerAioHandler<Object, ImPacket, Object> aioHandler = null;

	static ServerAioListener<Object, ImPacket, Object> aioListener = null;

	static String ip = null;

	static int port = 9321;

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @throws IOException 
	 * @创建时间:　2016年11月17日 下午5:59:24
	 * 
	 */
	public static void main(String[] args) throws IOException
	{
		aioHandler = new ImServerAioHandler();
		aioListener = new ImServerAioListener();
		serverGroupContext = new ServerGroupContext<>(aioHandler, aioListener);
		serverGroupContext.setEncodeCareWithChannelContext(false);
		aioServer = new AioServer<>(serverGroupContext);
		aioServer.start(ip, port);
	}

}
