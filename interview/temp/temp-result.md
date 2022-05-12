# 大数据面试汇总 🕹️0.3.3

总结一下自己这些年在大数据面试过程中所遇到的各类问题，仅供参考  

## 有点意思但是暂时不太了解的
1. 埋点数据缺失怎么处理，埋点数据相关的表示如何设计的？  
2. 零点漂移如何解决？  
3. Spark和Hive查询中遇到过什么难点？  
4. 多线程，线程启动个数如何确定？最多多少？（是指java多线程？）  
5. Hive中如何反查哪条sql执行较慢？  
6. 有做过用户路径模型吗？每条路径的转化率是多少？（是不是类似页面单跳率那种）  
7. 你们的OLAP引擎主要用的什么？为什么这样选型？  
8. 能聊聊`端到端的一致性`和`精准一次消费`吗？  


## 必问基础知识点
### 执行过程
1. MapReduce的执行过程，如何进行一个文件的计算，shuffle的过程（不需要手写mapreduce或者用java写mapreduce）  
2. HDFS的读写流程  
3. Yarn的任务提交流程，如何查看任务日志和报错信息  
4. Hive的执行过程（如何解析成MapReduce的）  
5. Spark任务的执行过程  

### 工作方向
1. Spark和Hive查询在工作中遇到什么样的难点？如何解决？  
2. 大数据集群用的什么集群？CDH吗？
	+ [大数据学习（一）CDH大数据平台](https://zhuanlan.zhihu.com/p/335663852)    

### MySQL
1. sql的执行计划，执行错误报警机制
2. *如何进行sql调优* **必问**  
	+ 一条sql语句，多个字段，如何考虑尽量提升效率  

### Hive
1. *工作中如何进行Hive优化* **必问**
	+ Hive中有任务跑的时间比较长，怎么优化  
	+ Hive任务处理过程中遇到过什么样的问题
	+ Hive SQL 查询比较慢，怎么处理  
	+ Hive的参数设置用过哪些，有什么作用  
	+ Hive中表关联方式join的分类、用法、应用场景  
	+ 什么时候会走MapJoin  
	+ 什么情况下会产生Hive小文件，小文件有什么危害  
	+ [Hive在工作中的调优总结](https://fx67ll.xyz/archives/hive-optimize-inwork)  
2. Hive的查询过程，哪些方法可以提高查询效率 **变相的问优化**  
	+ [居然可以百度到](https://blog.csdn.net/weixin_45695430/article/details/123791209)  
3. Hive的窗口函数，常用窗口函数，怎么使用  
	+ 窗口函数`row_number`、`rank`、`dense_rank`之间的区别  
		> row_number是行号，不会重复  
		> rank数据相同的，给出并列排名，但是会跳跃  
		> dense_rank类似于rank，但不会跳跃  
		> [hive窗口函数之排名函数row_number、rank和dense_rank](https://blog.csdn.net/jianai858/article/details/118490540)  
	+ 如何用窗口函数去重  
4. Hive SQL的数据去重方式  
	+ distinct、groupby、row_number  
	+ [hive中三种去重的方法](https://wenku.baidu.com/view/c2be4d6b757f5acfa1c7aa00b52acfc789eb9f9f.html)  
5. 项目中Hive有多少表  
6. Hive查询组件Impala了解吗？简单介绍下  
	+ [Impala入门操作](https://blog.csdn.net/qq_24852439/article/details/121686496)  
7. Hive中的行列转换，除了case when，还有别的方法吗
	+ 行转列：`collect_set()`、`collect_list()`、`concat_ws()`  
	+ 列转行：`explode()`、`split()`、`LATERAL VIEW`  
	+ [Hive sql 行列转换(行转列，列转行)](https://blog.csdn.net/weixin_42679482/article/details/120422007)  
8. Hive中内部表和外部表的区别？内部表和外部表如果删除了元数据是由谁来维护？元数据的存储位置和管理者是谁  
	+ 从创建表和删除表两个方面说明  
	+ metastore，master节点上  
	+ mysql，metastore  
9. Hive分区和分桶的区别？分桶的原理？分区可以提高查询效率吗？分区越多越好吗
	+ MR中：按照key的hash值去模除以reductTask的个数  
    + Hive中：按照分桶字段的hash值去模除以分桶的个数  
	+ 缩小数据查询范围，提高查询效率，但是不是分区越多越好  
	+ Hive中如果有过多的分区，由于底层是存储在HDFS上，HDFS上只用于存储大文件而非小文件，因为过多的分区会增加NameNode的负担  
	+ Hive SQL会转化为MapReduce， MapReduce会转化为多个task，过多小文件的话，每个文件一个task，每个taskー个JVM实例，JVM的开启与销毀会降低系统效率  
	+ 合理的分区不应该有过多的分区和文件目录，并且每个目录下的文件应该足够大  
	+ [Hive中分区是否越多越好？](https://blog.csdn.net/Shockang/article/details/118074010)  

### Hbase
1. Hbase查询用的多吗，有没有做过优化？  
	+ [Hbase快速入门（超精炼总结）](https://zhuanlan.zhihu.com/p/124928432)  
	+ [http://www.scicat.cn/aa/20211111/852338.html](http://www.scicat.cn/aa/20211111/852338.html)  
	+ [hbase常用查询命令](http://www.gitweixin.com/?p=686)  
	+ [Hbase查询性能优化](https://blog.csdn.net/lzxlfly/article/details/82314924)  
	+ 大多数key-value数据库是为了高频写入而设计的，而不是为了高速读取！用来做高性能查询完全是个方向性错误  
2. Hbase查询过滤器用过吗，简单介绍下
	+ RowFilter、FamilyFilter、QualifierFilter、ValueFilter  
	+ SingleColumnValueFilter、SingleColumnValueExcludeFilter、PrefixFilter、PageFilter  
	+ 多过滤器综合查询FilterList
	+ [HBase过滤器查询](https://blog.csdn.net/weixin_43230682/article/details/108169525)  
3. 用户画像的构建是Hbase做的吗？特征值怎么提取的？

### Scala
1. Scala有什么特性
2. Scala的class和case class在使用层面有什么区别  
3. Scala的Option类型的作用与使用方法  
4. Scala的泛型斜变逆变  
5. Scala的函数柯里化了解吗？优点是什么？  
6. Scala的隐式函数、隐式转换 
	+ [不是太了解所以查了一下资料](https://www.cnblogs.com/xia520pi/p/8745923.html) 
7. Scala用过哪些函数
	+ [Scala常用函数](https://blog.csdn.net/qq_47932841/article/details/108718413)  
	+ [scala常用函数大全](https://blog.csdn.net/qq_41704237/article/details/107750808)  
8. Scala中的String是可变的吗？  
	+ String是一个不可变的对象，所以该对象不可被修改，这就意味着你如果修改字符串就会产生一个新的字符串对象，但其他对象，如数组就是可变的对象  
	+ String对象是不可变的，如果你需要创建一个可以修改的字符串，可以使用`String Builder`类  
	+ [Scala:字符串](https://blog.csdn.net/pipisorry/article/details/52902348)  
9. Scala是否可以多继承  
	+ Scala中的多重继承由特质（trait）实现并遵循线性化规则  
	+ 在多重继承中，如果一个特质已经显式扩展了一个类，则混入该特质的类必须是之前特质混入的类的子类  
	+ 这意味着当混入一个已扩展了别的类的特质时，他们必须拥有相同的父类  

### Spark
1. 你了解Spark的序列化吗？Spark提供了哪些序列化类？  
2. Spark中RDD持久化了解过吗？  
3. *Spark算子分类？常用算子？怎么使用的？*  **必问**  
4. Spark的惰性计算机制了解过吗？（懒加载）  
	+ [Spark的惰性求值](https://www.jianshu.com/p/2887fc61ad46)  
4. *工作中如何进行Spark优化？* **必问**   
5. Spark的Stage和Task的划分？Task数目由什么决定？  
6. Spark的宽窄依赖了解过吗？  
7. Spark如何查看日志和排查报错问题？  
8. Spark Streaming怎么保证精准的消费？  

### Kafka  
1. 简单介绍下kafka的核心概念及个人理解  
2. Kafka在数据传输过程中遇到重复数据怎么处理  
3. Kafka如何保证数据不丢失  

### Redis

### Linux
1. 如何查看内存，CPU占用  
2. Shell脚本如何定义函数调用
3. Shell脚本第一行：`#!/bin/bash`的含义  
	+ 第一行的内容指定了shell脚本解释器的路径，而且这个指定路径只能放在文件的第一行  
	+ 第一行写错或者不写时，系统会有一个默认的解释器进行解释  
4. Linux脚本授权是什么？Linux授权命令是什么？  
	+ `chmod [{ugoa}{+-=}{rwx}] [文件或目录]`  
	+ `chmod [mode=421] [文件或目录]`  
5. Shell标准输出和标准错误是什么？  

### Java
1. Java的io流分类  
	+ [数据流向：输入流/输出流；数据类型：字节流/字符流](https://blog.csdn.net/m0_61961937/article/details/123957167)  
2. Java怎么写事务  
	+ [connection.setAutoCommit(false)](https://www.cnblogs.com/Oh-mydream/p/15768834.html)  
3. MyBatis怎么使用事务
	+ [使用TransactionFactory和Transaction接口](https://blog.csdn.net/qq_45756124/article/details/121248797)  
	+ [只要是对数据库进行任何update操作（update、delete、insert），都一定是在事务中进行的](https://blog.csdn.net/qq_32907195/article/details/107378480)
4. Java的内部类和外部类  
	+ 在Java中，可以将一个类定义在另一个类里面或者一个方法里面，这样的类称为内部类  
	+ 内部类一般来说包括这四种：成员内部类、局部内部类、匿名内部类和静态内部类  
	+ 静态成员内部类：使用static修饰类  
	+ 非静态成员内部类：未用static修饰类，在没有说明是静态成员内部类时，默认成员内部类指的就是非静态成员内部类  
5. Java中全局变量、静态全局变量、静态局部变量和局部变量的区别  
6. Java中重写与重载之间的区别  
7. Java中的final关键字  
	
### 常用组件
1. 项目Hbase的RowKey是如何设计的  
2. Flume的源头数据  
3. Sqoop的参数介绍  

### Flink
1. Flink-cdc介绍一下
2. Flink和Spark的区别是什么？  
3. 你们之前使用Spark做实时，后来为什么使用Flink了？  
4. Flink的windowapi的分类介绍一下  
5. Flink的cep了解么？怎么使用的？  
6. Flink的水位线了解吗？可以具体讲讲吗？  
7. 你们Flink主要使用api开发还是sql开发？  
8. 能讲讲Flink双流join是如何实现的吗？  

### clickhouse
1. 你们有有过clickhouse做join吗？如何保证秒级延迟？  


## 涉及到项目和数仓的问题
1. 简单介绍一下你们项目中的业务吧  
2. 能结合业务说说你们的数仓怎么搭建的吗？你负责哪些模块？  
3. 你们项目中的人员怎么分配的？  
4. 你们的`主题`是根据什么来划分，为什么这么划分？  
5. 你们源数据大概多少张表？  
6. 你们的日志数据到建立`事实表`的过程中，主要做了什么？  
7. 你们的`维度层`是怎么建设的  
8. *如果在解析日志文件时遇到很多的硬编码，如何使用维度去解决？*  
9. 你们的项目中有多少个`指标`？负责过多少个？怎么编写的？  
10. *如果指标出现同义不同名的情况如何解决？*  
11. *阿里的`oneData体系`有了解吗？你们如何保证数据质量的？你日常遇到最多的数据质量问题是什么？*  
12. 你的项目中`离线任务`有多少个？任务执行的时间是什么时候？  
13. 你的项目中`数据量，日活，漏斗分析`大概是多少？以及其他分析的方向？  
14. 你们项目中的`业务数据清洗`是怎么做的？  
15. 数仓的`整体架构`是什么样的？数仓是`如何分层`的？  
16. 介绍一下项目中的`数据流向` **[可以参考这种用户画像数据流动](https://www.bilibili.com/video/BV1ED4y1R7qM?p=2)**  
17. 介绍一下项目中`处理后的数据用途`？  
18. 数仓中的`增量表`和`全量表`分别是怎么做的？有没有用过`拉链表`？  
19. 工作中有没有遇到的`数据倾斜`？如何处理的？  
20. 工作中有没有遇到过`断点续传`的问题？怎么处理的 **这个问题具体技术具体处理**  
21. 你的`数据建模经验`介绍一下？  
22. `Java框架`在你们项目中的应用？  
23. 你在*实时开发*的过程中遇到什么问题？如何解决的？  
24. 你们的*实时模型*是如何进行优化的？怎么评估它是否是一个优质的模型？  
25. 你们的`任务监控`有做过吗？主要监控什么？  
26. 你们如何保证`数据的准确性`？  


## 工作内容的问题
1. 你们的需求周期一般是多久？拿到需求之后怎么分析？
2. 在完成需求的过程中，有没有考虑过数仓的通用性，你们是如何体现的？  
3. 你在工作中的最大收获是什么，带给你什么样的能力？ 
4. 你们的工作强度怎么样，能接受加班吗？ 
5. 如果一周内让你做十个紧急的需求，你会怎么办？ 
6. 你们部门之间是直接进行对接吗？  
7. 有没有反驳过产品提出的需求，如果不合理怎么办？  


## 关于sql题的汇总
1. 找出连续三天及三天以上都有营业额，且营业额都在上涨的店铺  
```
# 输出每天营业额，按时间倒序  
# 然后侧视图排序1.2.3.4 取出小于等于3的数据  
# 这个的结果出来后行专列 
# 生成店铺名name，第一天额度day1，第二天额度day2，第三天额度day3 等四列  
# 然后where条件day3 > day2 > day1  
```
2. 姓名、学历、分数，统计总分前六名  
3. 找出两表中都有的ID


## 各种组件需要学到什么样的程度  
### hadoop学习到什么样的程度  
1. hdfs、mapreduce、yarn 基本原理即可  
2. 各种面试基本题完成即可
	+ MapReduce的执行过程，如何进行一个文件的计算，shuffle的过程（不需要手写mapreduce或者用java写mapreduce）  
	+ HDFS的读写流程  
	+ Yarn的任务提交流程，如何查看任务日志和报错信息  
3. 大多数公司可能都进入spark阶段了  

### spark学习到什么样的程度  
1. 主要为了优化mapreduce2的一些问题，利用内存计算，减少磁盘IO  
2. spark streaming + hbase 离线实时 -> 使用flink搭建一整套实时  
3. 熟悉spark开发的api和常用的算子  
4. 熟悉spark调优  
5. spark的应用：批处理，流处理流计算（flink和sparkstreaming），数据分析，图形计算  

### spark面试重点
1. 一些八股文知识点，基本原理，是必问且必须熟练知晓的，核心原理的流程和图  
2. spark常见报错的解决方案
	+ real spark冲突怎么解决  
	+ om怎么处理，driver的om和excute的om分别怎么处理  
3. 参数调优，集群层面调优，应用层面调优  
	+ 为什么这么做  
	+ 有没有更好的方案  
4. sparksql调优  
	+ 调优场景  
	+ 怎么发现问题的  
	+ 发现问题后怎么定位问题的  
	+ 定位问题后怎么解决的  
	+ 解决之后达到什么样的一个效果  
	+ 还有哪些更好的一些优化方案吗  
5. 根据业务场景现场写sql，根据数仓的理论优化sql  
	+ 常规的业务逻辑，join，for函数，截取字符串，嵌套多个高级函数  
6. 企业开发使用场景的解决方案  
	+ udf管理：统一仓库管理还是自己提交，怎么解决相同含义的sql function被重复的udf提交  
	+ thift server怎么用的，怎么用的，怎么解决多租户，怎么解决权限，怎么解决负载均衡  
7. spark平台和二次开发，源码，遇到的问题详细问  
8. 面试流程相关
	+ 10-20分钟问项目  
	+ 20分钟问编程基础  
	+ 20分钟问组件
	+ spark问20分钟左右

### spark畅谈
1. 小公司大部分业务场景是离线开发，实时场景也较少  
2. 大公司80%离线，20%实时  
3. sparkstreaming批处理比flink批处理更快，性能差百分之五十左右，spark使用门槛较低  
4. 入职后可以想老员工讨教经验，核心组件  
5. 博客快速浏览一些内容  
6. 官网提供的一些例子  

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/temp/temp-result.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***