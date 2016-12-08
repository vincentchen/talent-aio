/**
 * 
 */
package com.talent.aio.examples.server.im;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.common.im.Command;
import com.talent.aio.examples.common.im.ImBytePacket;
import com.talent.aio.examples.common.im.ImPacket;

/**
 * 
 * 
 * @filename:	 com.talent.im.server.packet.ServerPackets
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2013年3月24日 下午1:31:30
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
* 	<tr><td>2013年3月24日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ServerPackets
{
	private static Logger log = LoggerFactory.getLogger(ServerPackets.class);

	/**
	 * 
	 */
	public ServerPackets()
	{

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}
	
	/**
	 * 
	 * @param packet
	 * @return
	 */
	public static ImBytePacket toByteOnlyPacket(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext)
	{
		ByteBuffer bs = channelContext.getGroupContext().getAioHandler().encode(packet, channelContext);
		ImBytePacket byteOnlyPacket = new ImBytePacket(bs);
		byteOnlyPacket.setCommand(packet.getCommand());
		byteOnlyPacket.setBodyLen(packet.getBodyLen());
		if (packet.getSynSeq() != null)
		{
			byteOnlyPacket.setSynSeq(packet.getSynSeq());
		}
		
		byteOnlyPacket.setBody(packet.getBody());
		return byteOnlyPacket;
	}

	private static ImBytePacket authRespPacket = null;

	/**
	 * 构建鉴权响应包
	 * @return
	 */
	public static ImPacket createAuthRespPacket(ChannelContext<Object, ImPacket, Object> channelContext)
	{
		if (authRespPacket == null)
		{
			synchronized (log)
			{
				if (authRespPacket == null)
				{
					ImPacket imRequestPacket = new ImPacket();
					imRequestPacket.setCommand(Command.AUTH_RESP);
					authRespPacket = toByteOnlyPacket(imRequestPacket, channelContext);
				}
			}
		}
		return authRespPacket;
	}
}
