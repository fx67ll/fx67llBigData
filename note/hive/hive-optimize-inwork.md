# Hive在工作中的调优总结 🕹️0.2.0

#### 先说一些废话
总结了一下自己在以往工作中，对于`Hive SQL`调优的一些实际应用，是日常积累的一些优化技巧，如有出入，欢迎在评论区留言探讨~

## EXPLAIN 查看执行计划  

## 建表优化
### 分区
1. 分区表基本操作，partitioned   
2. 二级分区  
3. 动态分区  

### 分桶
1. 分桶表基本操作，clustered  
2. 分桶表主要是抽样查询，找出具有代表性的结果  

### 选择合适的文件格式和压缩格式
1. LZO，拉兹罗
2. Snappy  
3. 压缩速度快，压缩比高


## HiveSQL语法优化
### 单表查询优化
1. 列裁剪和分区裁剪，全表和全列扫描效率都很差，`SELECT *`  
2. Group By，`map`阶段会把同一个`key`发给一个`reduce`，当一个`key`过大时就倾斜了，可以开启`map`端预聚合  
	```
	# 是否在map端聚合，默认为true
	set hive.map.aggr = true;
	
	# 在map端聚合的条数
	set hive.groupby.mapaggr.checkintervel = 100000;
	
	# 在数据倾斜的时候进行均衡负载（默认是false），开启后会有 两个mr任务
	# 当选项设定为true时，第一个 mr任务 会将map输出的结果随机分配到reduce，
	# 每个reduce会随机分布到reduce上，这样的处理结果是会使相同的`group by key`分到不同的reduce上。
	# 第二个 mr任务 再根据预处理的结果按`group by key`分到reduce上，保证相同`group by key`的数据分到同一个reduce上。
	# 但是这样能解决数据倾斜，但是不能让运行速度更快  
	# *切记！！！* 在数据量小的时候，开始数据倾斜负载均衡可能反而会导致时间变长  
	set hive.groupby.skewindata = true;
	```
3. Vectorization，矢量计算技术，通过设置批处理的增量大小为1024行单次来达到比单行单次更好的效率  
	```
	# 开启矢量计算  
	set hive.vectorized.execution.enabled = true;
	
	# 在reduce阶段开始矢量计算  
	set hive.vectorized.execution.reduce.enabled = true;
	```
4. 多重模式，一次读取多次插入，同一张表的插入操作优化成先`from table`再`insert`  
5. in/exists或者join用`left semi join`代替（为什么替代扩展一下~）  

### 多表查询优化
6. CBO优化，成本优化器，代价最小的执行计划就是最好的执行计划  
	+ join的时候表的顺序关系，前面的表都会被加载到内存中，后面的表进行磁盘扫描  
	+ 通过`hive.cbo.enable`，自动优化hivesql中多个join的执行顺序  
	+ 可以通过查询一下参数，这些一般都是true，无需修改  
	```
	set hive.cbo.enable = true;
	set hive.compute.query.using.stats = true;
	set hive.stats.fetch.column.stats = true;
	set hive.stats.fetch.partition.stats = true;
	```
7. 谓词下推，将`sql`语句中的`where`谓词逻辑都尽可能提前执行，减少下游处理的数据量，***非常关键的一个优化***  
	```
	# 这个设置是默认开启的，如果关闭了但是cbo开启，那么关闭依然不会生效，因为cbo会自动使用更为高级的优化计划  
	set hive.optimize.pdd = true;
	```
8. Map Join，减少`reduce`，减少`shuffle`，减少开销
	+ `set hive.auto.convert.join = true`，配置开启，默认是true  
	+ *注意！！！* 如果执行小表join大表，小表作为主连接的主表，所有数据都要写出去，此时会走reduce阶段，mapjoin会失效  
	+ 大表join小表不受影响，上一条的原因主要是因为小表join大表map的时候不知道reduce的结果其他reduce是否有，所以必须在最后reduce聚合的时候再处理，就产生了reduce的开销  
	+ mapjoin是指将join操作两方中比较小的表直接分发到各个map进程的内存中，在map中进行join的操作，这样就不用进行reduce的步骤，从而提高速度  
