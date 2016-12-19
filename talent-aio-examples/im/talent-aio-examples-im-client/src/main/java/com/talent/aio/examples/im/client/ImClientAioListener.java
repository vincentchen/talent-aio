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

import com.talent.aio.common.ChannelContext;
import com.talent.aio.common.maintain.ClientNodes;
import com.talent.aio.examples.im.client.ui.JFrameMain;
import com.talent.aio.examples.im.common.ImAioListener;
import com.talent.aio.examples.im.common.ImPacket;

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
public class ImClientAioListener extends ImAioListener
{

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

	/** 
	 * @see com.talent.aio.examples.im.common.ImAioListener#onClose(com.talent.aio.common.ChannelContext, java.lang.Throwable, java.lang.String)
	 * 
	 * @param channelContext
	 * @param throwable
	 * @param remark
	 * @重写人: tanyaowu
	 * @重写时间: 2016年12月16日 下午5:52:24
	 * 
	 */
	@Override
	public void onClose(ChannelContext<Object, ImPacket, Object> channelContext, Throwable throwable, String remark)
	{
		super.onClose(channelContext, throwable, remark);

		JFrameMain jFrameMain = JFrameMain.getInstance();
		synchronized (jFrameMain)
		{
			try
			{
				jFrameMain.getListModel().removeElement(ClientNodes.getKey(channelContext));
				jFrameMain.getClients().setModel(jFrameMain.getListModel());
			} catch (Exception e)
			{

			}

			jFrameMain.updateClientCount();
		}

	}

}
