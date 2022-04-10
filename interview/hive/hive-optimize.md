# Hive优化大全 🕹️0.1.1

## Hive的优化
**Hive优化大全，提取下面这些文章优秀的内容，并专门写一篇文章总结Hive的优化**
1. [hive优化大全-一篇就够了](https://blog.csdn.net/young_0609/article/details/84593316)  
2. [[一起学Hive]之十二-Hive SQL的优化](http://lxw1234.com/archives/2015/06/317.htm)  
3. [Hive/HiveQL常用优化方法全面总结（上篇）](https://www.jianshu.com/p/8e2f2f0d4b6c)  
4. [Hive/HiveQL常用优化方法全面总结（下篇）](https://www.jianshu.com/p/deb4a6f91d3b)  
5. [Hive总结篇及Hive的优化](https://blog.csdn.net/yu0_zhang0/article/details/81776459)  
6. [Hive调优及优化的12种方式](https://zhuanlan.zhihu.com/p/80718835?utm_source=qq)  
7. [hive优化之——控制hive任务中的map数和reduce数](http://lxw1234.com/archives/2015/04/15.htm)  
8. 上述弄完继续看看社区有没有好的方式，记得后续要自己手动试试  

### 优化大纲之一（**未整理完成，仅供参考**）
1. map join -> hql -> reduce join -> shuffle 会导致消耗磁盘资源，避免reduce阶段，减少shuffle开销  
2. 行列过滤，用哪个列哪哪个列，减少查询开销  
3. 分区分桶扫描优于全表扫描，减少查询开销
4. 设置合理的map数量。map越多jvm开一次关一次开销很大，浪费资源，map少但是存储的条数过多，处理的逻辑很复杂，会导致处理的时间长  
5. 设置合理的reduce数量。涉及到小文件，每一个reduce都会输出一个结果文件，导致小文件越多；reduce越少的话就会降低数据处理的并行度，降低了效率  
6. jvm重用，可以提升效率  
7. 小文件开启map端的局部聚合，CombineFileInputFormat  
8. 压缩，中间结果集压缩（处理要快，用snappy），最终结果集（reduce输出的结果，用bzip2，压缩比大）  

### 优化大纲之二（**未整理完成，仅供参考**）
1. 列裁剪和分区裁剪
2. 谓词下推
3. sort by代替order by
4. group by代替distinct
5. group by配置调整
	+ map端预聚合
	+ 倾斜均衡配置项
6. join基础优化
	+ build table（小表）前置
	+ 多表join时key相同
	+ 利用map join特性
	+ 分桶表map join
	+ 倾斜均衡配置项
7. 优化SQL处理join数据倾斜
	+ 空值或无意义值
	+ 单独处理倾斜key
	+ 不同数据类型
	+ build table过大
8. MapReduce优化
	+ 调整mapper数
	+ 调整reducer数
	+ 合并小文件
	+ 启用压缩
	+ JVM重用
9. 并行执行与本地模式
10. 严格模式
11. 采用合适的存储格式

### 如何合理的设置Hive中的map数  
#### input的文件总个数，input的文件大小，集群设置的文件块大小  
#### 举例子
	```
	假设input目录下有1个文件a，大小为780M，那么hadoop会将该文件a分隔成7个块（6个128m的块和1个12m的块），从而产生7个map数  
	假设input目录下有3个文件a、b、c，大小分别为10m、20m、130m，那么hadoop会分隔成4个块（10m、20m、128m、2m），从而产生4个map数  
	即，如果文件大于块大小(128m)，那么会拆分，如果小于块大小，则把该文件当成一个块  
	```
#### map是不是越多越好  
	+ 如果一个任务有很多小文件（远远小于块大小128m），则每个小文件也会被当做一个块，用一个map任务来完成  
	+ 而一个map任务启动和初始化的时间远远大于逻辑处理的时间，就会造成很大的资源浪费  
	+ 同时可执行的map数是受限的  
	+ ===============================================================  
	+ 假设`Select count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04’;`
	+ 该任务的`inputdir  /group/p_sdo_data/p_sdo_data_etl/pt/popt_tbaccountcopy_mes/pt=2012-07-04`  
	+ 共有194个文件，其中很多是远远小于128m的小文件，总大小9G，正常执行会用194个map任务  
	+ 我通过以下方法来在map执行前合并小文件，减少map数：
	```
	set mapred.max.split.size=100000000;（100M）
	set mapred.min.split.size.per.node=100000000;
	set mapred.min.split.size.per.rack=100000000;
	set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
	前面三个参数确定合并文件块的大小，大于文件块大小128m的，按照128m来分隔，小于128m，大于100m的，按照100m来分隔，
	把那些小于100m的（包括小文件和分隔大文件剩下的），进行合并，最终生成了74个块  
	```
#### 是不是保证每个map处理接近128m的文件块，就高枕无忧了
	+ 比如有一个127m的文件，正常会用一个map去完成，但这个文件只有一个或者两个小字段，却有几千万的记录  
	+ 如果map处理的逻辑比较复杂，用一个map任务去做，肯定也比较耗时
	+ ===============================================================
	+ 当input的文件都很大，任务逻辑复杂，map执行非常慢的时候，可以考虑增加Map数，来使得每个map处理的数据量减少，从而提高任务的执行效率  
	+ 假设`Select data_desc,count(1),count(distinct id),sum(case when …),sum(case when …),sum(…)from a group by data_desc`  
	+ 如果表a只有一个文件，大小为120M，但包含几千万的记录，如果用1个map去完成这个任务，肯定是比较耗时的，这种情况下，我们要考虑将这一个文件合理的拆分成多个  
	+ 这样就可以用多个map任务去完成：
	```
	set mapred.reduce.tasks=10;
	create table a_1 as
	select * from a
	distribute by rand(123);
	这样会将a表的记录，随机的分散到包含10个文件的a_1表中，再用a_1代替上面sql中的a表，则会用10个map任务去完成  
	每个map任务处理大于12M（几百万记录）的数据，效率肯定会好很多
	```
	+ 看上去，貌似这两种有些矛盾，一个是要合并小文件，一个是要把大文件拆成小文件，这点正是重点需要关注的地方  
	+ 根据实际情况，控制map数量需要遵循两个原则：第一是使大数据量利用合适的map数，第二是使单个map任务处理合适的数据量  

### 如何合理的设置Hive中的reduce数  
#### Hive自己如何确定reduce数  
```
reduce个数的设定极大影响任务执行效率，不指定reduce个数的情况下，Hive会猜测确定一个reduce个数，基于以下两个设定：
hive.exec.reducers.bytes.per.reducer（每个reduce任务处理的数据量，默认为1000^3=1G）
hive.exec.reducers.max（每个任务最大的reduce数，默认为999）
计算reducer数的公式很简单N=min(参数2，总输入数据量/参数1)
即，如果reduce的输入（map的输出）总大小不超过1G,那么只会有一个reduce任务；
如：select pt,count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04′ group by pt;
/group/p_sdo_data/p_sdo_data_etl/pt/popt_tbaccountcopy_mes/pt=2012-07-04 总大小为9G多，因此这句有10个reduce
```
#### 调整reduce个数（*方法一*）  
```
调整hive.exec.reducers.bytes.per.reducer参数的值；
set hive.exec.reducers.bytes.per.reducer=500000000; （500M）
select pt,count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04′ group by pt; 这次有20个reduce
```
#### 调整reduce个数（*方法二*）  
```
set mapred.reduce.tasks = 15;
select pt,count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04′ group by pt;这次有15个reduce
```
#### reduce个数并不是越多越好  
```
同map一样，启动和初始化reduce也会消耗时间和资源；
另外，有多少个reduce,就会有多少个输出文件，如果生成了很多个小文件，那么如果这些小文件作为下一个任务的输入，则也会出现小文件过多的问题；
```
#### 什么情况下只有一个reduce  
```
很多时候你会发现任务中不管数据量多大，不管你有没有设置调整reduce个数的参数，任务中一直都只有一个reduce任务；
其实只有一个reduce任务的情况，除了数据量小于hive.exec.reducers.bytes.per.reducer参数值的情况外，还有以下原因：
a)    没有group by的汇总，比如把select pt,count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04′ group by pt; 写成 select count(1) from popt_tbaccountcopy_mes where pt = ‘2012-07-04′;
这点非常常见，希望大家尽量改写。
b)    用了Order by
c)    有笛卡尔积
通常这些情况下，除了找办法来变通和避免，我暂时没有什么好的办法，因为这些操作都是全局的，所以hadoop不得不用一个reduce去完成；
同样的，在设置reduce个数的时候也需要考虑这两个原则：使大数据量利用合适的reduce数；使单个reduce任务处理合适的数据量；
```
	

## Hive的小文件
### 什么情况下会产生小文件?
1. 动态分区插入数据，产生大量的小文件，从而导致map数量剧增  
2. reduce数量越多，小文件也越多(reduce的个数和输出文件是对应的)  
3. 数据源本身就包含大量的小文件  

### 小文件有什么样的危害？
1. 从Hive的角度看，小文件会开很多map，一个map开一个java虚拟机jvm去执行，所以这些任务的初始化，启动，执行会浪费大量的资源，严重影响性能  
2. 在hdfs中，每个小文件对象约占150byte，如果小文件过多会占用大量内存，这样NameNode内存容量严重制约了集群的扩展
	+ 每个hdfs上的文件，会消耗128字节记录其meta信息，所以大量小文件会占用大量内存  

### 如何避免小文件带来的危害？
#### 从小文件产生的途经就可以从源头上控制小文件数量
1. 使用Sequencefile作为表存储格式，不要用textfile，在一定程度上可以减少小文件  
2. 减少reduce的数量(可以使用参数进行控制)  
3. 少用动态分区，用时记得按distribute by分区  

#### 对于已有的小文件
1. 使用hadoop archive命令把小文件进行归档，采用archive命令不会减少文件存储大小，只会压缩NameNode的空间使用  
2. 重建表，建表时减少reduce数量  
	+ 这两个参数是Hive自己确定reduce数量的参数  
	+ 实际开发中，reduce的个数一般通过程序自动推定，而不人为干涉，因为人为控制的话，如果使用不当很容易造成结果不准确，且降低执行效率  
	+ `hive.exec.reducers.bytes.per.reducer`（每个reduce任务处理的数据量，默认为1000^3=1G） 
	+ `hive.exec.reducers.max`（每个任务最大的reduce数，默认为999）  
	+ 在数据进入到reduce中的时候，在map的输入的时候总的数据量小于这个数的时候，会交给一个reduce去处理  
	+ ===============================================================  
	+ 调整reduce数量的方法一
	+ `set hive.exec.reducers.bytes.per.reducer=500000000;`（500M）  
	+ ===============================================================  
	+ 调整reduce数量的方法二  
	+ `set mapred.reduce.tasks = 15;`  
	+ 通过设置reduce的个数进行reduce端的设置，慎用，因为直接定死了  
	+ 虽然设置了reduce的个数看起来好像执行速度变快了，但是实际并不是这样的  
	+ 同map一样，启动和初始化reduce也会消耗时间和资源  
	+ 另外，有多少个reduce，就会有多少个输出文件，如果生成了很多小文件，那这些小文件作为下一次任务的输入，则也会出现小文件过多的问题  
	+ ===============================================================  
	+ 说完上面的内容可以往下继续说，Hive中整个sql的优化可以从下面几个步骤去优化（详见*Hive优化*）
	```
	（1）尽量尽早地过滤数据，减少每个阶段的数据量,对于分区表要加分区，同时只选择需要使用到的字段
	（2）单个SQL所起的JOB个数尽量控制在5个以下
	（3）慎重使用mapjoin,一般行数小于2000行，大小小于1M(扩容后可以适当放大)的表才能使用,小表要注意放在join的左边。否则会引起磁盘和内存的大量消耗
	（4）写SQL要先了解数据本身的特点，如果有join ,group操作的话，要注意是否会有数据倾斜
		set hive.exec.reducers.max=200;
		set mapred.reduce.tasks= 200;---增大Reduce个数
		set hive.groupby.mapaggr.checkinterval=100000 ;--这个是group的键对应的记录条数超过这个值则会进行分拆,值根据具体数据量设置
	（5）如果union all的部分个数大于2，或者每个union部分数据量大，应该拆成多个insert into 语句，这样会提升执行的速度。
	```
3. 通过参数进行调节，设置map/reduce端的相关参数，如下： 
	+ 设置map输入合并小文件的相关参数：  
	```
	//每个Map最大输入大小(这个值决定了合并后文件的数量)
	set mapred.max.split.size=256000000;
	//一个节点上split的至少的大小(这个值决定了多个DataNode上的文件是否需要合并)
	set mapred.min.split.size.per.node=100000000;
	//一个交换机下split的至少的大小(这个值决定了多个交换机上的文件是否需要合并)
	set mapred.min.split.size.per.rack=100000000;
	//执行Map前进行小文件合并
	set hive.input.format=org.apache.hadoop.hive.ql.io.CombineHiveInputFormat;
	```
	+ 设置map输出和reduce输出进行合并的相关参数：  
	```
	//设置map端输出进行合并，默认为true
	set hive.merge.mapfiles = true;
	//设置reduce端输出进行合并，默认为false
	set hive.merge.mapredfiles = true;
	//设置合并文件的大小
	set hive.merge.size.per.task = 256*1000*1000;
	//当输出文件的平均大小小于该值时，启动一个独立的MapReduce任务进行文件merge。
	set hive.merge.smallfiles.avgsize=16000000;
	```

#### 参考文档————Hive小文件的相关问题
1. [Hadoop的Archive归档命令使用指南 ](https://www.cnblogs.com/staryea/p/8603112.html)  
2. [关于hive中的reduce个数的设置](https://www.cnblogs.com/gxgd/p/9431525.html)  
3. [hive优化之——控制hive任务中的map数和reduce数](http://lxw1234.com/archives/2015/04/15.htm)  


## Hive数据倾斜
### 产生的原因
### 解决的方案

#### 参考文档————Hive数据倾斜
1. [hive数据倾斜原因和解决方法](https://blog.csdn.net/u010670689/article/details/42920917)  
2. [Hive中的数据倾斜](https://zhuanlan.zhihu.com/p/82616299)  
3. [Hive学习之路 （十九）Hive的数据倾斜 ](https://www.cnblogs.com/qingyunzong/p/8847597.html)  
4. [Hive数据倾斜案例讲解](http://www.techweb.com.cn/cloud/2020-11-03/2809569.shtml)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/hive/hive-optimize.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***