9. Map Join，大表和大表的`MapReduce任务`，`SMB Join`  
	+ 直接join耗时会很长，但是根据某字段分桶后，两个大表每一个桶就是一个小文件，两个表的每个小文件的分桶字段都应该能够一一对应（hash值取模的结果）  
	+ 总结就是分而治之，注意两个大表的分桶字段和数量都应该保持一致
	```
	set hive.optimize.bucketmapjoin = true;
	set hive.optimeize.bucketmapjoin.sortedmerge = true;
	hive.input.format = org.apache.hadoop.hive.ql.io.BucketizedHiveInputFormat;
	```
10. 笛卡尔积，在生产环境中严禁使用  

## 数据倾斜
注意要和数据过量的情况区分开，数据倾斜是大部分任务都已经执行完毕，但是某一个任务或者少数几个任务，一直未能完成，甚至执行失败，
而数据过量，是大部分任务都执行的很慢，这种情况需要通过扩充执行资源的方式来加快速度  

### 单表携带了GroupBy字段的查询  
1. 任务中存在GroupBy操作，同时聚合函数为`count`或`sum`，单个`key`导致的数据倾斜可以这样通过设置开启`map`端预聚合参数的方式来处理  
	```
	# 是否在map端聚合，默认为true
	set hive.map.aggr = true;
	# 在map端聚合的条数
	set hive.groupby.mapaggr.checkintervel = 100000;
	# 有数据倾斜的时候开启负载均衡，这样会生成两个mr任务
	set hive.groupby.skewindata = true;
	```
2. 任务中存在GroupBy操作，同时聚合函数为count或sum，多个key导致的数据倾斜可以通过增加reduce的数量来处理  
	+ 增加分区可以减少不同分区之间的数据量差距，而且增加的分区时候不能是之前分区数量的倍数，不然会导致取模结果相同继续分在相同分区  
	+ 第一种修改方式
	```
	# 每个reduce处理的数量
	set hive.exec.reduce.bytes.per.reducer = 256000000;
	# 每个任务最大的reduce数量
	set hive.exec.reducers.max = 1009;
	# 计算reducer数的公式，根据任务的需要调整每个任务最大的reduce数量  
	N = min（设置的最大数，总数量数/每个reduce处理的数量）
	```
	+ 第二种修改方式
	```
	# 在hadoop的mapred-default.xml文件中修改
	set mapreduce.job.reduces = 15;
	```

### 两表或多表的join查询操作  
1. 设置参数增加`map`数量
	```
	# join的key对应记录条数超过该数量，会进行分拆  
	set hive.skewjoin.key = 1000;
	# 并设置该参数为true，默认是false
	set hive.optimize.skewjoin = true;
	# 上面的参数如果开启了会将计算数量超过阈值的key写进临时文件，再启动另外一个任务做map join  
	# 可以通过设置这个参数，控制第二个任务的mapper数量，默认10000
	set hive.skewjoin.mapjoin.map.tasks = 10000;
	```
2. 使用`mapjoin`，减少`reduce`从根本上解决数据倾斜，参考`HiveSQL语法优化 -> 多表查询优化 ->  Map Join，大表和大表的MapReduce任务，SMB Join`  


## Hive Job 优化  
### Hive Map 优化
#### Map数量多少的影响
1. Map数过大  
	+ `map`阶段输出文件太小，产生大量小文件  
	+ 初始化和创建`map`的开销很大  
2. Map数太小  
	+ 文件处理或查询并发度小，`Job`执行时间过长  
	+ 大量作业时，容易堵塞集群  

