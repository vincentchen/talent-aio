/**
 * **************************************************************************
 *
 * @说明: 
 * @项目名称: talent-aio-common
 *
 * @author: tanyaowu 
 * @创建时间: 2016年11月15日 上午11:24:17
 *
 * **************************************************************************
 */
package com.talent.aio.common;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 
 * 
 * @author tanyaowu 
 * @创建时间 2016年11月15日 上午11:25:07
 *
 * @操作列表
 *  编号	| 操作时间	| 操作人员	 | 操作说明
 *  (1) | 2016年11月15日 | tanyaowu | 新建类
 *
 */
public class RemoteNode implements Comparable<RemoteNode>
{
	private String ip;
	private int port;

	public RemoteNode(String ip, int port)
	{
		super();

		this.setIp(ip);
		this.setPort(port);
	}

	@Override
	public int hashCode()
	{
		return (ip + ":" + port).hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		RemoteNode other = (RemoteNode) obj;
		return ip.equals(other.getIp()) && port == other.getPort();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(ip).append(":").append(port);
		return builder.toString();
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException
	{
		RemoteNode remoteNode1 = new RemoteNode("127.1.1.1", 80);
		RemoteNode remoteNode2 = new RemoteNode("127.1.1.1", 80);

		java.util.concurrent.ConcurrentSkipListSet<RemoteNode> set = new ConcurrentSkipListSet<>();
		boolean b1 = set.add(remoteNode1);
		boolean b2 = set.add(remoteNode2);
		System.out.println(b1 + "---" + b2);

		java.lang.Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				// 在退出JVM时要做的事
				System.out.println("在退出JVM时要做的事");

			}
		});
		System.exit(0);
		Thread.sleep(1000000);

	}

	@Override
	public int compareTo(RemoteNode other)
	{
		if (other == null)
		{
			return -1;
		}
//		RemoteNode other = (RemoteNode) obj;

		if (ip.equals(other.getIp()) && port == other.getPort())
		{
			return 0;
		} else
		{
			return this.toString().compareTo(other.toString());
		}
	}

}
