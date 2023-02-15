# Hive在工作中的调优总结 🕹️1.0.0

总结了一下在以往工作中，对于`Hive SQL`调优的一些实际应用，是日常积累的一些优化技巧，如有出入，欢迎在评论区留言探讨~


## 一、EXPLAIN 查看执行计划  

## 二、建表优化
### 2.1 分区
1. 分区表基本操作，partitioned   
2. 二级分区  
3. 动态分区  

### 2.2 分桶
1. 分桶表基本操作，clustered  
2. 分桶表主要是抽样查询，找出具有代表性的结果  

### 2.3 选择合适的文件格式和压缩格式
1. LZO，拉兹罗
2. Snappy  
3. 压缩速度快，压缩比高


## 三、HiveSQL语法优化
### 3.1 单表查询优化
1. 列裁剪和分区裁剪，全表和全列扫描效率都很差，生产环境绝对不要使用`SELECT *`，所谓列裁剪就是在查询时只读取需要的列，分区裁剪就是只读取需要的分区  
	+ 与列裁剪优化相关的配置项是`hive.optimize.cp`，默认是true  
	+ 与分区裁剪优化相关的则是`hive.optimize.pruner`，默认是true  
	+ 在`HiveSQL`解析阶段对应的则是`ColumnPruner`逻辑优化器  

