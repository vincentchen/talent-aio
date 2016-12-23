 <h1>**#talent-aio**  **（官方交流群：428058412）** </h1>
把多线程技巧运用到极致，与netty在性能和易用性上进行正面PK的java aio框架，**每秒可发100万级消息，单机可支持100K级TCP长连接。** 

 **talent-aio的一些测试数据(测试环境：i7 4790、8G内存、windows7)：** 
1、用talent-aio-server实现的tcp长连接服务器，目前简单测试后，可以支持 **75000个TCP长连接** (这个数值后面会继续增加，因为已有的测试还没达到服务器极限，只是用完了5个客户端的极限)
2、用talent-aio-server实现的tcp长连接服务器，客户机与服务器是同一台的话，服务器可以接收、处理、发送 **48万条完整的业务消息包/每秒** ，客户端可以同时处理与之对应的数据量(一来一回就是有收发 **96万条消息/秒** 的能力)

 **用talent-aio实现的简单的im例子(先启动服务器再启动客户端，然后控制台会打印相当性能数据，使用方法见客户端界面):** 
1、先运行parent/install.bat，用来安装本项目所有代码
2、运行server examples: com.talent.aio.examples.im.server.ImServerStarter
3、运行client examples: com.talent.aio.examples.im.client.ImClientStarter
![im调试客户端](https://git.oschina.net/tywo45/talent-aio/raw/master/docs/client-1.png?dir=0&filepath=docs%2Fclient-1.png&oid=050904d7aff73f932c0c58675c301fc20f57275a&sha=4d34c427e8d933feb5f64810c1d2ab06e1603de9 "基于talent-aio的im调试客户端")

 **talent-aio的一些特点：** 
1、易用：某工作一年，甚至不会maven的小伙子，通过下载代码，阅读提供im例子，完成了本框架的入门。
2、自带与组绑定、与用户绑定等功能，并且在连接关闭时，自动销毁这些绑定(以避免用户自己忘记销毁带来的内存溢出问题)
3、API自带发送到指定用户、指定组等功能
4、代码精简，包含提供的一个im例子，代码行数也不到5000行，非常便于学习。
5、可以通过本框架学习到非常丰富的多线程知识及运用技巧。 

