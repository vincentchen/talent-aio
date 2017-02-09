package com.talent.aio.examples.im.server.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.packets.AuthReqBody;
import com.talent.aio.examples.im.common.packets.DeviceType;


/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月8日 下午3:29:46
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月8日 | tanyaowu | 新建类
 *
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
