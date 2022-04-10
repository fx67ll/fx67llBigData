# Hive在工作中的调优总结 🕹️v0.1.0

最近在为下一份工作做准备，hive sql的调优对于每一个数据开发工程师来说都是非常重要的必备技能，
所以我花了一点时间，总结了一下自己在以往工作中对于hive sql调优的一些实际应用，希望对读者有一定的作用  

## EXPLAIN 查看执行计划  

## 建表优化
#### 分区
1. 分区表基本操作，partitioned   
2. 二级分区  
3. 动态分区  
#### 分桶
1. 分桶表基本操作，clustered  
2. 分桶表主要是抽样查询，找出具有代表性的结果  
#### 选择合适的文件格式和压缩格式
1. LZO，拉兹罗
2. Snappy  
3. 压缩速度快，压缩比高

## HiveSQL语法优化
#### 单表查询优化
1. 列裁剪和分区裁剪，全表和全列扫描效率都很差，`SELECT *`  
2. Group By，map阶段会把同一个key发给一个reduce，当一个key过大时就倾斜了  
	+ 开启map端预聚合
	```
	# 是否在map端聚合，默认为true
	set hive.map.aggr = true;
	# 在map端聚合的条数
	set hive.groupby.mapaggr.checkintervel = 100000;
	# 在数据倾斜的时候进行均衡负载（默认是false），开启后会有两个mr任务
	# 当选项设定为true时，第一个mr任务会将map输出的结果随机分配到reduce，
	# 每个reduce会随机分布到reduce上，这样的处理结果是会使相同的`group by key`分到不同的reduce上。
	# 第二个mr任务再根据预处理的结果按`group by key`分到reduce上，保证相同`group by key`的数据分到同一个reduce上。
	# 但是这样能解决数据倾斜，但是不能让运行速度更快  
	# *切记！！！* 在数据量小的时候，开始数据倾斜负载均衡可能反而会导致时间变长  
	set hive.groupby.skewindata = true;
	``
3. Vectorization，矢量计算技术，通过设置批处理的增量大小为1024行单次来达到比单行单次更好的效率  
	```
	# 开启矢量计算  
	set hive.vectorized.execution.enabled = true;
	# 在reduce阶段开始矢量计算  
	set hive.vectorized.execution.reduce.enabled = true;
	```
4. 多重模式，一次读取多次插入，同一张表的插入操作优化成先`from table`再`insert`  
5. in/exists或者join用`left semi join`代替（为什么替代扩展一下~）  
#### 多表查询优化
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
7. 谓词下推，将sql语句中的where谓词逻辑都尽可能提前执行，减少下游处理的数据量，***非常关键的一个优化***  
	```
	# 这个设置是默认开启的，如果关闭了但是cbo开启，那么关闭依然不会生效，因为cbo会自动使用更为高级的优化计划  
	set hive.optimize.pdd = true;
	```
8. Map Join，减少reduce，减少shuffle，减少开销
	+ `set hive.auto.convert.join = true`，配置开启，默认是true  
	+ *注意！！！* 如果执行小表join大表，小表作为主连接的主表，所有数据都要写出去，此时会走reduce阶段，mapjoin会失效  
	+ 大表join小表不受影响，上一条的原因主要是因为小表join大表map的时候不知道reduce的结果其他reduce是否有，所以必须在最后reduce聚合的时候再处理，就产生了reduce的开销  
	+ mapjoin是指将join操作两方中比较小的表直接分发到各个map进程的内存中，在map中进行join的操作，这样就不用进行reduce的步骤，从而提高速度  
9. Map Join，大表和大表的MapReduce任务，SMB Join  
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
#### 单表携带了GroupBy字段的查询  
1. 任务中存在GroupBy操作，同时聚合函数为count或sum，单个key导致的数据倾斜可以这样通过设置开启map端预聚合参数的方式来处理  
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
#### 两表或多表的join查询操作  
1. 设置参数增加map数量
	```
	# join的key对应记录条数超过该数量，会进行分拆  
	set hive.skewjoin.key = 1000;
	# 并设置该参数为true，默认是false
	set hive.optimize.skewjoin = true;
	# 上面的参数如果开启了会将计算数量超过阈值的key写进临时文件，再启动另外一个任务做map join  
	# 可以通过设置这个参数，控制第二个任务的mapper数量，默认10000
	set hive.skewjoin.mapjoin.map.tasks = 10000;
	```
2. 使用mapjoin，减少reduce从根本上解决数据倾斜，参考`HiveSQL语法优化 -> 多表查询优化 ->  Map Join，大表和大表的MapReduce任务，SMB Join`  

## Hive 任务优化  
#### Hive Map 优化  
#### Hive Reduce 优化
#### Hive 任务整体优化
# 从Hive精讲P13继续

### 面试官如果听不下去你说的这么多优化方式，或者说就某一个深入问下去，需要做好这样的准备
### 优化大纲需要有，但不要一下就说出来，先长篇大论  

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hive/hive-optimize-use.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***