<h3>
  talent-aio: 让天下没有难开发的即时通讯
</h3>

<h4>
  talent-aio官网上线了，地址：<a href="http://www.talent-aio.com/talent-aio" target="_blank">http://www.talent-aio.com/talent-aio</a>，上面有大家期待已久的入门文档。
</h4>

<h4>
  关于talent-aio
</h4>
<ol>
	<li><strong>talent-aio是什么</strong>：talent-aio是基于java aio实现的即时通讯框架，功能类似netty和mina，但极易掌握，不需要各种学习才能入门，只需要<strong>花上半天学习helloworld</strong>就能比较好地掌握并实现一个性能极好的即时应用，并且天生不会有粘包问题；talent-aio性能极好：<strong>可同时支持10万级tcp长连接，彻底甩开当年的c10K问题</strong>；<strong>每秒可收发处理138万以上条消息，每秒收发的消息大小可达137M以上，收发200万条消息，只需要1440毫秒</strong>
	</li>
	<li><strong>应用场景</strong>：即时通讯场景皆可，常见有如下一些场景
		<ul>
			<li>RPC框架</li>
			<li>点对点聊天</li>
			<li>群组聊天</li>
			<li>实时监测</li>
		</ul>
	</li>

	<li><strong>性能：</strong>请参考见下方的测试数据及截图
		<table>
			<tr>
				<td>
				<img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/step/aio-test-step-2.png?dir=0&filepath=docs%2Fstep%2Faio-test-step-2.png&oid=5e03c477f7b9ff9c32b85e93cb520108075fb147&sha=0cf7086630dc611e1922fb0715616ece7f09752e'>
				</td>
			</tr>
			<tr>
				<td>
				<img  src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/performance/client-10.png?dir=0&filepath=docs%2Fperformance%2Fclient-10.png&oid=a6311c568fe363f49834463b3f495304e54e2429&sha=8189c46410a1ce392470188a743ef47968c665d1 '>
				</img>
				</td>
			</tr>
		</table>
	</li>
	<li><strong>如何驾驭本框架</strong>：本项目提供了一个helloworld版的例子(位置见下图)，如果已有bytebuffer知识基础，<strong>30分钟就可以上手使用，两天就能驾驭</strong>。后文有talent-aio快速上手指南，请参考
	<div>
		<img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/docs/example-dir.png?dir=0&filepath=docs%2Fdocs%2Fexample-dir.png&oid=c003e1a2194ec85610f447ce591641b6f26398c8&sha=5213d09a2ebea417b6f0d1bb08b061da294e2f2e'/>
	</div>
	
	</li>
</ol>


<h4>
  talent-aio产生的背景
</h4>
<ol>
	<li>2011年笔者参与了中兴某刀片的网管系统开发，虽然入职才3个月，但大领导还是亲点让笔者来改造原来的实时通讯模块，而且不允许使用mina。在这样的背景下，开始学习nio，改造后的系统，可管理上千个节点，消息收发速度极快，最近有和还在职的中兴同事了解过，核心代码仍然在运行，足见其稳定性，这就是后来talent-nio的雏形</li>
	<li>后来担任热波间(一个直播平台)的平台端架构师，持续优化和封装了talent-nio，使之可以支持4万TCP长连接，每秒可以收发10万条消息，当年甚至扛住了自杀式的2000人同时无限点赞场景</li>
	<li>因为热波间架构师的角色，认识了不少业界朋友，部分朋友表达希望开源talent-nio， 以便参考借鉴，但是talent-nio在易用性方面做得还不是很理想，开源出来的话要么无人问津要么就要消耗大量的咨询时间</li>
	<li>几番考虑之后，写了talent-aio，线程池部分和部分思想来源于并优化于talent-nio，在性能大步提升的基础上，易用性得到根本性解决，其中锁的优化和API的重新设计耗费了不少脑细胞(有类似经历的的朋友知道，有些东西如何取舍很矛盾很纠结)。</li>
</ol>


<h4>
  talent-aio功能和入门简介
</h4>
<ol>
	<li>框架层面已经解决<strong>粘包问题</strong>，所以困扰很多用户的粘包问题将不会再存在</li>
	<li>在框架层面为tcp client提供定时发<strong>心跳功能</strong>，用户只需要提供一个心跳包给框架----实现：com.talent.aio.client.intf.ClientAioHandler.heartbeatPacket()</li>
	<li>可以方便地将链路和<strong>用户绑定</strong>，方便实际业务操作，譬如聊天、权限等</li>
	<li>可以方便地将链路和<strong>群组绑定</strong>，方便实际业务操作，譬如群聊、类似频道需求等</li>
	<li>除了启动性的API，其它开放给使用者的api，几乎都在<strong>com.talent.aio.common.Aio</strong>中，并且以static方法提供，方便使用</li>
	
	<li>启动server请参见com.talent.aio.examples.helloworld.server.HelloServerStarter
		<code>
		<pre>
