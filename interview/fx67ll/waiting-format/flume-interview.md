# Sqoop面试宝典 🕹️0.1.0

## 高频面试题
### Flume组成，Put事务，Take事务
#### taildir source
1. 断点续传、多目录
	+ 其本质就是记录下每一次消费的位置，把消费信息的位置保存到文件中，后续程序挂掉了再重启的时候，可以接着上一次消费的数据位置继续拉取
	+ 目录配置，实现不了断点续传功能	
	+ 针对采集原始文件实现断点续传（taildir source才有这个断点续传的功能），可以采用多文件进行配置，也可以采用文件通配符进行配置要采集的文件（比如：*.log）
	```
	# source 类型---->taildir
	vim taildir.conf
	
	a1.channels = ch1
	a1.sources = s1
	a1.sinks = hdfs-sink1
	
	# channel
	a1.channels.ch1.type = memory
	a1.channels.ch1.capacity=10000
	a1.channels.ch1.transactionCapacity=500
	
	# source
	a1.sources.s1.channels = ch1
	# 监控一个目录下的多个文件新增的内容
	a1.sources.s1.type = taildir
	# 通过 json 格式存下每个文件消费的偏移量，避免从头消费
	a1.sources.s1.positionFile = /opt/bigdata/flume/index/taildir_position.json
	a1.sources.s1.filegroups = f1 f2 f3 
	a1.sources.s1.filegroups.f1 = /home/hadoop/taillogs/access.log
	a1.sources.s1.filegroups.f2 = /home/hadoop/taillogs/nginx.log
	a1.sources.s1.filegroups.f3 = /home/hadoop/taillogs/web.log
	a1.sources.s1.headers.f1.headerKey = access
	a1.sources.s1.headers.f2.headerKey = nginx
	a1.sources.s1.headers.f3.headerKey = web
	a1.sources.s1.fileHeader  = true
	
	# sink
	a1.sinks.hdfs-sink1.channel = ch1
	a1.sinks.hdfs-sink1.type = hdfs
	a1.sinks.hdfs-sink1.hdfs.path =hdfs://node1:9000/demo/data/%{headerKey}
	a1.sinks.hdfs-sink1.hdfs.filePrefix = event_data
	a1.sinks.hdfs-sink1.hdfs.fileSuffix = .log
	a1.sinks.hdfs-sink1.hdfs.rollSize = 1048576
	a1.sinks.hdfs-sink1.hdfs.rollInterval =20
	a1.sinks.hdfs-sink1.hdfs.rollCount = 10
	a1.sinks.hdfs-sink1.hdfs.batchSize = 1500
	a1.sinks.hdfs-sink1.hdfs.round = true
	a1.sinks.hdfs-sink1.hdfs.roundUnit = minute
	a1.sinks.hdfs-sink1.hdfs.threadsPoolSize = 25
	a1.sinks.hdfs-sink1.hdfs.fileType =DataStream
	a1.sinks.hdfs-sink1.hdfs.writeFormat = Text
	a1.sinks.hdfs-sink1.hdfs.callTimeout = 60000
	```
2. 哪个Flume版本产生的？
	+ Apache1.7、CDH1.6
3. 没有断点续传功能时怎么做的？
	+ 自定义
4. taildir挂了怎么办？
	+ 不会丢数：断点续传
5. 怎么处理重复数据？
	+ 不处理：生产环境通常不处理，出现重复的概率比较低。处理会影响传输效率。
	+ 处理  
		> 自身：在taildirsource里面增加自定义事务，影响效率  
		> 找兄弟：下一级处理（hive dwd sparkstreaming flink布隆）、去重手段（groupby、开窗取窗口第一条、redis）  
6. taildir source 是否支持递归遍历文件夹读取文件？
	+ 不支持。自定义递归遍历文件夹 + 读取文件
#### file channel /memory channel/kafka channel  
1. File Channel
数据存储于磁盘，优势：可靠性高；劣势：传输速度低
默认容量：100万event
注意：FileChannel可以通过配置dataDirs指向多个路径，每个路径对应不同的硬盘，增大Flume吞吐量。
2. Memory Channel
数据存储于内存，优势：传输速度快；劣势：可靠性差
默认容量：100个event
3. Kafka Channel
数据存储于Kafka，基于磁盘；
优势：可靠性高；
传输速度快 Kafka Channel 大于 Memory Channel + Kafka Sink 原因省去了Sink阶段
4. Kafka Channel哪个版本产生的？
Flume1.6 版本产生 =》并没有火 =》因为有bug
```
topic-start 数据内容
topic-event 数据内容    
ture和false 很遗憾，都不起作用
增加了额外清洗的工作量
Flume1.7解决了这个问题，开始火了
```
5. 生产环境如何选择
如果下一级是Kafka，优先选择`Kafka Channel`
如果是金融、对钱要求准确的公司，选择`File Channel`
如果就是普通的日志，通常可以选择`Memory Channel`
*每天丢几百万数据   pb级   亿万富翁，掉1块钱会捡？*
#### HDFS sink
时间（1小时-2小时）、大小128m 、event个数（0禁止）  
具体参数：`hdfs.rollInterval=3600`，`hdfs.rollSize=134217728`，`hdfs.rollCount=0`  
#### 事务
Source到Channel是Put事务
Channel到Sink是Take事务

### flume拦截器
#### 拦截器注意事项
项目中自定义了：ETL拦截器。
采用两个拦截器的优缺点：优点，模块化开发和可移植性；缺点，性能会低一些
#### 自定义拦截器步骤
1. 实现 Interceptor
2. 重写四个方法 
	+ initialize 初始化
	+ public Event intercept(Event event) 处理单个Event
	+ public List<Event> intercept(List<Event> events) 处理多个Event，在这个方法中调用Event intercept(Event event)
	+ close方法
3. 静态内部类，实现Interceptor.Builder
#### 拦截器可以不用吗？
可以不用；需要在下一级hive的dwd层和sparkstreaming里面处理
优势：只处理一次，轻度处理；劣势：影响性能，不适合做实时推荐这种对实时要求比较高的场景。

### Flume Channel选择器
Replicating：默认选择器。功能：将数据发往下一级所有通道
Multiplexing：选择性发往指定通道。

### Flume监控器
1. 采用Ganglia监控器，监控到Flume尝试提交的次数远远大于最终成功的次数，说明Flume运行比较差。
2. 解决办法？
	+ 自身：增加内存`flume-env.sh 4-6g`，`-Xmx与-Xms`最好设置一致，减少内存抖动带来的性能影响，如果设置不一致容易导致频繁`full gc`。
	+ 找朋友：增加服务器台数，搞活动 618 =》增加服务器=》用完再退出
3. 日志服务器配置：8-16g内存、磁盘8T

### Flume采集数据会丢失吗?（防止数据丢失的机制）
如果是FileChannel不会，Channel存储可以存储在File中，数据传输自身有事务。
如果是MemoryChannel有可能丢。