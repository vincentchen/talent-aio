/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-client
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月16日 下午5:52:06
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talent.aio.client.intf.ClientAioListener;
import com.talent.aio.common.Aio;
import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.utils.SystemTimer;
import com.talent.aio.examples.im.client.ui.JFrameMain;
import com.talent.aio.examples.im.common.Command;
import com.talent.aio.examples.im.common.CommandStat;
import com.talent.aio.examples.im.common.ImPacket;
import com.talent.aio.examples.im.common.packets.AuthReqBody;
import com.talent.aio.examples.im.common.packets.DeviceType;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月16日 下午5:52:06
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月16日 | tanyaowu | 新建类
 *
 */
public class ImClientAioListener implements ClientAioListener<Object, ImPacket, Object>
{
	private static Logger log = LoggerFactory.getLogger(ImClientAioListener.class);

	/**
	 * 
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public ImClientAioListener()
	{
	}

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午5:52:06
	 * 
	 */
	public static void main(String[] args)
	{
	}

	@Override
	public boolean onAfterReconnected(ChannelContext<Object, ImPacket, Object> initChannelContext, boolean isConnected)
	{
		if (isConnected)
		{
			JFrameMain jFrameMain = JFrameMain.getInstance();
//			synchronized (jFrameMain.getClients())
//			{
				jFrameMain.getClients().updateUI();
//			}
		}
		return true;
	}

	@Override
	public void onAfterConnected(ChannelContext<Object, ImPacket, Object> channelContext, boolean isConnected, boolean isReconnect)
	{
//		JFrameMain jFrameMain = JFrameMain.getInstance();
//		synchronized (jFrameMain)
//		{
//			try
//			{
//				jFrameMain.getListModel().addElement(channelContext);
////				jFrameMain.getClients().setModel(jFrameMain.getListModel());
//			} catch (Exception e)
//			{
//
//			}
//
////			jFrameMain.updateClientCount();
//		}
		
		JFrameMain.updateConnectionCount();
		
		if (!isConnected)
		{
			//没连上
			return;
		}
		
		String did = "did";
		String token = "token";
		String info = "info";
		Long seq = 1L;
		ImPacket respPacket;
		try
		{
			respPacket = createAuthPacket(did, token, info, seq);
			Aio.send(channelContext, respPacket);
		} catch (Exception e)
		{
			log.error(e.toString(), e);
		}
		
		return;
	}
	
	
	
	
	
	
	
