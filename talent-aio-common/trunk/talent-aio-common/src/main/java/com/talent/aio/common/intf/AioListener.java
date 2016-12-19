/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月8日 上午11:25:42
 *
 * **************************************************************************
 */
package com.talent.aio.common.intf;

import com.talent.aio.common.ChannelContext;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月8日 上午11:25:42
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月8日 | tanyaowu | 新建类
 *
 */
public interface AioListener <Ext, P extends Packet, R>
{
	/**
	 * 当连接建立时触发的方法，一般用于过滤黑名单
	 * @param channelContext
	 * @return true: 表示这个连接可以连接，false: 表示这个连接需要立即关闭
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月16日 下午3:28:24
	 *
	 */
	boolean onConnected(ChannelContext<Ext, P, R> channelContext);
	
	/**
	 * 消息包发送出去后
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月8日 下午1:14:03
	 *
	 */
	void onAfterSent(ChannelContext<Ext, P, R> channelContext, P packet, int packetSize);
	
	/**
	 * 解码成功后
	 * @param channelContext
	 * @param packet
	 * @param sentSize
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月15日 上午10:22:49
	 *
	 */
	void onAfterDecoded(ChannelContext<Ext, P, R> channelContext, P packet, int packetSize);
	

	/**
	 * 连接关闭时触发本方法，业务层作一些释放资源等操作，记录日志等.
	 *
	 * @param channelContext the channelcontext
	 * @param throwable the throwable 有可能为空
	 * @param remark the remark 有可能为空
	 * @author: tanyaowu
	 * @创建时间:　2016年11月17日 上午9:17:55
	 */
	void onClose(ChannelContext<Ext, P, R> channelContext, Throwable throwable, String remark);
}
