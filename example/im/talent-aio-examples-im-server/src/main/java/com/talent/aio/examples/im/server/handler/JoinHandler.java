package com.talent.aio.examples.im.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.packets.JoinGroupResult;
import com.talent.aio.examples.im.common.packets.JoinReqBody;
import com.talent.aio.examples.im.common.packets.JoinReqBody.Builder;
import com.talent.aio.examples.im.common.packets.JoinRespBody;
import com.talent.aio.examples.im.common.vo.JoinGroupResultVo;

/**
 * 手机端进入房间
 * 
 * @filename:	 com.talent.im.server.handler.bs.TcpJoinHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年4月3日 下午5:19:16
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年4月3日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class JoinHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(JoinHandler.class);

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		if (packet.getBody() == null)
		{
			throw new Exception("body is null");
		}

		JoinReqBody reqBody = JoinReqBody.parseFrom(packet.getBody());
		
		
		
		if (log.isInfoEnabled())
		{
			log.info("{}收到join包{}", channelContext.toString(), reqBody.toString());
		}
		
//		JoinReqBody joinReqBody = Json.toBean(bodyStr, JoinReqBody.class);
		String group = reqBody.getGroup();
		if (StringUtils.isBlank(group))
		{
			log.error("group is null,{}", channelContext);
			Aio.close(channelContext, "group is null when join group");
			return null;
		}

//		channelContext.getGroupContext().getGroups().bind(groupid, channelContext);
		com.talent.aio.common.Aio.bindGroup(channelContext, group);

		
		JoinGroupResult joinGroupResult = JoinGroupResult.newBuilder().setCode(JoinGroupResultVo.Code.OK).build();
		JoinRespBody joinRespBody = JoinRespBody.newBuilder().setTime(SystemTimer.currentTimeMillis()).setResult(joinGroupResult).setGroup(group).build();
		byte[] body = joinRespBody.toByteArray();
		
		
		ImPacket respPacket = new ImPacket();
		respPacket.setCommand(Command.JOIN_GROUP_RESP);
		respPacket.setBody(body);
		
		Aio.send(channelContext, respPacket);
	
		return null;
	}
}
