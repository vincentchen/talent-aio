package com.talent.aio.examples.im.server.handler;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.Const;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.packets.ChatReqBody;
import com.talent.aio.examples.im.common.packets.ChatRespBody;

/**
 * 聊天
 * 
 * @filename:	 com.talent.app.chat.handler.LeaveHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年3月4日 下午2:50:10
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年3月4日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class ChatHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(ChatHandler.class);

	@PostConstruct
	public void init()
	{
	}

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		
		if (packet.getBody() == null)
		{
			throw new Exception("body is null");
		}
		
		
		ChatReqBody chatReqBody = ChatReqBody.parseFrom(packet.getBody());

		Integer fromId = 111;
		String fromNick = "test";



		Integer toId = chatReqBody.getToId();
		String toNick = chatReqBody.getToNick();
		String toGroup = chatReqBody.getGroup();


		if (chatReqBody != null)
		{

			ChatRespBody.Builder builder = ChatRespBody.newBuilder();
			builder.setType(chatReqBody.getType());
			builder.setText(chatReqBody.getText());
			builder.setFromId(fromId);
			builder.setFromNick(fromNick);
			
//			if (toId != null)
//			{
				builder.setToId(toId);
//			}
//			if (StringUtils.isNotBlank(toNick))
//			{
				builder.setToNick(toNick);
//			}
			
			builder.setGroup(toGroup);
			builder.setTime(SystemTimer.currentTimeMillis());
			ChatRespBody chatRespBody = builder.build();
			byte[] bodybyte = chatRespBody.toByteArray();
			
			
			ImPacket respPacket = new ImPacket();
			respPacket.setCommand(Command.CHAT_RESP);
			
			respPacket.setBody(bodybyte);
			
			if (Objects.equals(Const.ChatType.pub, chatReqBody.getType()))
			{
				Aio.sendToGroup(channelContext.getGroupContext(), toGroup, respPacket);
			} else if (Objects.equals(Const.ChatType.pri, chatReqBody.getType()))
			{
//				MsgSendApi.deliverToUser(chatRespBody, fromId, Command.CHAT_RESP);
//				MsgSendApi.deliverToUser(chatRespBody, toId, Command.CHAT_RESP);
			}
			
		}
		return null;
	}

	/**
	 * 发送消息到手机客户端
	 * @param datas
	 */
	//	public void sendMsgToMobile(Map<String, Object> datas, Integer staruserid)
	//	{
	////		ChatRequestBody chatRequestBody = ChatRequestBody.wrapFromMap(datas);
	//
	//		if (staruserid == null || staruserid == -1)
	//		{
	//			log.warn("staruserid is {}", staruserid);
	//			return;
	//		}
	//
	//		Map<Integer, Collection<ChannelContext<ImServerChannelContextExt>>> map = TcpJoinHandler.staruseridAndChannelContext;
	//		Collection<ChannelContext<ImServerChannelContextExt>> set = map.get(staruserid);
	//
	//		if (set == null || set.size() == 0)
	//		{
	//			log.info("房间没有人在用手机聊天");
	//			return;
	//		}
	//
	//		ChatRequestPacket packet = new ChatRequestPacket();
	//		packet.setCommand(CommandExt.CHAT_RESP);
	//		packet.setBody(Json.toJson(datas));
	//		ClientApi.sendServerPacketToChannelContexts(packet, set);
	//		//
	//		//		ByteOnlyPacket byteOnlyPacket = new ByteOnlyPacket(packet.toBytes());
	//		//
	//		//		for (ChannelContext channelContext : set)
	//		//		{
	//		//			try
	//		//			{
	//		//				Nio.getInstance().asySend(byteOnlyPacket, channelContext);
	//		//			} catch (Exception e)
	//		//			{
	//		//				log.error(channelContext.getId() + e.getMessage());
	//		//			}
	//		//		}
	//
	//	}
}
