# 大数据面试汇总 🕹️0.2.1

## 有点意思但是暂时不太了解的
1. 埋点数据缺失怎么处理，埋点数据相关的表示如何设计的
2. 零点漂移如何解决
3. Spark和Hive查询中遇到过什么难点
4. 多线程，启动个数如何确定，最多多少（是指java多线程？）  
5. Hive中如何反查哪条sql执行较慢


## 必问基础知识点
### 执行过程
1. MapReduce的执行过程  
2. HDFS的读写流程  
3. Hive的执行过程（如何解析成MapReduce的）  
4. Spark任务的执行过程

### Scala
1. Scala的class和case class在使用层面有什么区别  
2. Scala的Option类型的作用与使用方法  
3. Scala的泛型斜变逆变  
4. Scala的函数柯里化  
5. Scala的隐式函数 
	+ [不是太了解所以查了一下资料](https://www.cnblogs.com/xia520pi/p/8745923.html) 

### Spark
1. 你了解Spark的序列化吗，提供了哪些序列化类  
2. *Spark算子分类，常用算子，怎么使用的*  **必问**  
3. *工作中如何进行Spark优化* **必问**   
4. Spark Streaming怎么保证精准的消费

### Hive
1. *工作中如何进行Hive优化* **必问**
	+ Hive中有任务跑的时间比较长，怎么优化  
	+ Hive的参数设置用过哪些，有什么作用  
	+ 什么时候会走MapJoin  
	+ 什么情况下会产生Hive小文件，小文件有什么危害  
2. Hive的查询过程，哪些方法可以提高查询效率 **变相的问优化**  
	+ [居然可以百度到](https://blog.csdn.net/weixin_45695430/article/details/123791209)  
3. Hive的窗口函数，常用窗口函数，怎么使用  

### MySQL
1. sql的执行计划，执行错误报警机制
2. *如何进行sql调优* **必问**  
	+ 一条sql语句，多个字段，如何考虑尽量提升效率  

### Linux
1. 如何查看内存，CPU占用

### Java
1. Java的io流分类  
	+ [数据流向：输入流/输出流；数据类型：字节流/字符流](https://blog.csdn.net/m0_61961937/article/details/123957167)  
2. Java怎么写事务  
	+ [connection.setAutoCommit(false)](https://www.cnblogs.com/Oh-mydream/p/15768834.html)  
3. MyBatis怎么使用事务
	+ [使用TransactionFactory和Transaction接口](https://blog.csdn.net/qq_45756124/article/details/121248797)  
	+ [只要是对数据库进行任何update操作（update、delete、insert），都一定是在事务中进行的](https://blog.csdn.net/qq_32907195/article/details/107378480)

### 常用组件
1. 项目Hbase的RowKey是如何设计的  
2. Flume的源头数据  
3. Sqoop的参数介绍  
4. Redis支持的数据类型

### Flink
1. Flink-cdc
2. Flink和Spark的区别

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

## 涉及到项目和数仓的问题
1. 数仓的`整体架构`是什么样的  
2. 数仓中的`增量表`和`全量表`分别是怎么做的  
3. 介绍一下项目中的`数据流向`  
4. 介绍一下项目中`处理后的数据用途`  
5. 项目中的数仓是`如何分层`  
6. 有没有遇到过`断点续传`的问题，怎么处理的  
7. `Java框架`在项目中的应用  
8. 项目中遇到的`业务数据清洗`是怎么做的  
9. 工作中有没有遇到的`数据倾斜`，如何处理的  
10. 你的项目中`数据量，日活，漏斗分析`，以及其他分析的方向  
11. 你的`数据建模经验`介绍一下  
12. 数仓中负责过哪些`指标`，怎么编写的，有多少个