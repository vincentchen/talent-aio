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
package com.talent.aio.examples.server.im;

import java.io.IOException;

import com.talent.aio.common.config.AioServerConfig;
import com.talent.aio.common.intf.AioHandler;
import com.talent.aio.examples.common.im.ImPacket;
import com.talent.aio.server.AioServer;

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
	static AioServerConfig<Object, ImPacket, Object> aioServerConfig = null;

	static AioServer<Object, ImPacket, Object> aioServer = null;
	
	static AioHandler<Object, ImPacket, Object> aioHandler = null;
	
	static String ip;
	
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
		aioHandler = new ImAioHandler();
		aioServerConfig = new AioServerConfig<>(ip, port, aioHandler);
		aioServer = new AioServer<>(aioServerConfig);
		aioServer.start();
	}

}