2. Group By 配置调整，`map`阶段会把同一个`key`发给一个`reduce`，当一个`key`过大时就倾斜了，可以开启`map`端预聚合，可以有效减少`shuffle`数据量并  
	```
	# 是否在map端聚合，默认为true
	set hive.map.aggr = true;
	
	# 在map端聚合的条数
	set hive.groupby.mapaggr.checkintervel = 100000;
	
	# 在数据倾斜的时候进行均衡负载（默认是false），开启后会有 两个`mr任务`。
	# 当选项设定为true时，第一个 `mr任务` 会将map输出的结果随机分配到`reduce`，
	# 每个`reduce`会随机分布到`reduce`上，这样的处理结果是会使相同的`group by key`分到不同的`reduce`上。
	# 第二个 `mr任务` 再根据预处理的结果按`group by key`分到`reduce`上，
	# 保证相同`group by key`的数据分到同一个`reduce`上。
	
	# *切记！！！* 
	# 这样能解决数据倾斜，但是不能让运行速度更快  
	# 在数据量小的时候，开始数据倾斜负载均衡可能反而会导致时间变长  
	# 配置项毕竟是死的，单纯靠它有时不能根本上解决问题
	# 因此还是建议自行了解数据倾斜的细节，并优化查询语句  
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

### 3.2 多表查询优化
1. CBO优化，成本优化器，代价最小的执行计划就是最好的执行计划  
	+ join的时候表的顺序关系，前面的表都会被加载到内存中，后面的表进行磁盘扫描  
	+ 通过`hive.cbo.enable`，自动优化hivesql中多个join的执行顺序  
	+ 可以通过查询一下参数，这些一般都是true，无需修改  
	```
	set hive.cbo.enable = true;
	set hive.compute.query.using.stats = true;
	set hive.stats.fetch.column.stats = true;
	set hive.stats.fetch.partition.stats = true;
	```

2. 谓词下推（***非常关键的一个优化***），将`sql`语句中的`where`谓词逻辑都尽可能提前执行，减少下游处理的数据量，
	在关系型数据库如MySQL中，也有`谓词下推（Predicate Pushdown，PPD）`的概念，
	它就是将`sql`语句中的`where`谓词逻辑都尽可能提前执行，减少下游处理的数据量
	```
	# 这个设置是默认开启的 
	# 如果关闭了但是cbo开启，那么关闭依然不会生效 
	# 因为cbo会自动使用更为高级的优化计划  
	# 与它对应的逻辑优化器是PredicatePushDown
	# 该优化器就是将OperatorTree中的FilterOperator向上提
	set hive.optimize.pdd = true;
	
	# 举个例子
	# 对forum_topic做过滤的where语句写在子查询内部，而不是外部
	select a.uid,a.event_type,b.topic_id,b.title
	from calendar_record_log a
	left outer join (
	  select uid,topic_id,title from forum_topic
	  where pt_date = 20220108 and length(content) >= 100
	) b on a.uid = b.uid
	where a.pt_date = 20220108 and status = 0;
	```

3. Map Join，`map join`是指将`join`操作两方中比较小的表直接分发到各个`map`进程的内存中，在`map`中进行`join`的操作。
	`map join`特别适合`大小表join`的情况，Hive会将`build table`和`probe table`在`map`端直接完成`join`过程，消灭了`reduce`，减少`shuffle`，所以会减少开销  
	+ `set hive.auto.convert.join = true`，配置开启，默认是true  
	+ *注意！！！* 如果执行`小表join大表`，小表作为主连接的主表，所有数据都要写出去，此时会走`reduce`阶段，`mapjoin`会失效  
	+ `大表join小表`不受影响，上一条的原因主要是因为`小表join大表`的时候，`map`阶段不知道`reduce`的结果其他`reduce`是否有，
	+ 所以必须在最后`reduce`聚合的时候再处理，就产生了`reduce`的开销  
	```
	# 举个例子
	# 在最常见的`hash join`方法中，一般总有一张相对小的表和一张相对大的表，
	# 小表叫`build table`，大表叫`probe table`  
	# Hive在解析带join的SQL语句时，会默认将最后一个表作为`probe table`，
	# 将前面的表作为`build table`并试图将它们读进内存  
	# 如果表顺序写反，`probe table`在前面，引发`OOM（内存不足）`的风险就高了  
	# 在维度建模数据仓库中，事实表就是`probe table`，维度表就是`build table`  
	# 假设现在要将日历记录事实表和记录项编码维度表来`join`  
	select a.event_type,a.event_code,a.event_desc,b.upload_time
	from calendar_event_code a
	inner join (
	  select event_type,upload_time from calendar_record_log
	  where pt_date = 20220108
	) b on a.event_type = b.event_type;
	```

4. Map Join，大表和大表的`MapReduce任务`，可以使用`SMB Join`  
	+ 直接join耗时会很长，但是根据某字段分桶后，两个大表每一个桶就是一个小文件，两个表的每个小文件的分桶字段都应该能够一一对应（hash值取模的结果）  
	+ 总结就是分而治之，注意两个大表的分桶字段和数量都应该保持一致
	```
	set hive.optimize.bucketmapjoin = true;
	set hive.optimeize.bucketmapjoin.sortedmerge = true;
	hive.input.format = org.apache.hadoop.hive.ql.io.BucketizedHiveInputFormat;
	```

5. 多表join时key相同，这种情况会将多个`join`合并为一个`mr 任务`来处理  
	```
	# 举个例子
	# 如果下面两个join的条件不相同  
	# 比如改成a.event_code = c.event_code  
	# 就会拆成两个MR job计算
	select a.event_type,a.event_code,a.event_desc,b.upload_time
	from calendar_event_code a
	inner join (
	  select event_type,upload_time from calendar_record_log
	  where pt_date = 20220108
	) b on a.event_type = b.event_type
	inner join (
	  select event_type,upload_time from calendar_record_log_2
	  where pt_date = 20220108
	) c on a.event_type = c.event_type;
	```

6. 笛卡尔积，在生产环境中严禁使用  

### 3.3 其他查询优化
1. Sort By 代替 Order By，HiveQL中的`order by`与其他`sql`方言中的功能一样，就是将结果按某字段全局排序，这会导致所有`map`端数据都进入一个`reducer`中，
	在数据量大时可能会长时间计算不完。如果使用`sort by`，那么还是会视情况启动`多个reducer进行排序`，并且保证每个`reducer`内局部有序。
	为了控制`map`端数据分配到`reducer`的`key`，往往还要配合`distribute by`一同使用，如果不加`distribute by`的话，`map`端数据就会随机分配到`reducer`  
	```
	# 举个例子
	select uid,upload_time,event_type,record_data
	from calendar_record_log
	where pt_date >= 20220108 and pt_date <= 20220131
	distribute by uid
	sort by upload_time desc,event_type desc;
	```

2. Group By代替Distinct，当要统计某一列的去重数时，如果数据量很大，`count(distinct)`就会非常慢，原因与`order by`类似，
	`count(distinct)`逻辑只会有很少的`reducer`来处理。但是这样写会启动两个`mr任务`（单纯distinct只会启动一个），
	所以要确保数据量大到启动`mr任务`的`overhead`远小于计算耗时，才考虑这种方法，当数据集很小或者`key`的倾斜比较明显时，`group by`还可能会比`distinct`慢  


## 四、数据倾斜
注意要和数据过量的情况区分开，数据倾斜是大部分任务都已经执行完毕，但是某一个任务或者少数几个任务，一直未能完成，甚至执行失败，
而数据过量，是大部分任务都执行的很慢，这种情况需要通过扩充执行资源的方式来加快速度，大数据编程不怕数据量大，就怕数据倾斜，一旦数据倾斜，严重影响效率  

### 4.1 单表携带了 Group By 字段的查询  
1. 任务中存在`group by`操作，同时聚合函数为`count`或`sum`，单个`key`导致的数据倾斜可以这样通过设置开启`map`端预聚合参数的方式来处理  
	```
	# 是否在map端聚合，默认为true
	set hive.map.aggr = true;
	
	# 在map端聚合的条数
	set hive.groupby.mapaggr.checkintervel = 100000;
	
	# 有数据倾斜的时候开启负载均衡，这样会生成两个mr任务
	set hive.groupby.skewindata = true;
	```
2. 任务中存在`group by`操作，同时聚合函数为`count`或`sum`，多个`key`导致的数据倾斜可以通过增加`reduce`的数量来处理  
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

### 4.2 两表或多表的 join 关联时，其中一个表较小，但是 key 集中  
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

### 4.3 两表或多表的 join 关联时，有 Null值 或 无意义值
这种情况很常见，比如当`事实表是日志类数据`时，往往会有一些项没有记录到，我们视情况会将它置为`null`，或者`空字符串`、`-1`等，
如果缺失的项很多，在做`join`时这些空值就会非常集中，拖累进度，因此，若不需要空值数据，就提前写where语句过滤掉，
需要保留的话，将空值key用随机方式打散，例如将*用户ID为null的记录随机改为负值*：
```
select a.uid,a.event_type,b.nickname,b.age
from (
  select 
  (case when uid is null then cast(rand()*-10240 as int) else uid end) as uid,
  event_type from calendar_record_log
  where pt_date >= 20220108
) a left outer join (
  select uid,nickname,age from user_info where status = 4
) b on a.uid = b.uid;
```

### 4.4 两表或多表的 join 关联时，数据类型不统一
比如`int`类型和`string`类型进行关联，关联时候以小类型作为分区，这里`int`、`string`会到一个`reduceTask`中，如果数据量多，会造成数据倾斜
```
# 可以通过转换为同一的类型来处理
cast(user.id as string)
```

### 4.5 单独处理倾斜key
这其实是上面处理空值方法的拓展，不过倾斜的`key`变成了有意义的，一般来讲倾斜的`key`都很少，我们可以将它们抽样出来，
对应的行单独存入临时表中，然后打上一个较小的随机数前缀（比如`0~9`），最后再进行聚合


## 五、Hive Job 优化  
### 5.1 Hive Map 优化
#### 5.1.1 Map数量多少的影响
1. Map数过大  
	+ `map`阶段输出文件太小，产生大量小文件  
	+ 初始化和创建`map`的开销很大  
2. Map数太小  
	+ 文件处理或查询并发度小，`Job`执行时间过长  
	+ 大量作业时，容易堵塞集群  

#### 5.1.2 控制Map数的原则
根据实际情况，控制`map`数量需要遵循两个原则
1. 第一是使大数据量利用合适的`map`数  
2. 第二是使单个`map`任务处理合适的数据量   

#### 5.1.3 复杂文件适当增加Map数
1. 当`input`的文件都很大，任务逻辑复杂，`map`执行非常慢的时候，可以考虑增加`map`数，来使得每个`map`处理的数据量减少，从而提高任务的执行效率  
2.  那么如何增加`map`的数量呢？在`map`阶段，文件先被切分成`split`块，而后每一个`split`切片对应一个`Mapper任务`，
	`FileInputFormat`这个类先对输入文件进行逻辑上的划分，以`128m`为单位，将原始数据从逻辑上分割成若干个`split`，每个`split`切片对应一个`mapper任务`，
	所以说减少切片的大小就可增加`map`数量  
3. 可以依据公式计算`computeSliteSize(Math.max(minSize, Math.min(maxSize, blockSize))) = blockSize = 128m`  
4. 执行语句：`set mapreduce.input.fileinputformat.split.maxsize = 100;`  

#### 5.1.4 小文件进行合并减少Map数
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

#### 5.1.5 Map端预聚合减少Map数量
1. 相当于在`map`端执行`combiner`，执行命令：`set hive.map.aggr = true;`  
2. `combiners`是对`map`端的数据进行适当的聚合，其好处是减少了从`map`端到`reduce`端的数据传输量  
3. 其作用的本质，是将`map`计算的结果进行二次聚合，使`Key-Value<List>`中`List`的数据量变小，从而达到减少数据量的目的  

#### 5.1.6 推测执行
1. 在分布式集群环境下，因为程序Bug（包括Hadoop本身的bug），负载不均衡或者资源分布不均等原因，会造成同一个作业的多个任务之间运行速度不一致，
	有些任务的运行速度可能明显慢于其他任务（比如一个作业的某个任务进度只有50%，而其他所有任务已经运行完毕），则这些任务会拖慢作业的整体执行进度  
2. Hadoop采用了`推测执行（Speculative Execution）`机制，它根据一定的法则推测出拖后腿的任务，并为这样的任务启动一个备份任务，
	让该任务与原始任务同时处理同一份数据，并最终选用最先成功运行完成任务的计算结果作为最终结果  
3. 执行命令：`set mapred.reduce.tasks.speculative.execution = true;  # 默认是true`  
4. 当然，如果用户对于运行时的偏差非常敏感的话，那么可以将这些功能关闭掉，如果用户因为输入数据量很大而需要执行长时间的`map task`或者`reduce task`的话，
	那么启动推测执行造成的浪费是非常巨大的  

