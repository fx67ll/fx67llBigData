# Hive 调优快速入门

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
	set hive.groupby.skewindata ]= true;
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
	+ 注意！！！如果执行小表join大表，小表作为主连接的主表，所有数据都要写出去，此时会走reduce阶段，mapjoin会失效  
	+ 大表join小表不受影响，原因主要是因为map的时候不知道reduce的结果其他reduce是否有，所以必须在最后reduce聚合的时候再处理，就产生了reduce的开销  
9. 大表和大表的MapReduce任务  