# Hbase面试宝典 🕹️0.1.0

## 数据模型
namespace，region，row，column（列族:列名），timestamp，cell（没有类型，全部是字节码形式贮存，有工具类进行转换类似于序列化）  

## hive关联hbase
示例代码，创建hive表的同时建立hbase表，字段之间是通过位置顺序完成映射的，尽量保持字段名的一致 ：
```
create external table hbase_stu (key string,
name string,
sex string,
Math int)
stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
with serdeproperties ("hbase.columns.mapping"=":key,info:name,info:sex,score:Math")
tblproperties ("hbase.table.name"="student");
```
实际操作应用：[建立Hive和Hbase的映射关系，通过Spark将Hive表中数据导入ClickHouse](https://download.csdn.net/download/m0_51197424/20667117)  

## rowkey的设计

## 高频面试题
### HBase存储结构
[一文讲清HBase存储结构](https://zhuanlan.zhihu.com/p/54184168)

### RowKey设计原则
[Hbase中rowkey设计原则](https://www.csdn.net/tags/MtTaEgzsMzIxNi1ibG9n.html)  
1. rowkey长度原则：
（rowkey不宜过长）
建议不要超过16个字节（若rowkey长度过长，memorystore会将部分缓存数据存入内存降低内存利用率，降低检索效率，HFile进行数据持久化时也会极大影响存储效率）；
2. rowkey散列原则：
设计目标：将数据均匀的分布在每个RegionServer，实现负载均衡，避免出现热点问题
热点问题解决：
*加盐*：在rowkey高几位随机生成一些字符串
*hash取值*：对rowkey取哈希值保证唯一性
*反转*：对rowkey进行反转，反转rowkey的例子以手机号为rowkey，可以将手机号反转后的字符串作为rowkey，这样的就避免了以手机号那样比较固定开头导致热点问题
3. rowkey唯一原则
原理：rowkey是按照字典顺序排序存储的，将经常读取的数据存储到一块

### RowKey如何设计
1）生成随机数、hash、散列值
2）字符串反转

### RowKey设计详解
HBase是三维有序存储的，通过rowkey（行键），column key（column family和qualifier）和TimeStamp（时间戳）这个三个维度可以对HBase中的数据进行快速定位。
HBase中rowkey可以唯一标识一行记录，在HBase查询的时候，有三种方式：
1. 通过get方式，指定rowkey获取唯一一条记录
2. 通过scan方式，设置startRow和stopRow参数进行范围匹配
3. 全表扫描，即直接扫描整张表中所有行记录
#### rowkey长度原则
rowkey是一个二进制码流，可以是任意字符串，最大长度 64kb ，实际应用中一般为10-100bytes，以 byte[] 形式保存，一般设计成定长。
*建议越短越好，不要超过16个字节，原因如下：*
数据的持久化文件HFile中是按照KeyValue存储的，如果rowkey过长，比如超过100字节，1000w行数据，光rowkey就要占用100*1000w=10亿个字节，将近1G数据，这样会极大影响HFile的存储效率；
MemStore将缓存部分数据到内存，如果rowkey字段过长，内存的有效利用率就会降低，系统不能缓存更多的数据，这样会降低检索效率。
#### rowkey散列原则
如果rowkey按照时间戳的方式递增，不要将时间放在二进制码的前面，建议将rowkey的高位作为散列字段，由程序随机生成，低位放时间字段，这样将提高数据均衡分布在每个RegionServer，以实现负载均衡的几率。如果没有散列字段，首字段直接是时间信息，所有的数据都会集中在一个RegionServer上，这样在数据检索的时候负载会集中在个别的RegionServer上，造成热点问题，会降低查询效率。
#### rowkey唯一原则
必须在设计上保证其唯一性，rowkey是按照字典顺序排序存储的，因此，设计rowkey的时候，要充分利用这个排序的特点，将经常读取的数据存储到一块，将最近可能会被访问的数据放到一块。
#### 热点问题
HBase中的行是按照rowkey的字典顺序排序的，这种设计优化了scan操作，可以将相关的行以及会被一起读取的行存取在临近位置，便于scan。然而糟糕的rowkey设计是热点的源头。 热点发生在大量的client直接访问集群的一个或极少数个节点（访问可能是读，写或者其他操作）。大量访问会使热点region所在的单个机器超出自身承受能力，引起性能下降甚至region不可用，这也会影响同一个RegionServer上的其他region，由于主机无法服务其他region的请求。 设计良好的数据访问模式以使集群被充分，均衡的利用。
为了避免写热点，设计rowkey使得不同行在同一个region，但是在更多数据情况下，数据应该被写入集群的多个region，而不是一个。
**下面是一些常见的避免热点的方法以及它们的优缺点：**
1. 加盐:
	这里所说的加盐不是密码学中的加盐，而是在rowkey的前面增加随机数，具体就是给rowkey分配一个随机前缀以使得它和之前的rowkey的开头不同。
	分配的前缀种类数量应该和你想使用数据分散到不同的region的数量一致。加盐之后的rowkey就会根据随机生成的前缀分散到各个region上，以避免热点。
2. 哈希:
	哈希会使同一行永远用一个前缀加盐。哈希也可以使负载分散到整个集群，但是读却是可以预测的。使用确定的哈希可以让客户端重构完整的rowkey，
	可以使用get操作准确获取某一个行数据。
3. 反转:
	第三种防止热点的方法时反转固定长度或者数字格式的rowkey。这样可以使得rowkey中经常改变的部分（最没有意义的部分）放在前面。
	这样可以有效的随机rowkey，但是牺牲了rowkey的有序性。
	反转rowkey的例子以手机号为rowkey，可以将手机号反转后的字符串作为rowkey，这样的就避免了以手机号那样比较固定开头导致热点问题
4. 时间戳反转：
	一个常见的数据处理问题是快速获取数据的最近版本，使用反转的时间戳作为rowkey的一部分对这个问题十分有用，
	可以用 `Long.Max_Value - timestamp` 追加到key的末尾，例如`[key][reverse_timestamp] , [key]` 的最新值可以通过`scan [key]`获得[key]的第一条记录，
	因为HBase中rowkey是有序的，第一条记录是最后录入的数据。
	比如需要保存一个用户的操作记录，按照操作时间倒序排序，在设计rowkey的时候，可以这样设计
	`[userId反转][Long.Max_Value - timestamp]`，在查询用户的所有操作记录数据的时候，
	直接指定反转后的`userId，startRow是[userId反转][000000000000]`，stopRow是`[userId反转][Long.Max_Value - timestamp]`
	如果需要查询某段时间的操作记录，startRow是`[user反转][Long.Max_Value - 起始时间]`，stopRow是`[userId反转][Long.Max_Value - 结束时间]`
#### 其他一些建议
1. 尽量减少rowkey和列的大小，当具体的值在系统间传输时，它的rowkey，列簇、列名，时间戳也会一起传输。如果你的rowkey、列簇名、列名很大，甚至可以和具体的值相比较，
那么将会造成大量的冗余，不利于数据的储存与传输
2. 列族尽可能越短越好，最好是一个字符
3. 列名也尽可能越短越好，冗长的列名虽然可读性好，但是更短的列名存储在HBase中会更好

### HBase 的特点是什么
1. 大：一个表可以有数十亿行，上百万列  
2. 无模式：每行都有一个可排序的主键和任意多的列，列可以根据需要动态的增加，同一张表中不同的行可以有截然不同的列  
3. 面向列：面向列（族）的存储和权限控制，列（族）独立检索  
4. 稀疏：空（null）列并不占用存储空间，表可以设计的非常稀疏  
5. 数据多版本：每个单元中的数据可以有多个版本，默认情况下版本号自动分配，是单元格插入时的时间戳  
6. 数据类型单一：Hbase 中的数据都是字符串，没有类型  

### HBase 优化方式
1. 高可用
在 HBase 中 Hmaster 负责监控 RegionServer 的生命周期，均衡 RegionServer 的负载，如果 Hmaster 挂掉了，
那么整个 HBase 集群将陷入不健康的状态，并且此时的工作状态并不会维持太久。所以 HBase 支持对 Hmaster 的高可用配置。
2. 预分区
每一个 region 维护着 startRow 与 endRowKey，如果加入的数据符合某个 region 维护的rowKey 范围，
则该数据交给这个 region 维护。那么依照这个原则，我们可以将数据所要投放的分区提前大致的规划好，以提高 HBase 性能。
3. RowKey 设计
一条数据的唯一标识就是 rowkey，那么这条数据存储于哪个分区，取决于 rowkey 处于哪个一个预分区的区间内，设计 rowkey 的主要目的 ，
就是让数据均匀的分布于所有的 region中，在一定程度上防止数据倾斜。
4. 内存优化
HBase 操作过程中需要大量的内存开销，毕竟 Table 是可以缓存在内存中的，一般会分配整个可用内存的 70%给 HBase 的 Java 堆。
但是不建议分配非常大的堆内存，因为 GC 过程持续太久会导致 RegionServer 处于长期不可用状态，一般 16~48G 内存就可以了，
如果因为框架占用内存过高导致系统内存不足，框架一样会被系统服务拖死。
5. 基础优化