#### 控制Map数的原则
根据实际情况，控制`map`数量需要遵循两个原则
1. 第一是使大数据量利用合适的`map`数  
2. 第二是使单个`map`任务处理合适的数据量   

#### 复杂文件适当增加Map数
1. 当`input`的文件都很大，任务逻辑复杂，`map`执行非常慢的时候，可以考虑增加`map`数，来使得每个`map`处理的数据量减少，从而提高任务的执行效率  
2.  那么如何增加`map`的数量呢？在`map`阶段，文件先被切分成`split`块，而后每一个`split`切片对应一个`Mapper任务`，
	`FileInputFormat`这个类先对输入文件进行逻辑上的划分，以`128m`为单位，将原始数据从逻辑上分割成若干个`split`，每个`split`切片对应一个`mapper任务`，
	所以说减少切片的大小就可增加`map`数量  
3. 可以依据公式计算`computeSliteSize(Math.max(minSize, Math.min(maxSize, blockSize))) = blockSize = 128m`  
4. 执行语句：`set mapreduce.input.fileinputformat.split.maxsize = 100;`  

#### 小文件进行合并减少Map数
为什么要进行小文件合并？因为如果一个任务有很多小文件（远远小于块大小128m），则每个小文件也会被当做一个块，用一个`map`任务来完成，
而一个`map`任务启动和初始化的时间远远大于逻辑处理的时间，就会造成很大的资源浪费，同时可执行的`map`数是受限的  
*两种方式合并小文件*
1. 在`Map执行前`合并小文件，减少`map`数量  
	```
	// 每个Map最大输入大小(这个值决定了合并后文件的数量)
	set mapred.max.split.size = 256000000;
	
	// 一个节点上split的至少的大小(这个值决定了多个DataNode上的文件是否需要合并)
	set mapred.min.split.size.per.node = 100000000;
	
	// 一个交换机下split的至少的大小(这个值决定了多个交换机上的文件是否需要合并)
	set mapred.min.split.size.per.rack = 100000000;
	
	// 执行Map前进行小文件合并
	set hive.input.format = org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
	```
2. 在`Map-Reduce任务执行结束时`合并小文件，减少小文件输出  
	```
	// 设置map端输出进行合并，默认为true
	set hive.merge.mapfiles = true;
	
	// 设置reduce端输出进行合并，默认为false
	set hive.merge.mapredfiles = true;
	
	// 设置合并文件的大小，默认是256
	set hive.merge.size.per.task = 256 * 1000 * 1000;
	
	// 当输出文件的平均大小小于该值时，启动一个独立的`MapReduce任务`进行文件`merge`。
	set hive.merge.smallfiles.avgsize = 16000000;
	```

#### Map端预聚合减少Map数量
1. 相当于在`map`端执行`combiner`，执行命令：`set hive.map.aggr = true;`  
2. `combiners`是对`map`端的数据进行适当的聚合，其好处是减少了从`map`端到`reduce`端的数据传输量  
3. 其作用的本质，是将`map`计算的结果进行二次聚合，使`Key-Value<List>`中`List`的数据量变小，从而达到减少数据量的目的  

#### 推测执行
1. 在分布式集群环境下，因为程序Bug（包括Hadoop本身的bug），负载不均衡或者资源分布不均等原因，会造成同一个作业的多个任务之间运行速度不一致，
	有些任务的运行速度可能明显慢于其他任务（比如一个作业的某个任务进度只有50%，而其他所有任务已经运行完毕），则这些任务会拖慢作业的整体执行进度  
2. Hadoop采用了`推测执行（Speculative Execution）`机制，它根据一定的法则推测出拖后腿的任务，并为这样的任务启动一个备份任务，
	让该任务与原始任务同时处理同一份数据，并最终选用最先成功运行完成任务的计算结果作为最终结果  