#### 5.1.7 合理控制Map数量的实际案例
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

### 5.2 Hive Reduce 优化
#### 5.2.1 Reduce数量多少的影响  
1. 同`map`一样，启动和初始化`reduce`也会消耗时间和资源  
2. 另外，有多少个`reduce`，就会有多少个输出文件，如果生成了很多个小文件，那么如果这些小文件作为下一个任务的输入，则也会出现小文件过多的问题  

#### 5.2.2 控制Reduce数的原则
和`map`一样，控制`reduce`数量需要遵循两个原则
1. 第一是使大数据量利用合适的`reduce`数  
2. 第二是使单个`reduce`任务处理合适的数据量   

#### 5.2.3 Hive自己如何确定Reduce数  
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

#### 5.2.4 如何调整Reduce数量
*注意！！！实际开发中，reduce的个数一般通过程序自动推定，而不人为干涉，因为人为控制的话，如果使用不当很容易造成结果不准确，且降低执行效率*  
1. 通过调整每个`reduce`任务处理的数据量来调整`reduce`个数，处理的数据量少了，任务数就多了   
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
2. 直接调整每个`Job`中的最大`reduce`数量，过于简单粗暴，慎用，尽量不要，虽然设置了`reduce`的个数看起来好像执行速度变快了，但是实际并不是这样的  
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

