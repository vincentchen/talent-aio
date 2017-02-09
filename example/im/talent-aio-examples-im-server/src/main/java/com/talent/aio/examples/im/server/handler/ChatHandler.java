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
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月8日 下午3:29:37
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月8日 | tanyaowu | 新建类
 *
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
			builder.setToId(toId);
			builder.setToNick(toNick);
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
				if (toId != null)
				{
					Aio.sendToUser(channelContext.getGroupContext(), toId + "", respPacket);
				}
			}
		}
		return null;
	}
}
