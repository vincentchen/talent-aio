/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-im-client
 *
 * @author: tanyaowu 
 * @创建时间: 2017年2月1日 上午10:31:55
 *
 * **************************************************************************
 */
package com.talent.aio.examples.im.client.ui.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.talent.aio.client.ClientChannelContext;
import com.talent.aio.examples.im.common.ImPacket;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2017年2月1日 上午10:31:55
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2017年2月1日 | tanyaowu | 新建类
 *
 */
public class MyRenderer extends DefaultListCellRenderer
{
	private Color okColor;
	private Color warnColor;

	//#5cb85c OK
	//#f0ad4e warn
	public MyRenderer(Color okColor, Color warnColor)
	{
		this.okColor = okColor;
		this.warnColor = warnColor;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		ClientChannelContext<Object, ImPacket, Object> channelContext = (ClientChannelContext<Object, ImPacket, Object>) value;

		if (channelContext.isClosed())
		{
			setBackground(this.warnColor);
		} else
		{
			setBackground(this.okColor);
		}

		return this;
	}
}