3. 执行命令：`set mapred.reduce.tasks.speculative.execution = true;  # 默认是true`  
4. 当然，如果用户对于运行时的偏差非常敏感的话，那么可以将这些功能关闭掉，如果用户因为输入数据量很大而需要执行长时间的`map task`或者`reduce task`的话，
	那么启动推测执行造成的浪费是非常巨大的  

#### 合理控制Map数量的实际案例
**假设一个SQL任务：**
```sql
SELECT COUNT(1) 
FROM fx67ll_alarm_count_copy
WHERE alarm_date = "2021-01-08";
```  
该任务的输入目录`inputdir`是：`/group/fx67ll_data/fx67ll_data_etl/date/fx67ll_alarm_count_copy/alarm_date=2021-01-08`，共有194个文件，
其中很多是远远小于`128m`的小文件，总大小约`9G`，正常执行会用`194个Map任务`，`map`总共消耗的计算资源：`SLOTS_MILLIS_MAPS= 610,023`  
*通过在Map执行前合并小文件，减少Map数*
```
# 前面三个参数确定合并文件块的大小
# 大于文件块大小128m的，按照128m来分隔 
# 小于128m,大于100m的，按照100m来分隔
# 把那些小于100m的（包括小文件和分隔大文件剩下的），进行合并，最终生成了74个块
set mapred.max.split.size=100000000;
set mapred.min.split.size.per.node=100000000;
set mapred.min.split.size.per.rack=100000000;
set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
```  
合并后，用了74个`map`任务，`map`消耗的计算资源：`SLOTS_MILLIS_MAPS= 323,098`，对于这个简单SQL任务，执行时间上可能差不多，但节省了一半的计算资源  

**再假设这样一个SQL任务：**
```sql
SELECT data_fx67ll,
COUNT(1),
COUNT(DISTINCT id),
SUM(CASE WHEN …),
SUM(CASE WHEN …),
SUM(…)
FROM fx67ll_device_info_zs
GROUP data_fx67ll
```
如果表`fx67ll_device_info_zs`只有一个文件，大小为`120m`，但包含几千万的记录，如果用1个`map`去完成这个任务，肯定是比较耗时的，
这种情况下，我们要考虑将这一个文件合理的拆分成多个  
*增加Reduce数量，来增加Map数量*
```sql
set mapred.reduce.tasks=10;
CREATE TABLE fx67ll_device_info_zs_temp
AS
SELECT * 
FROM fx67ll_device_info_zs
DISTRIBUTE BY RAND(123);
```
这样会将`fx67ll_device_info_zs`表的记录，随机的分散到包含10个文件的`fx67ll_device_info_zs_temp`表中，
再用`fx67ll_device_info_zs_temp`代替上面`sql`中的`fx67ll_device_info_zs`表，
则会用10个`map`任务去完成，每个`map`任务处理大于`12m（几百万记录）`的数据，效率肯定会好很多  

### Hive Reduce 优化
#### Reduce数量多少的影响  
1. 同`map`一样，启动和初始化`reduce`也会消耗时间和资源  
2. 另外，有多少个`reduce`，就会有多少个输出文件，如果生成了很多个小文件，那么如果这些小文件作为下一个任务的输入，则也会出现小文件过多的问题  

#### 控制Reduce数的原则
和`map`一样，控制`reduce`数量需要遵循两个原则
1. 第一是使大数据量利用合适的`reduce`数  
2. 第二是使单个`reduce`任务处理合适的数据量   

#### Hive自己如何确定Reduce数  
`reduce`个数的设定极大影响任务执行效率，不指定`reduce`个数的情况下，Hive会猜测确定一个`reduce`个数，基于以下两个设定：
```
# 每个reduce任务处理的数据量，默认为 1000^3=1G
hive.exec.reducers.bytes.per.reducer

# 每个任务最大的reduce数，默认为999
hive.exec.reducers.max
```
计算`reducer`数的公式很简单`N = min(参数2，总输入数据量 / 参数1)`  
即，如果`reduce`的输入（`map`的输出）总大小不超过1G，那么只会有一个`reduce`任务  

