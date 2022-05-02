# Hive优化大全 🕹️0.1.1

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