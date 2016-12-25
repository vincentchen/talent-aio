<h2>
  talent-aio: 为大并发即时通讯而生，让即时通讯变得容易。
</h2>

<h3>
  talent-aio是什么？应用场景是什么？性能如何？稳定性如何？上手是否困难？
</h3>
<ol>
	<li>talent-aio是在易用和性能方面超越netty的java即时通讯框架，它的底层采用的是java aio技术</li>
	<li>应用场景：即时通讯场景皆可----实时聊天、TCP长连接(譬如mysql客户端、redis客户端等)、实时监控等</li>
	<li>性能：单机IM场景，可处理<strong>73万条消息/秒</strong>，收发数据量<strong>70M/秒</strong>，可同时稳定维护<strong>10万级以上TCP长连接</strong>(测试环境：i7 4790、8G内存、windows7)</li>
	<li>稳定性：压过两个星期，<strong>7万TCP长连接</strong>，妥妥地没有任何异常(测试环境：服务器i7 4790、8G内存、windows7；客户端为4台虚拟机和一台笔记本)</li>
	<li>上手是否困难：本项目提供了一个helloworld版的例子，如果已有bytebuffer知识基础，<strong>30分钟就可以上手</strong>。本项目还提供了一个用于压测的聊天版的例子，但涉及到了界面、性能测试和统计、类似QQ群聊等功能，所以上手会要一天左右的样子</li>
</ol>


<h3>
  talent-aio产生的背景
</h3>
<ol>
	<li>2011年本人有幸参与了中兴某刀片的网管系统，大领导亲点让我来改造原来的实时通讯模块，因为老系统每管理一个节点就需要两个线程，实测出来的数据是管理100个节点时，就会达到1000多个线程，稳定性和性能极差。在这样的背景下，开始学习nio，改造后的系统线程维持在100个左右，可管理上千个节点，收发速度也是极快（数据忘了），这就是后来talent-nio的雏形</li>
	<li>后来作为热波间(一个直播平台)的平台端架构师，持续优化和封装了talent-nio，使之可以支持4万TCP长连接，每秒可以收发10万条消息</li>
	<li>因为热波间架构师的角色，认识了不少业界朋友，很多朋友要求我开源talent-nio，但是talent-nio在API设计方面不是太好，开源出来无疑个砸牌子的事情</li>
	<li>一翻纠结后，写了talent-aio，线程池部分来源于并优化于talent-nio，其它部分一律重新设计，尤其是锁的优化和API的重新设计，为了折衷花费了大量精力。</li>
</ol>


<h3>
  talent-aio如何快速上手？
</h3>
本项目已经提供了一个helloworld版的例子，虽然有3个maven工程，6个java文件，但是有效代码只有区区100多行，结构清晰极易上手，位于:example\helloworld目录，可按如下步骤上手
<ol>
	<li>先运行parent/install.bat，用来安装本项目所有代码</li>
	<li>运行com.talent.aio.examples.helloworld.server.HelloServerStarter</li>
	<li>运行com.talent.aio.examples.helloworld.client.HelloClientStarter</li>
	<li>顺藤摸瓜，花20分钟仔细阅读这个例子的100多行代码</li>
	<li>恭喜您，你已经顺利上手了，后续遇到问题可以点击<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459">
	<img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术">
  </a>咨询</li>
</ol>


<h3>
  参与talent-aio将得到什么福利
</h3>
<ol>
	<li>java aio的驾驭需要有扎实的多线程基础，并且需要掌握很多多线程技巧，而talent-aio是将多线程技巧运用到极致的框架，所以一旦您参与到本项目，你将会从本项目中学到很多关于多线程的技巧。</li>
	<li>本项目会陆续提供一些业界案例作为例子供大家参考，譬如融云的IM</li>
	<li>本项目会提供一个技术交流群，与大家一起分解工作中遇到的困难</li>
</ol>


<h3>
  获取帮助
</h3>
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


<h3>
  帮助本项目
</h3>
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


<h3>
  用talent-aio实现的一个用于性能测试的群聊系统
  <a 
      target='_blank'
      href='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client-4.png?dir=0&filepath=docs%2Fclient-4.png&oid=5d0af1bd72723d841fa7763e54871f560631e36c&sha=5f44cd4f8356f8ce131b4e087c12b2bb56993e80'>

    (点击本链接可见性能图)
  </a>
</h3>

<ol>
	<li>
	先运行parent/install.bat，用来安装本项目所有代码
	</li>
	<li>
	运行server examples: com.talent.aio.examples.im.server.ImServerStarter
	</li>
	<li>
	运行client examples: com.talent.aio.examples.im.client.ImClientStarter
	</li>
	<img 
    src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client-4.png?dir=0&filepath=docs%2Fclient-4.png&oid=5d0af1bd72723d841fa7763e54871f560631e36c&sha=5f44cd4f8356f8ce131b4e087c12b2bb56993e80 '>
	</img>
</ol>

