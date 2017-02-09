package com.talent.aio.examples.im.server.handler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.ImPacket;


public class HeartbeatHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);

	@PostConstruct
	public void init()
	{
	}

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		if (log.isInfoEnabled())
		{
			log.info("{}收到心跳包", channelContext.toString());
		}
		
		return null;
	}
}