#### 5.2.5 推测执行
*参考map优化的最后一项*

#### 5.2.6 什么情况下只有一个Reduce  
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

### 5.3 Hive 任务整体优化
#### 5.3.1 Fetch抓取
`Fetch抓取`是指Hive在某些情况的查询可以不必使用`mr 任务`，例如在执行一个简单的`select * from XX`时，我们只需要简单的进行抓取对应目录下的数据即可。
在`hive-default.xml.template`中，`hive.fetch.task.conversion（默认是morn）`，老版本中默认是`minimal`。
该属性为`morn`时，在全局查找，字段查找，limit查找等都不走`mr 任务`  

#### 5.3.2 本地模式
Hive也可以不将任务提交到集群进行运算，而是直接在一台节点上处理，因为消除了提交到集群的overhead，所以比较适合数据量很小，且逻辑不复杂的任务。
设置`hive.exec.mode.local.auto`为true可以开启本地模式，但任务的输入数据总量必须小于`hive.exec.mode.local.auto.inputbytes.max（默认值128MB）`，
且mapper数必须小于`hive.exec.mode.local.auto.tasks.max（默认值4）`，`reducer`数必须为`0或1`，才会真正用本地模式执行  

#### 5.3.3 并行执行
Hive中互相没有依赖关系的`job`间是可以并行执行的，最典型的就是多个子查询`union all`，
在集群资源相对充足的情况下，可以开启并行执行，即将参数`hive.exec.parallel`设为true，
另外`hive.exec.parallel.thread.number`可以设定并行执行的线程数，默认为8，一般都够用。 
*注意！！！没资源无法并行，且数据量小时开启可能还没不开启快，所以建议数据量大时开启*  