	/**
	 * 构建鉴权包
	 * @return
	 * @throws Exception 
	 */
	public static ImPacket createAuthPacket(String did, String token, String info, Long seq) throws Exception
	{
		ImPacket imReqPacket = new ImPacket();
		imReqPacket.setCommand(Command.AUTH_REQ);

		AuthReqBody.Builder authReqBodyBuilder = com.talent.aio.examples.im.common.packets.AuthReqBody.newBuilder();
		authReqBodyBuilder.setDeviceId(did);
		authReqBodyBuilder.setSeq(seq);
		authReqBodyBuilder.setDeviceType(DeviceType.DEVICE_TYPE_ANDROID);
		authReqBodyBuilder.setDeviceInfo(info);
		authReqBodyBuilder.setToken(token);

		did = did == null ? "" : did;
		token = token == null ? "" : token;
		info = info == null ? "" : info;
		seq = seq == null ? 0 : seq;
		
		String data = token + did + info + seq + "fdsfeofa";
//		String sign = null;
//		try
//		{
//			sign = Md5.getMD5(data);
//		} catch (Exception e)
//		{
//			log.error(e.getLocalizedMessage(), e);
//			throw new RuntimeException(e);
//		}
//		authReqBodyBuilder.setSign(sign);

		AuthReqBody authReqBody = authReqBodyBuilder.build();
		imReqPacket.setBody(authReqBody.toByteArray());
		return imReqPacket;
	}
	
	

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onBeforeSent(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:41:27
	 * 
	 */
	@Override
	public void onBeforeSent(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet)
	{
		CommandStat.getCount(packet.getCommand()).sent.incrementAndGet();
		JFrameMain.sentPackets.incrementAndGet();
		com.talent.aio.examples.im.client.ui.JFrameMain.updateSentLabel();
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterDecoded(com.talent.aio.common.ChannelContext, com.talent.aio.common.intf.Packet, int)
	 * 
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月20日 上午11:41:27
	 * 
	 */
	@Override
	public void onAfterDecoded(ChannelContext<Object, ImPacket, Object> channelContext, ImPacket packet, int packetSize)
	{
		CommandStat.getCount(packet.getCommand()).received.incrementAndGet();
		com.talent.aio.examples.im.client.ui.JFrameMain.updateReceivedLabel();
		
		
		
		
		
		
		
		
		
		long receivedPacket = JFrameMain.receivedPackets.incrementAndGet();
		long sentPacket = JFrameMain.sentPackets.get();
		
		long time = SystemTimer.currentTimeMillis();

		JFrameMain frameMain = JFrameMain.getInstance();
		if (receivedPacket == 1 || receivedPacket == 0 || receivedPacket % 100000 == 0)
		{
			long sendStartTime = frameMain.getSendStartTime();
			long in = time - sendStartTime;
			if (in <= 0)
			{
				in = 1;
			}

			long initReceivedBytes = frameMain.getStartRecievedBytes();
			long initSentBytes = frameMain.getStartSentBytes();

			long nowReceivedBytes = channelContext.getGroupContext().getGroupStat().getReceivedBytes().get();
			long nowSentBytes = channelContext.getGroupContext().getGroupStat().getSentBytes().get();

			long receivedBytes = nowReceivedBytes - initReceivedBytes;
			long sentBytes = nowSentBytes - initSentBytes;

			double perReceivedPacket = Math.ceil(((double) receivedPacket / (double) in) * (double) 1000);
			double perReceivedBytes = Math.ceil(((double) receivedBytes / (double) in));
			
			double perSentPacket = Math.ceil(((double) sentPacket / (double) in) * (double) 1000);
			double perSentBytes = Math.ceil(((double) sentBytes / (double) in));

			
			NumberFormat numberFormat = NumberFormat.getInstance();
			
			
			//汇总：耗时31毫秒、接收消息100条共10KB，发送消息100条共30KB
			//每秒：接收消息100条共10KB，发送消息100条共30KB
			log.error("<<--------------------\r\n"
					+ "汇总：耗时{}毫秒，接收消息{}条共{}B，发送消息约{}条共{}B \r\n"
					+ "每秒：接收消息{}条共{}B，发送消息约{}条共{}B\r\n"
					+ "接收消息每条平均{}B，发送消息每条平均{}B\r\n"
					+ "注：发送消息的条数用的是约数，但99.9%的情况下打印出来都是精确的，本例中最后打印的那条统计数100%是精确的\r\n"
					+ "-------------------->>", 
					numberFormat.format(in), numberFormat.format(receivedPacket), numberFormat.format(receivedBytes), numberFormat.format(sentPacket), numberFormat.format(sentBytes),
							numberFormat.format(perReceivedPacket), numberFormat.format(perReceivedBytes), numberFormat.format(perSentPacket), numberFormat.format(perSentBytes),
									numberFormat.format(Math.ceil((receivedBytes / receivedPacket))), numberFormat.format(Math.ceil((sentBytes / sentPacket))));
		}
		
	}

	/** 
	 * @see com.talent.aio.common.intf.AioListener#onAfterClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @重写人: tanyaowu
	 * @重写时间: 2017年2月1日 上午11:02:39
	 * 
	 */
	@Override
	public void onAfterClose(ChannelContext<Object, ImPacket, Object> channelContext, Throwable throwable, String remark, boolean isRemove)
	{
		log.info("已经关闭连接:{}", channelContext);

		JFrameMain.updateConnectionCount();
//		JFrameMain jFrameMain = JFrameMain.getInstance();
//		try
//		{
////			synchronized (jFrameMain.getClients())
////			{
//				if (isRemove)
//				{
//					jFrameMain.getListModel().removeElement(channelContext);
//				}
//				jFrameMain.getClients().updateUI();
////			}
//			
//		} catch (Exception e)
//		{
//
//		}
	}

}
