 **#talent-aio**  **（官方交流群：428058412）** 
与netty在性能和易用性上进行正面PK的java aio框架（官方交流群：428058412）， **让你轻松完成10万级以上的TCP长连接应用。** 

 **talent-aio的一些测试数据(测试环境：i7 4790、8G内存、windows7)：** 
1、用talent-aio-server实现的tcp长连接服务器，目前简单测试后，可以支持 **75000个TCP长连接** (这个数值后面会继续增加，因为已有的测试还没达到服务器极限，只是用完了5个客户端的极限)
2、用talent-aio-server实现的tcp长连接服务器，客户机与服务器是同一台的话，服务器可以接收、处理、发送 **18万条完整的业务消息包/每秒** ，客户端可以同时处理与之对应的数据量

 **talent-aio的一些特点：** 
1、易用：某工作一年，甚至不会maven的小伙子，通过下载代码，阅读提供im例子，完成了本框架的入门。
2、自带与组绑定、与用户绑定等功能，并且在连接关闭时，自动销毁这些绑定(以避免用户自己忘记销毁带来的内存溢出问题)
3、API自带发送到指定用户、指定组等功能
4、代码精简，包含提供的一个im例子，代码行数也不到5000行，非常便于学习。
5、可以通过本框架学习到非常丰富的多线程知识及运用技巧。 

 **talent-aio的一些缺点** 
1、文档，目前可以用例子学习，例子也是很简单的
2、性能细节方面还有些待优化，但由于总体架构包括线程模型很优秀，所以目前的性能已经非常好(以前有个大企业的架构师跟我说 **netty的c10k** ，目前talent-aio已经支持到 **c100k** 了，这不是极限，现在还差个极限数字)

 **用talent-aio实现的简单的im例子(先启动服务器再启动客户端，然后控制台会打印相当性能数据，使用方法见客户端界面):** 
1、先运行parent/install.bat，用来安装本项目所有代码
2、运行server examples: com.talent.aio.examples.im.server.ImServerStarter
3、运行client examples: com.talent.aio.examples.im.client.ImClientStarter
![im调试客户端](https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client.png?dir=0&filepath=docs%2Fclient.png&oid=edf5289619d5b760c67f599cacf6a0744cca656c&sha=a85d874cfb6e8b19f42a6dcd5796ac28484ad73d "在这里输入图片标题")