#### 5.3.4 严格模式
要开启严格模式，需要将参数`hive.mapred.mode设为strict`。
所谓严格模式，就是强制不允许用户执行3种有风险的`sql`语句，一旦执行会直接失败，这3种语句是：  
1. 查询分区表时不限定分区列的语句  
2. 两表join产生了笛卡尔积的语句  
3. 用order by来排序但没有指定limit的语句  

#### 5.3.5 JVM重用
1. 主要用于处理小文件过多的时候
2. 在`mr 任务`中，默认是每执行一个`task`就启动一个`JVM`，如果`task`非常小而碎，那么`JVM`启动和关闭的耗时就会很长  
3. 可以通过调节参数`mapred.job.reuse.jvm.num.tasks`来重用  
4. 例如将这个参数设成5，那么就代表同一个`mr 任务`中顺序执行的5个`task`可以重复使用一个`JVM`，减少启动和关闭的开销，但它对不同`mr 任务`中的`task`无效  

#### 5.3.6 启用压缩
压缩job的中间结果数据和输出数据，可以用少量CPU时间节省很多空间，压缩方式一般选择Snappy，效率最高。
要启用中间压缩，需要设定`hive.exec.compress.intermediate`为true，
同时指定压缩方式`hive.intermediate.compression.codec`为`org.apache.hadoop.io.compress.SnappyCodec`。
另外，参数`hive.intermediate.compression.type`可以选择对`块（BLOCK）`还是`记录（RECORD）`压缩，BLOCK的压缩率比较高。
输出压缩的配置基本相同，打开`hive.exec.compress.output`即可  

#### 5.3.7 采用合适的存储格式
1. 在Hive SQL的`create table`语句中，可以使用`stored as ...`指定表的存储格式。
	Hive表支持的存储格式有`TextFile`、`SequenceFile`、`RCFile`、`Avro`、`ORC`、`Parquet`等。
	存储格式一般需要根据业务进行选择，在我们的实操中，绝大多数表都采用`TextFile`与`Parquet`两种存储格式之一。
2. `TextFile`是最简单的存储格式，它是纯文本记录，也是Hive的默认格式，虽然它的磁盘开销比较大，查询效率也低，但它更多地是作为跳板来使用。
3. `RCFile`、`ORC`、`Parquet`等格式的表都不能由文件直接导入数据，必须由`TextFile`来做中转。
4. `Parquet`和`ORC`都是Apache旗下的开源列式存储格式。列式存储比起传统的行式存储更适合`批量OLAP查询`，并且也支持更好的压缩和编码。
5. 我们选择`Parquet`的原因主要是它支持`Impala查询引擎`，并且我们对`update`、`delete`和`事务性操作`需求很低。


## 六、Hive的小文件
### 6.1 什么情况下会产生小文件?
1. 动态分区插入数据，产生大量的小文件，从而导致map数量剧增  
2. reduce数量越多，小文件也越多，有多少个reduce，就会有多少个输出文件，如果生成了很多小文件，那这些小文件作为下一次任务的输入  
3. 数据源本身就包含大量的小文件  

### 6.2 小文件有什么样的危害？
1. 从Hive的角度看，小文件会开很多map，一个map开一个java虚拟机jvm去执行，所以这些任务的初始化，启动，执行会浪费大量的资源，严重影响性能  
2. 在hdfs中，每个小文件对象约占150byte，如果小文件过多会占用大量内存，这样NameNode内存容量严重制约了集群的扩展
	+ 每个hdfs上的文件，会消耗128字节记录其meta信息，所以大量小文件会占用大量内存  

### 6.3 如何避免小文件带来的危害？
#### 6.3.1 从小文件产生的途经就可以从源头上控制小文件数量
1. 使用Sequencefile作为表存储格式，不要用textfile，在一定程度上可以减少小文件  
2. 减少reduce的数量(可以使用参数进行控制)  
3. 少用动态分区，用时记得按distribute by分区  
#### 6.3.2 对于已有的小文件
1. 使用hadoop archive命令把小文件进行归档，采用archive命令不会减少文件存储大小，只会压缩NameNode的空间使用  
2. 重建表，建表时减少reduce数量    


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hive/hive-optimize-use.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***