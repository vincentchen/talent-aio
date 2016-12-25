/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-server
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月18日 上午9:13:15
 *
 * **************************************************************************
 */
package com.talent.aio.examples.helloworld.client;

import com.talent.aio.client.intf.ClientAioHandler;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.helloworld.common.HelloAbsAioHandler;
import com.talent.aio.examples.helloworld.common.HelloPacket;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月18日 上午9:13:15
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月18日 | tanyaowu | 新建类
 *
 */
public class HelloClientAioHandler extends HelloAbsAioHandler implements ClientAioHandler<Object, HelloPacket, Object>
{
	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年11月18日 上午9:13:15
	 * 
	 */
	public HelloClientAioHandler()
	{
	}

	/** 
	 * @see com.talent.aio.common.intf.AioHandler#handler(com.talent.aio.common.intf.Packet)
	 * 
	 * @param packet
	 * @return
	 * @throws Exception 
	 * @重写人: tanyaowu
	 * @重写时间: 2016年11月18日 上午9:37:44
	 * 
	 */
	@Override
	public Object handler(HelloPacket packet, ChannelContext<Object, HelloPacket, Object> channelContext) throws Exception
	{
		byte[] body = packet.getBody();
		if (body != null)
		{
			String str = new String(body, HelloPacket.CHARSET);
			System.out.println("收到消息：" + str);
		}

		return null;
	}

	private static HelloPacket heartbeatPacket = new HelloPacket();

	/** 
	 * @see com.talent.aio.client.intf.ClientAioHandler#heartbeatPacket()
	 * 
	 * @return
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月6日 下午2:18:16
	 * 
	 */
	@Override
	public HelloPacket heartbeatPacket()
	{
		return heartbeatPacket;
	}

}
