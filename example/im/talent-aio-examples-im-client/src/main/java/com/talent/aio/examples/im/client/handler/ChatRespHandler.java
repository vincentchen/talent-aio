/**
 * 
 */
package com.talent.aio.examples.im.client.handler;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.client.ui.JFrameMain;
import com.talent.aio.examples.im.common.ImPacket;

/**
 * 
 * @filename:	 com.talent.im.client.handler.bs.ChatRespHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年7月1日 下午1:31:50
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年7月1日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ChatRespHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(ChatRespHandler.class);

	/**
	 * 
	 */
	public ChatRespHandler()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		
		return null;
	}
}
