/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-examples-helloworld-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年12月24日 下午10:38:51
 *
 * **************************************************************************
 */
package com.talent.aio.examples.helloworld.common;

import com.talent.aio.common.intf.Packet;

/**
 * 
 * @author tanyaowu 
 * @创建时间 2016年12月24日 下午10:38:51
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年12月24日 | tanyaowu | 新建类
 *
 */
public class HelloPacket extends Packet
{

	public static final int HEADER_LENGHT = 4;//消息头的长度

	public static final String CHARSET = "utf-8";
	
	private byte[] body;
	
//	private int bodyLen;

	/**
	 * @param args
	 *
	 * @author: tanyaowu
	 * @创建时间:　2016年12月24日 下午10:38:51
	 * 
	 */
	public static void main(String[] args)
	{
		
	}

	/**
	 * @return the body
	 */
	public byte[] getBody()
	{
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body)
	{
		this.body = body;
	}

//	/**
//	 * @return the bodyLen
//	 */
//	public int getBodyLen()
//	{
//		return bodyLen;
//	}
//
//	/**
//	 * @param bodyLen the bodyLen to set
//	 */
//	public void setBodyLen(int bodyLen)
//	{
//		this.bodyLen = bodyLen;
//	}

}