举个例子：
```sql
SELECT alarm_date,
		COUNT(1) 
FROM fx67ll_alarm_count_copy
WHERE alarm_date = "2021-01-08"
GROUP BY alarm_date;
```  
该任务的输入目录`inputdir`是：`/group/fx67ll_data/fx67ll_data_etl/date/fx67ll_alarm_count_copy/alarm_date=2021-01-08`，
总大小为`9G`多，因此这句有10个`reduce`  

#### 如何调整Reduce数量
1. 通过调整每个reduce任务处理的数据量来调整reduce个数，处理的数据量少了，任务数就多了   
	```sql
	# 设置每个reduce任务处理的数据量500M，默认是1G
	set hive.exec.reducers.bytes.per.reducer = 500000000;

	SELECT alarm_date,
			COUNT(1) 
	FROM fx67ll_alarm_count_copy
	WHERE alarm_date = "2021-01-08"
	GROUP BY alarm_date;

	这次有20个reduce
	```
2. 直接调整每个Job中的最大reduce数量，过于简单粗暴，尽量不要  
	```sql
	# 设置每个任务最大的reduce数为15个，默认为999
	set mapred.reduce.tasks = 15;
	
	SELECT alarm_date,
			COUNT(1) 
	FROM fx67ll_alarm_count_copy
	WHERE alarm_date = "2021-01-08"
	GROUP BY alarm_date;
	
	这次有15个reduce
	```

#### 推测执行
*参考map优化的最后一项*

#### 什么情况下只有一个Reduce  
很多时候你会发现任务中不管数据量多大，不管你有没有设置调整`reduce`个数的参数，任务中一直都只有一个`reduce`任务，
其实只有一个`reduce`任务的情况，除了数据量小于`hive.exec.reducers.bytes.per.reducer`参数值的情况外，还有以下原因：
1. 没有`Group By`的汇总，例如：
	```sql
	SELECT alarm_date,
			COUNT(1) 
	FROM fx67ll_alarm_count_copy
	WHERE alarm_date = "2021-01-08"
	GROUP BY alarm_date;
	
	写成
	
	SELECT COUNT(1) 
	FROM fx67ll_alarm_count_copy
	WHERE alarm_date = "2021-01-08";
	
	注意避免这样情况的发生
	```
2. 用了`Order by`排序，因为它会对数据进行全局排序，所以数据量特别大的时候效率非常低，尽量避免  
3. 有笛卡尔积，生产环境必须严格避免  

### Hive 任务整体优化
# 从P14继续看，Sprak On Hive的优化等Scala、Spark、Spark优化结束后再统一处理






**Hive优化大全 🕹️0.1.1 还未完全处理完，记得处理**


*看完视频后参考这两篇文章补充一下*
1. [Hive/HiveQL常用优化方法全面总结（上篇）](https://www.jianshu.com/p/8e2f2f0d4b6c)  
2. [Hive/HiveQL常用优化方法全面总结（下篇）](https://www.jianshu.com/p/deb4a6f91d3b)  


#### 什么情况下Hive不走MapReduce任务（添加到Hive面试宝典中）
1. [https://blog.csdn.net/u012667450/article/details/124460897](https://blog.csdn.net/u012667450/article/details/124460897)  
#### Hive常用开窗函数以及遇到不能解决的问题怎么办（添加到Hive面试宝典中）
1. [HIVE开窗函数](https://blog.csdn.net/weixin_46602525/article/details/117791073)  
2. 查阅更多资料，并考虑后期实操  
#### Hive中你用过UDF解决过哪些问题（）
1. [hive之UDF编程详解](https://blog.csdn.net/qq_32641659/article/details/89421696)  
2. Hive自定义UDF函数详解
3. 查阅更多资料，并考虑后期实操  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hive/hive-optimize-use.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***