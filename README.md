<h2>talent-aio技术交流群:<span style="color:red">428058412</span><a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术"></a></h2>

<h3>把多线程技巧运用到极致，在易用和性能方面超越netty的TCP长连接框架，单机IM场景，每秒可处理<span style="color:red">100万</span>级消息，可收发数据70M/秒，可同时支持<span style="color:red">10万</span>级TCP长连接。</h3>

<h3>talent-aio的一些测试数据(测试环境：i7 4790、8G内存、windows7)</h3>
<ol>
<li>目前简单测试后，可以支持 **75000个TCP长连接** (这个数值后面会继续增加，因为已有的测试还没达到服务器极限，只是用完了5个客户端的极限)</li>
<li>客户机与服务器是同一台的话，服务器可以接收、处理、发送 **48万条完整的业务消息包/每秒** ，客户端可以同时处理与之对应的数据量(一来一回就是有收发 **96万条消息/秒** 的能力)</li>
</ol>


<h3>
用talent-aio实现的简单的im例子，先启动服务器再启动客户端，然后界面会打印相当性能数据<a target='_blank' href='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client-4.png?dir=0&filepath=docs%2Fclient-4.png&oid=5d0af1bd72723d841fa7763e54871f560631e36c&sha=5f44cd4f8356f8ce131b4e087c12b2bb56993e80'>(点击本链接可见性能图)
</a>
</h3>

<ol>
<li>先运行parent/install.bat，用来安装本项目所有代码</li>
<li>运行server examples: com.talent.aio.examples.im.server.ImServerStarter</li>
<li>运行client examples: com.talent.aio.examples.im.client.ImClientStarter</li>
<img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client-4.png?dir=0&filepath=docs%2Fclient-4.png&oid=5d0af1bd72723d841fa7763e54871f560631e36c&sha=5f44cd4f8356f8ce131b4e087c12b2bb56993e80 '></img>
</ol>

<h3>talent-aio的一些特点</h3>
<ol>
<li>易用：某工作一年，甚至不会maven的小伙子，通过下载代码，阅读提供im例子，完成了本框架的入门，并且使用到项目中。</li>
<li>自带与组绑定、与用户绑定等功能，并且在连接关闭时，自动销毁这些绑定(以避免用户自己忘记销毁带来的内存溢出问题)</li>
<li>API自带发送到指定用户、指定组等功能</li>
<li>代码精简，包含提供的一个im例子，代码行数也不到5000行，非常便于学习。</li>
<li>可以通过本框架学习到非常丰富的多线程知识及运用技巧。 </li>
</ol>

<h3>获取帮助</h3>
<ol>
<li>talent-aio官方交流群:<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术"></a></li>
<li>邮箱:<a href="mailto:tywo45@163.com">tywo45@163.com</a></li>
<li>作者QQ(不能及时回复): <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=33745965&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:33745965:52" alt="点击这里给我发消息" title="点击这里给我发消息"/></a></li>
</ol>

<h3>帮助本项目</h3>
<ol>
<li><a target="_blank" href='https://git.oschina.net/tywo45/talent-aio/issues/new?issue%5Bassignee_id%5D=&issue%5Bmilestone_id%5D='>提交Issue</a></li>
<li><a href='#project-donate-overview'>点击下方的<span class="ui green donate button">捐赠</span>按钮赞助本项目</a></li>
</ol>