public class HelloServerStarter
{
	static ServerGroupContext<Object, HelloPacket, Object> serverGroupContext = null;
	static AioServer<Object, HelloPacket, Object> aioServer = null;
	static ServerAioHandler<Object, HelloPacket, Object> aioHandler = null;
	static ServerAioListener<Object, HelloPacket, Object> aioListener = null;
	static String ip = null;
	static int port = com.talent.aio.examples.helloworld.common.Const.PORT;

	public static void main(String[] args) throws IOException
	{
		aioHandler = new HelloServerAioHandler();
		aioListener = null; //可以为空
		serverGroupContext = new ServerGroupContext<>(ip, port, aioHandler, aioListener);
		aioServer = new AioServer<>(serverGroupContext);
		aioServer.start();
	}
}
		</pre>
		</code>
	</li>
	<li>启动client请参见com.talent.aio.examples.helloworld.client.HelloClientStarter
		<code>
		<pre>
public class HelloClientStarter
{
	private static String serverIp = null; //服务器的IP地址
	private static int serverPort = 0; //服务器的PORT
	private static AioClient<Object, HelloPacket, Object> aioClient;
	private static ClientGroupContext<Object, HelloPacket, Object> clientGroupContext = null;
	private static ClientAioHandler<Object, HelloPacket, Object> aioClientHandler = null;
	private static ClientAioListener<Object, HelloPacket, Object> aioListener = null;
	private static ReconnConf<Object, HelloPacket, Object> reconnConf = new ReconnConf<Object, HelloPacket, Object>(5000L);//用来自动连接的，不想自动连接请传null

	public static void main(String[] args) throws Exception
	{
		serverIp = "127.0.0.1";
		serverPort = com.talent.aio.examples.helloworld.common.Const.PORT;
		aioClientHandler = new HelloClientAioHandler();
		aioListener = null;

		clientGroupContext = new ClientGroupContext<>(serverIp, serverPort, aioClientHandler, aioListener, reconnConf);
		aioClient = new AioClient<>(clientGroupContext);

		String bindIp = null;
		int bindPort = 0;
		ClientChannelContext<Object, HelloPacket, Object> clientChannelContext = aioClient.connect(bindIp, bindPort);

		//以下内容不是启动的过程，而是属于发消息的过程
		HelloPacket packet = new HelloPacket();
		packet.setBody("hello world".getBytes(HelloPacket.CHARSET));
		Aio.send(clientChannelContext, packet);
	}
}
		</pre>
		</code>
	</li>
</ol>


<h4>
  talent-aio快速上手指南
</h4>
本项目已经提供了一个helloworld版的例子，虽然有3个maven工程，6个java文件，但是有效代码只有区区100多行，结构清晰极易上手，位于:example\helloworld目录，可按如下步骤上手
<ol>
	<li>先运行install.bat，用来安装本项目所有代码</li>
	<li>运行com.talent.aio.examples.helloworld.server.HelloServerStarter</li>
	<li>运行com.talent.aio.examples.helloworld.client.HelloClientStarter</li>
	<li>顺藤摸瓜，花20分钟仔细阅读这个例子的100多行代码</li>
	<li>恭喜您，你已经顺利上手了</li>
</ol>


<h4>
  参与talent-aio将得到什么福利
</h4>
<ol>
	<li>java aio的驾驭需要有扎实的多线程基础，并且需要掌握很多多线程技巧，而talent-aio是将多线程技巧运用到极致的框架，所以一旦您参与到本项目，你将会从本项目中学到很多关于多线程的技巧。</li>
	<li>本项目会陆续提供一些业界案例作为例子供大家参考，譬如融云的IM</li>
	<li>本项目会提供一个技术交流群，与大家一起分解工作中遇到的困难</li>
</ol>


<h4>
  与项目一起成长
</h4>
<ol>
	<li>
	通过以下方式之一，加入talent-aio技术群
		<ul>
			<li>通过群号加入: 428058412</li>
			<li>点击加入: <a  target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术"></a></li>
			<li>扫码加入<div><img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/qq_group-1.png?dir=0&filepath=docs%2Fqq_group-1.png&oid=312d980621fab8fe7ba790abb1381eda4cfea198&sha=3a23f5d8d7858329a6121c74adccd159bdf88c96'/></div></li>
		</ul>
	</li>
	<li>
	<a 
      href="/tywo45/talent-aio/issues/new?issue%5Bassignee_id%5D=&amp;issue%5Bmilestone_id%5D="
      class="ui mini green button"
      title="提交issue">
    <i class="icon plus"></i>提交Issue
	</a>
	给项目提出有意义的新需求，或是帮项目发现BUG，或是上传你本地测试的一些数据让作者参考以便进一步优化。
	</li>

	<li>
	点击右上方的
	<span class="basic buttons mini star-container ui">
	<a href="javascritp:void(0);" class="ui button star" data-method="post" data-remote="true" rel="nofollow">Star</a>
	</span>
	以便随时掌握本项目的动态
	</li>
</ol>




