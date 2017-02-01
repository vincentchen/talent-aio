/**
 * 
 */
package com.talent.aio.examples.im.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.json.Json;
import com.talent.aio.examples.im.common.packets.AuthReqBody;
import com.talent.aio.examples.im.common.packets.DeviceType;
import com.talent.aio.examples.im.common.utils.Md5;


/**
 * 
 * 鉴权处理者
 * @filename:	 com.talent.im.server.handler.bs.AuthorizationHandler
 * @copyright:   Copyright (c)2010
 * @company:     talent
 * @author:      谭耀武
 * @version:     1.0
 * @create time: 2014年3月19日 下午4:23:04
 * @record
 * <table cellPadding="3" cellSpacing="0" style="width:600px">
 * <thead style="font-weight:bold;background-color:#e3e197">
 * 	<tr>   <td>date</td>	<td>author</td>		<td>version</td>	<td>description</td></tr>
 * </thead>
 * <tbody style="background-color:#ffffeb">
 * 	<tr><td>2014年3月19日</td>	<td>谭耀武</td>	<td>1.0</td>	<td>create</td></tr>
 * </tbody>
 * </table>
 */
public class AuthHandler implements ImBsAioHandlerIntf
{
	private static Logger log = LoggerFactory.getLogger(AuthHandler.class);

	//	private static final byte tokenIndex = 0;

	/**
	 * 
	 */
	public AuthHandler()
	{

	}

	@Override
	public Object handler(ImPacket packet, ChannelContext<Object, ImPacket, Object> channelContext) throws Exception
	{
		if (packet.getBody() == null)
		{
			throw new Exception("body is null");
		}
		
		
		AuthReqBody authReqBody = AuthReqBody.parseFrom(packet.getBody());
		String token = authReqBody.getToken();
		String deviceId = authReqBody.getDeviceId();
		String deviceInfo = authReqBody.getDeviceInfo();
		Long seq = authReqBody.getSeq();
		String sign = authReqBody.getSign();

		if (StringUtils.isBlank(deviceId))
		{
			Aio.close(channelContext, "did is null");
			return null;
		}

		if (seq == null || seq <= 0)
		{
			Aio.close(channelContext, "seq is null");
			return null;
		}

		token = token == null ? "" : token;
		deviceInfo = deviceInfo == null ? "" : deviceInfo;

		String data = token + deviceId + deviceInfo + seq + com.talent.aio.examples.im.common.Const.authkey;

//		try
//		{
//			String _sign = Md5.getMD5(data);
//			if (!_sign.equals(sign))
//			{
//				log.error("sign is invalid, {}, actual sign:{},expect sign:{}", channelContext.toString(), sign, _sign);
//				Aio.close(channelContext, "sign is invalid");
//				return null;
//			}
//		} catch (Exception e)
//		{
//			log.error(e.toString(), e);
//			Aio.close(channelContext, e.getMessage());
//			return null;
//		}

		DeviceType deviceType = authReqBody.getDeviceType();

		ImPacket imRespPacket = new ImPacket();
		imRespPacket.setCommand(Command.AUTH_RESP);
		
		Aio.send(channelContext, imRespPacket);
		return null;
	}



	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		
	}
}
