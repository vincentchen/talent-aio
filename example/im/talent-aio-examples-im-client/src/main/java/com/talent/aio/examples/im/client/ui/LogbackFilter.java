/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.talent.aio.examples.im.client.ui;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

/**
 *
 * @author Administrator
 */
public class LogbackFilter extends LevelFilter
{

	@Override
	public FilterReply decide(ILoggingEvent event)
	{
		int xx = event.getLevel().levelInt;
		int xx1 = Level.ERROR.toInt();
		if (xx <= xx1)
		{
			
			JFrameMain.getInstance().getMsgTextArea().append(event.getFormattedMessage() + System.lineSeparator());

		}
		return FilterReply.ACCEPT;
	}

}
