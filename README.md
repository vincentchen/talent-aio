 **#talent-aio** 
高性能、易用、灵活的java aio框架，可方便用于创建tcp长连接服务器。

 **talent-aio的一些测试数据：** 
1、用talent-aio-server实现的tcp长连接服务器，目前简单测试后，可以支持75000个TCP长连接(这个数值后面会继续增加，因为已有的测试还没达到服务器权限，只是用完了5个客户端的权限)
2、用talent-aio-server实现的tcp长连接服务器，客户机与服务器是同一台的话，服务器可以接收、处理、发送18万条完整的业务消息包/每秒，客户端可以同时处理与之对应的数据量

 **talent-aio的一些特点：** 
1、易用。易到到什么程度，可以参考已经用talent-aio实现的im例子
2、自带与组绑定、与用户绑定等功能，并且在连接关闭时，自动销毁这些绑定(以避免用户自己忘记销毁带来的内存溢出问题)
3、API自带发送到指定用户、指定组等功能

 **talent-aio的一些缺点** 
1、文档，目前可以用例子学习，例子也是很简单的
2、性能细节方面还有些待优化，但由于总体架构包括线程模型很优秀(这是不是有点在自夸？)，所以目前的性能已经非常非常好(以前有个大企业的架构师跟我说netty的c10k，目前talent-aio已经支持到c75k了，后续这个数字肯定还要增加，预计增到了c200k问题不大)

 **用talent-aio实现的简单的im例子:** 
1、运行server examples: com.talent.aio.examples.im.server.ImServerStarter
2、运行client examples: com.talent.aio.examples.im.client.ImClientStarter