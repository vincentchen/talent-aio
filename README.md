<h3>
  talent-aio: 让天下没有难开发的即时通讯
</h3>

<h4>
  关于talent-aio
</h4>
<ol>
	<li><strong>talent-aio是什么</strong>：talent-aio是基于java aio实现的即时通讯框架，功能类似netty和mina但又略有侧重。talent-aio的出现对解决如下几个业界难题，提供了一个可选方案：
		<ul>
			<li>tcp server端能支持的最大tcp连接数</li>
			<li>每秒收发消息及处理消息的能力</li>
		</ul>
	</li>
	<li><strong>常见应用场景</strong>：即时通讯场景皆可，一般可用于以下场景的通讯层框架
		<ul>
			<li>可作RPC框架的通讯层框架</li>
			<li>点对点聊天</li>
			<li>群组聊天</li>
			<li>实时监测</li>
		</ul>
	</li>

	<li><strong>性能：</strong>请参考见下方的测试数据及截图
		<table>
			<tr>
				<td>
				<img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/step/aio-test-step.png?dir=0&filepath=docs%2Fstep%2Faio-test-step.png&oid=2593045570415b26c028b18e53a7e8f01ca0a5f1&sha=ba3bfff3360a1f2083b7c7391f67fd60e2121b3b'>
				</td>
			</tr>
			<tr>
				<td>
				<img  src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/performance/client-8.png?dir=0&filepath=docs%2Fperformance%2Fclient-8.png&oid=0866934f6e636f33b357d907d7e1d20a2fd96f82&sha=cf5bfd7aa595d9698a61f8379c14a7857e8da188 '>
				</img>
				</td>
			</tr>
		
		</table>
	</li>
	
	
	
	
	<li><strong>上手是否困难</strong>：本项目提供了一个helloworld版的例子，如果已有bytebuffer知识基础，<strong>30分钟就可以上手使用</strong></li>
</ol>


<h4>
  talent-aio产生的背景
</h4>
<ol>
	<li>2011年本人有幸参与了中兴某刀片的网管系统，大领导亲点让我来改造原来的实时通讯模块，因为老系统每管理一个节点就需要两个线程，实测出来的数据是管理100个节点时，就会达到1000多个线程，稳定性和性能极差。在这样的背景下，开始学习nio，改造后的系统线程维持在100个左右，可管理上千个节点，消息收发速度极快，最近和中兴同事了解过，核心代码仍然在运行，足见稳定性，这就是后来talent-nio的雏形</li>
	<li>后来作为热波间(一个直播平台)的平台端架构师，持续优化和封装了talent-nio，使之可以支持4万TCP长连接，每秒可以收发10万条消息</li>
	<li>因为热波间架构师的角色，认识了不少业界朋友，很多朋友要求我开源talent-nio，但是talent-nio在API设计方面不是太好，开源出来无疑是个不负责任的行为(浪费观众的时间)</li>
	<li>一翻考虑之后，写了talent-aio，线程池部分来源于并优化于talent-nio，其它部分一律重新设计，尤其是锁的优化和API的重新设计。</li>
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
			static AioServer<Object, HelloPacket, Object> aioServer = null; //可以为空
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

			public static String SERVER_IP = "127.0.0.1"; //服务器的IP地址
			public static int SERVER_PORT = 9321; //服务器的PORT

			public static void main(String[] args) throws Exception
			{
				serverIp = "127.0.0.1";
				serverPort = com.talent.aio.examples.helloworld.common.Const.PORT;
				aioClientHandler = new HelloClientAioHandler();
				aioListener = null;
				clientGroupContext = new ClientGroupContext<>(serverIp, serverPort, aioClientHandler, aioListener);
				aioClient = new AioClient<>(clientGroupContext);

				String bindIp = null;
				int bindPort = 0;
				boolean autoReconnect = false; //暂时不支持自动重连，需要业务自己实现，后续版本会支持此属性为true
				ClientChannelContext<Object, HelloPacket, Object> clientChannelContext = aioClient.connect(bindIp, bindPort, autoReconnect);

				
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
  talent-aio如何快速上手？
</h4>
本项目已经提供了一个helloworld版的例子，虽然有3个maven工程，6个java文件，但是有效代码只有区区100多行，结构清晰极易上手，位于:example\helloworld目录，可按如下步骤上手
<ol>
	<li>先运行install.bat，用来安装本项目所有代码</li>
	<li>运行com.talent.aio.examples.helloworld.server.HelloServerStarter</li>
	<li>运行com.talent.aio.examples.helloworld.client.HelloClientStarter</li>
	<li>顺藤摸瓜，花20分钟仔细阅读这个例子的100多行代码</li>
	<li>恭喜您，你已经顺利上手了，后续遇到问题可以点击<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459">
	<img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术">
  </a>咨询</li>
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
  获取帮助
</h4>
<ol>
	<li>
	talent-aio官方交流群:
	<a  target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459">
    <img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术">
	</a>
	</li>
	<li>邮箱:<a href="mailto:tywo45@163.com">tywo45@163.com</a></li>
	<li>
	作者QQ(不能及时回复):
	<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=33745965&site=qq&menu=yes">
	<img border="0" src="http://wpa.qq.com/pa?p=2:33745965:52" alt="点击这里给我发消息" title="点击这里给我发消息"/>
	</a>
	</li>
</ol>


<h4>
  帮助本项目
</h4>
<ol>
	<li>
	<a 
      href="/tywo45/talent-aio/issues/new?issue%5Bassignee_id%5D=&amp;issue%5Bmilestone_id%5D="
      class="ui mini green button"
      title="提交issue">
    <i class="icon plus"></i>提交Issue
	</a>
	为本项目出谋划策，或是指出项目BUG
	</li>

	<li>
	点击右上方的
	<span class="basic buttons mini star-container ui">
	<a href="javascritp:void(0);" class="ui button star" data-method="post" data-remote="true" rel="nofollow">Star</a>
	</span>
	为本项目积攒人气
	</li>

	<li>
	碰到有需要即时通讯的朋友（譬如IM系统、实时监控系统等），请按如下方式推荐本项目
		<ul>
			<li>
			告知本项目的地址: https://git.oschina.net/tywo45/talent-aio
			</li>
			<li>
			告知本项目的技术交流群: http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459
			</li>
		</ul>
	</li>

	<li>
	如果以上都不想做，又想为开源项目出点力，那么请亲点击下方的
	<a href='#project-donate-overview'>
	<span class="ui green donate button">捐赠</span>
	</a>按钮，为本项目积攒金币
	</li>
</ol>




