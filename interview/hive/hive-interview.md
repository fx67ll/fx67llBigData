# Hive面试宝典 🕹️0.2.3
# 2023 Hive 面试大纲

#### 先说一些废话
总结一下Hive面试大纲，方便读者快速过一遍Hive面试所需要的知识点


## Hive的介绍
### Hive和Hadoop的关系
1. Hive利用hdfs存储数据，利用MapReduce查询数据  
2. Hive的数据存储在hdfs上，简单的说Hive就是hdfs的简单一种映射，比如：Hive的一张表映射hdfs上的一个文件，Hive的一个数据库就映射为hdfs上的文件夹  
3. Hive是一个计算框架，他是MapReduce的一种封装，实际上他的底层还是MR，Hive就是用人们熟悉的sql对数据进行分析的  
4. Hive执行程序是运行在Yarn上的  

### Hive的特点
1. Hive可以自由的扩展集群的规模，一般情况下不需要重启服务（世界上最大的Hadoop集群在Yahoo!，2009年的规模在4000台节点左右）  
2. Hive支持用户自定义函数，用户可以根据自己的需求来实现自己的函数（可能会引申自定义函数）  
3. 良好的容错性，节点出现问题SQL仍可完成执行（可能会拓展数据倾斜相关问题，或者直接问你你工作中有没有遇到这样的问题）  

### Hive的缺点
1. Hive的HQL表达能力有限。迭代式算法无法表达；数据挖掘方面不擅长  
2. Hive的效率比较低。Hive自动生成的MapReduce作业，通常情况下不够智能化；Hive调优比较困难，粒度较粗  
3. Hive执行延迟
	+ Hive 在查询数据的时候，由于没有索引，需要扫描整个表，因此延迟较高  
	+ 另外一个导致 Hive 执行延迟高的因素是 MapReduce框架，由于MapReduce 本身具有较高的延迟，因此在利用MapReduce 执行Hive查询时，也会有较高的延迟  
	+ 相对的，数据库的执行延迟较低。当然，这个低是有条件的，即数据规模较小，当数据规模大到超过数据库的处理能力的时候，Hive的并行计算显然能体现出优势  

### Hive常见的应用场景
1. 日志分析：大部分互联网公司使用Hive进行日志分析，包括百度、淘宝等  
	+ 统计网站一个时间段内的pv、uv  
	+ 多维度数据分析  
2. 海量结构化数据离线分析  

### Hive和mysql的区别
1. Hive采用了类SQL的查询语言HQL（hive query language），除了HQL之外，其余无任何相似的地方，Hive是为了数据仓库设计的  
2. 存储位置：Hive在Hadoop上；mysql将数据存储在设备或本地系统中  
3. 数据更新：Hive不支持数据的改写和添加，是在加载的时候就已经确定好了；数据库可以CRUD  
4. 索引：Hive无索引，每次扫描所有数据，底层是MR，并行计算，适用于大数据量；mysql有索引，适合在线查询数据  
6. 执行：Hive底层是MarReduce；mysql底层是执行引擎  
7. 可扩展性：Hive：大数据量，慢慢扩去吧；mysql:相对就很少了  


## Hive的架构
```
# Hive架构简易示意

Meta Store -> 
Client (CLI/JDBC/WebGUI + 
		Driver/驱动 + 
		SQL Parser/解析器 + 
		Physical Plan/编译器 + 
		QueryOptimizer/优化器 + 
		Execution/执行器) ->
MapReduce ->
HDFS
```
1. 用户接口：Hive 对外提供了三种服务模式，即 Hive 命令行模式（CLI），Hive 的 Web 模式（WUI），Hive 的远程服务（Client）  
	+ 其中最常用的是 CLI shell 命令行，CLI 启动的时候，会同时启动一个Hive副本  
	+ WUI 是通过浏览器访问 Hive，默认端口是***9999***  
	+ Client 是Hive的客户端，，在启动 Client模式 的时候，需要指出 Hive Server 所在节点，并且在该节点启动 Hive Server  
	+ JDBC/ODBC用 JAVA 实现，与传统数据库 JDBC 类似  
2. 元数据存储：通常是存储在关系数据库如 mysql , derby中  
	+ Hive中的元数据包括表的名字，表的列和分区及其属性，表的属性（是否为外部表等），表的数据所在目录等  
3. 解释器、编译器、优化器、执行器  
	+ 解释器、编译器、优化器完成 HQL 查询语句从词法分析、语法分析、编译、优化以及查询计划的生成  
	+ 生成的查询计划存储在 HDFS 中，并在随后有 MapReduce 调用执行（注意！！！包含*的查询，比如select * from tbl不会生成MapRedcue任务）  
	+ ===============================================================  
	+ 解析器（parser）：将查询字符串转化为解析树表达式  
	+ ===============================================================
	+ 编译器（physical plan）：分为`语义分析器（semantic analyzer）`和 `逻辑策略生成器（logical plan generator）`  
	+ 语义分析器（semantic analyzer）：将解析树表达式转换为基于块（block-based）的内部查询表达式  
	+ 逻辑策略生成器（logical plan generator）：将内部查询表达式转换为逻辑策略，这些策略由逻辑操作树组成  
	+ ===============================================================
	+ 优化器（optimizer）：通过逻辑策略构造多途径并以不同方式重写  


## Hive的数据
### Hive的数据模型
1. Hive中所有的数据都存储在hdfs中，没有专门的数据存储格式（可支持TextFile，SequenceFile，ParquetFile，RCFILE等）  
2. 只需要在创建表的时候告诉Hive数据中的列分隔符和行分隔符，Hive就可以解析数据  
3. Hive中包含以下数据模型：DB、Table、External Table、Partition、Bucket  
	+ DB：在hdfs中表现为`${hive.metastore.warehouse.dir}`目录下一个文件夹  
	+ Table：在hdfs中表现所属db目录下一个文件夹，普通表删除表后，hdfs上的文件都删了  
	+ External Table：外部表, 与table类似，不过其数据存放位置可以在任意指定路径，外部表删除后，hdfs上的文件没有删除，只是把文件删除了  
	+ Partition：在hdfs中表现为table目录下的子目录  
	+ Bucket：桶在hdfs中表现为同一个表目录下根据hash散列之后的多个文件，会根据不同的文件把数据放到不同的文件中  

### Hive的底层如何存储Null值
1. Null在Hive底层默认是用'\N'来存储的  
2. 能够经过`alter table test SET SERDEPROPERTIES('serialization.null.format' = 'a');`来修改  

### Hive中元数据`metadata`和元数据商店`metastore`的作用  
1. metadata即元数据，元数据包含用Hive创建的database、tabel等的元信息，元数据存储在`关系型数据库(RDBMS)`中，如derby、mysql等  
2. metastore的作用是：客户端连接metastore服务，metastore再去连接mysql数据库来存取元数据，
	有了metastore服务，就可以有多个客户端同时连接，而且这些客户端不需要知道mysql数据库的用户名和密码，只需要连接metastore服务即可  

### Hive有哪些保存元数据`metadata`的方式  
1. 内嵌模式：将元数据保存在本地内嵌的derby数据库中，内嵌的derby数据库每次只能访问一个数据文件，也就意味着它不支持多会话连接，适用于用来实验，不适用于生产环境  
2. 本地模式：将元数据保存在本地独立的数据库中（一般是mysql），这可以支持多会话连接  
3. 远程模式：把元数据保存在远程独立的mysql数据库中，避免每个客户端都去安装mysql数据库  
4. 需要注意的是： 内存数据库derby，安装小，但是数据存在内存，不稳定。mysql数据库，数据存储模式可以自己设置，持久化好，查看方便  

### Hive元数据存储方式中，本地模式和远程模式的区别  
1. **本地元存储**和**远程元存储**都采用**外部数据库**来存储元数据  
2. **本地元存储**不需要单独起metastore服务，用的是跟Hive在同一个进程里的metastore服务  
3. **远程元存储**需要单独起metastore服务，然后每个客户端都在配置文件里配置连接到该metastore服务，远程元存储的metastore服务和Hive运行在不同的进程  

### Hive的数据类型
1. 基本数据类型，因为Hive的底层是用java开发，所以基本数据类型和java保持一致  
	+ 整型 tinyint(字节整型) / smallint(短整型) / int(整型) / bigint(长整型)，分别占用1/2/4/8个字节，等价于java的 byte/short/int/long  
	+ 浮点型 float(浮点型) / double(双精度浮点型)，分别占用4/8个字节，等价于java的 float/double  
	+ 字符型 string，等价于数据库的 varchar，可变字符串，理论上可以存储2GB的字节  
	+ 布尔型 boolean，等价于java的 boolean
2. 复杂数据类型
	+ array/map，等价于java的array/map  
	+ struct，等价于c语言中的struct  
3. 类型转换
	+ Hive 的原子数据类型是可以进行隐式转换的，类似于 Java 的类型转换  
	+ 例如某表达式使用 int 类型，tinyint 会自动转换为 int 类型  
	+ 但是 Hive 不会进行反向转化，例如，某表达式使用 tinyint 类型，int 不会自动转换为 tinyint 类型，它会返回错误，除非使用 CAST 操作  
	+ ===============================================================
	+ 可以使用 CAST 操作显示进行数据类型转换  
	+ 例如 CAST('1' AS INT) 将把字符串'1' 转换成整数 1  
	+ 如果强制类型转换失败，如执行 CAST('X' AS INT)，表达式返回空值 NULL  

### Hive的隐式类型转换规则
1. 任何整数类型都可以隐式地转换为一个范围更广的类型，如 tinyint 可以转换成 int，int 可以转换成 bigint  
2. 所有整数类型、float 和 string 类型都可以隐式地转换成 double  
3. tinyint、smallint、int 都可以转换为 float  
4. boolean 类型不可以转换为任何其它的类型  

### Hive数据存储所使用的文件格式  
1. 默认是TextFile文件格式  
	+ 文本格式，Hive的默认格式，数据不压缩，磁盘开销大、数据解析开销大  
	+ 对应的Hive API为：`org.apache.hadoop.mapred.TextInputFormat和org.apache.hive.ql.io.HiveIgnoreKeyTextOutputFormat;`
	+ 可结合Gzip、Bzip2使用(系统自动检查，执行查询时自动解压)，但是使用这种方式，hive不会对数据进行切分，从而无法对数据进行并行操作  
2. RCFile文件格式
	+ RCFile是一种行列存储相结合的存储方式，先将数据按行进行分块再按列式存储，保证同一条记录在一个块上，避免读取多个块，有利于数据压缩和快速进行列存储  
	+ 对应Hive API为：`org.apache.hadoop.hive.ql.io.RCFileInputFormat和org.apache.hadoop.hive.ql.io.RCFileOutputFormat;`  
3. ORCFile文件格式
	+ 数据按行分块，每块按照列存储，不是真正意义上的列存储，可以理解为分段列存储  
	+ 用于降低Hadoop数据存储空间和加速Hive查询速度  
	+ ORCfile特点是压缩比比较高，压缩快，快速列存取，是RCfile的改良版本，相比RCfile能够更好的压缩，更快的查询  
	+ 需要注意的是ORC在读写时候需要消耗额外的CPU资源来压缩和解压缩，当然这部分的CPU消耗是非常少的  
	+ 优点：
	```
	每个task只输出单个文件，减少namenode负载；
	支持各种复杂的数据类型，比如：datetime，decima以及复杂类型struct、list、map；
	文件中存储了一些轻量级的索引数据；
	基于数据类型的块模式压缩：integer类型的列用行程长度编码，string类型的列使用字典编码；
	用多个相互独立的recordReaders并行读相同的文件
	无需扫描markers即可分割文件
	绑定读写所需内存
	metadata存储用protocol buffers，支持添加和删除列
	```
4. SequenceFile文件格式
	+ Hadoop提供的二进制文件，Hadoop支持的标准文件  
	+ 数据直接序列化到文件中，SequenceFile文件不能直接查看，可以通过Hadoop fs -text查看
	+ SequenceFile具有使用方便、可分割、可压缩、可进行切片，压缩支持NONE、RECORD、BLOCK（优先）  
	+ 对应Hive API：`org.apache.hadoop.mapred.SequenceFileInputFormat和org.apache.hadoop.hive.ql.io.HiveSequenceFileOutputFormat;`  
5. Parquet文件格式  
	+ 二进制存储，面向分析性的存储格式  
	+ 能够很好的压缩，同时减少大量的表扫描和反序列化的时间，有很好的查询性能，支持有限的模式演进，但是写速度通常比较慢  
	+ Parquet文件是以二进制方式存储的，所以是不可以直接读取的，文件中包括该文件的数据和元数据，因此Parquet格式文件是自解析的  
6. 总结
	+ TextFile 存储空间消耗比较大，并且压缩的text无法分割和合并查询的效率最低，可以直接存储，加载数据的速度最高  
	+ SequenceFile 存储空间消耗最大，压缩的文件可以分割和合并，查询效率高  
	+ ORCFile / RCFile 存储空间最小，查询的效率最高，加载的速度最低  
	+ Parquet 格式是列式存储，有很好的压缩性能和表扫描功能
	+ SequenceFile / ORCFile / RCFile 格式的表不能直接从本地文件导入数据，数据要先导入到TextFile格式的表中，
		然后再从TextFile表中导入到SequenceFile/ORCFile/RCFile表中  

### Hive中使用的压缩算法
1. 我们原始数据使用的是LZO的压缩格式，因为原始数据比较大，所以选择了支持切割的LZO压缩  
2. 清洗过的数据存到DWD层，我们在DWS中需要对清洗后的数据进行分析，所以我们DWD层使用的存储格式是Parquet，压缩格式是Snappy  
3. 之前我们压缩还遇到过一个问题，当时之前的项目中使用的是Snappy+ORC存储，后来我发现使用Snappy+ORC 存储比ORC单独存储还多占用了近一半的空间  
4. 后来我又对各个压缩格式及存储格式的结合做了一个测试，最终单独使用ORC存储节省了大量的空间  
5. Snappy压缩格式  
	+ 其中压缩比bzip2 > zlib > gzip > deflate > snappy > lzo > lz4，在不同的测试场景中，会有差异，这仅仅是一个大概的排名情况  
	+ bzip2、zlib、gzip、deflate可以保证最小的压缩，但在运算中过于消耗时间  
	+ 从压缩性能上来看：lz4 > lzo > snappy > deflate > gzip > bzip2，其中lz4、lzo、snappy压缩和解压缩速度快，压缩比低  
	+ 所以一般在生产环境中，经常会采用lz4、lzo、snappy压缩，以保证运算效率  

### 什么是数据可分割
1. 在考虑如何压缩那些将由MapReduce处理的数据时，考虑压缩格式是否支持分割是很重要的。  
	考虑存储在HDFS中的未压缩的文件，其大小为1GB，HDFS的块大小为64MB，所以该文件将被存储为16块。  
	将此文件用作输入的MapReduce作业会创建1个输人分片（split，也称为“分块”。对于block，我们统一称为“块”。）  
	每个分片都被作为一个独立map任务的输入单独进行处理  

2. 现在假设，该文件是一个gzip格式的压缩文件，压缩后的大小为1GB。和前面一样，HDFS将此文件存储为16块。
	然而，针对每一块创建一个分块是没有用的，因为不可能从gzip数据流中的任意点开始读取，map任务也不可能独立于其他分块只读取一个分块中的数据。
	gzip格式使用DEFLATE来存储压缩过的数据，DEFLATE将数据作为一系列压缩过的块进行存储。
	问题是，每块的开始没有指定用户在数据流中任意点定位到下一个块的起始位置，而是其自身与数据流同步。
	因此，gzip不支持分割(块)机制。 

3. 在这种情况下，MapReduce不分割gzip格式的文件，因为它知道输入是gzip压缩格式的(通过文件扩展名得知)，而gzip压缩机制不支持分割机制。
	因此一个map任务将处理16个HDFS块，且大都不是map的本地数据。
	与此同时，因为map任务少，所以作业分割的粒度不够细，从而导致运行时间变长。
	
### 关于压缩模式说明
1. 压缩模式评价：  
	```
	可使用以下三种标准对压缩方式进行评价：
	压缩比：压缩比越高，压缩后文件越小，所以压缩比越高越好。
	压缩时间：越快越好。
	已经压缩的格式文件是否可以再分割：可以分割的格式允许单一文件由多个Mapper程序处理，可以更好的并行化。
	```
2. 压缩模式对比
	```
	BZip2有最高的压缩比但也会带来更高的CPU开销，Gzip较BZip2次之。
	如果基于磁盘利用率和I/O考虑，这两个压缩算法都是比较有吸引力的算法。
	LZO和Snappy算法有更快的解压缩速度，如果更关注压缩、解压速度，它们都是不错的选择。 
	LZO和Snappy在压缩数据上的速度大致相当，但Snappy算法在解压速度上要较LZO更快。
	Hadoop的会将大文件分割成HDFS block(默认64MB)大小的splits分片，每个分片对应一个Mapper程序。
	在这几个压缩算法中 BZip2、LZO、Snappy压缩是可分割的，Gzip则不支持分割。
	```


## Hive的安装与使用
#### 当前版本请阅读以下参考资料，后期再行完善  
1. [hive的安装和使用](https://blog.csdn.net/RonieWhite/article/details/104141634)  
2. [Hive入门及常用指令](https://blog.csdn.net/ddydavie/article/details/80667727)  
3. 更多进阶内容请自行百度拓展查阅  

### 如何在Hive中集成HBase  
1. 将Hbase的客户端`jar`拷贝至`Hive/lib`目录下  
2. 修改`hive/conf`下的`hive-site.xml`配置文件，添加如下属性：  
	```
	<poperty>
		<name>hbase.zookeeper.quorum</name>
		<value>hadoop</value>
	</poperty>
	```
3. 启动Hive，创建表管理表`hbase_table_1`，指定数据存储在Hbase表中，主要是通过`stored by HBaseStorageHandler`类来实现  
4. 往Hive表`hbase_table_1`表中插入数据   

### 如何通过 HiveSQL 来直接读写 HBase
#### 当前版本请阅读以下参考资料，后期再行完善
1. [如何整合hive和hbase](https://zhuanlan.zhihu.com/p/74041611)  
2. [HiveHbase集成实践](https://www.cnblogs.com/cssdongl/p/6857891.html)  
3. 更多进阶内容请自行百度拓展查阅  


## Hive的分区和分桶
Hive的分区分桶都是数据存储和组织的策略，分区类似文件的分类归档，分桶类似于传统数据库的索引  

### 什么是Hive分区
1. Hive中数据库，表，及分区都是在HDFS存储的一个抽象  
2. Hive中的一个分区对应的就是HDFS的一个目录，目录名就是分区字段  
3. 声明分区表 `PARTITIONED BY (name string)`，分区键不能和任何列重名  
4. 声明数据要导入的分区 `PARTITION(name="fx67ll")`  
5. 查看分区 `SHOW PARTITIONAS`  
6. 根据分区查询 `WHERE name = "fx67ll"`  
7. 指定切分格式
	```
	ROW FORMAT DELIMITED
	# 每个字段之间由[ , ]分割
	FIELDS TERMINATED BY ','
	# 字段是Array形式，元素与元素之间由[ - ]分割
	COLLECTION ITEMS TERMINATED BY '-'
	# 字段是K-V形式，每组K-V对内部由[ : ]分割
	MAP KEYS TERMINATED BY ':';
	```

### Hive分区的优点
1. 如果一个表中有大量的数据，我们全部拿出来做查词的功能，耗时比较长，查询较慢，
	使用了分区，就可以做到用到了那个分区就拿那个分区中的数据方便了查询，提高了查词的效率  
2. 横向分配数据，使得负载更为均衡  

### Hive分区的缺点  
1. 容易造成过多的小分区，过多的目录  
2. 如果分区策略不佳，容易导致分区数据不均衡，造成数据倾斜  

### 什么是Hive分桶
1. 分桶是相对分区进行更细粒度的划分，分桶将整个数据内容按照某列属性值得hash值进行区分，类似于*关系型数据的索引*  
2. 如要安装id属性分为3个桶，就是对id属性值的hash值对3取摸，按照取模结果对数据分桶，
	如取模结果为0的数据记录存放到一个文件，取模为1的数据存放到一个文件，取模为2的数据存放到一个文件  
3. 分桶之前要执行命令 `set hive.enforce.bucketing = true`  
4. 声明分桶表 `CLUSTERED BY(id) INTO 3 BUCKETS`  

### 关于Hive索引的说明
1. 即从3.0开始索引已经被移除，有一些可替代的方案可能与索引类似：
	+ 具有自动重写的物化视图可以产生非常相似的结果，Hive2.3.0增加了对物化视图视图的支持  
	+ 使用列式文件格式（(Parquet、ORC）–他们可以进行选择性扫描；甚至可以跳过整个文件/块。很显然，例如我们创建表时使用的ORC格式就已经具有了索引的功能  
2. Hive为什么删除了索引：
	+ 由于Hive是针对海量数据存储的，创建索引需要占用大量的空间，最主要的是Hive索引无法自动进行刷新，也就是当新的数据加入时候，无法为这些数据自动加入索引

### Hive分桶的优点
1. 分桶字段需要根据业务进行设定，可以解决数据倾斜问题，主要是在关联join的时候通过map端更快的连接  
2. 能够提供类似的哈希的快速响应，比分区更快  

### Hive分桶的缺点
1. 需要在建表时规划好分桶策略，需要手动加载数据到分桶表  
2. 本质是空间换时间，时间换效率，所以在加载数据到表的时候有空间和时间上的消耗  	

### Hive中静态分区和动态分区的区别 
1. 静态分区与动态分区的主要区别在于静态分区是手动指定，而动态分区是通过数据来进行判断  
2. 详细来说，静态分区的列实在编译时期，通过用户传递来决定的；动态分区只有在SQL执行时才能决定  
3. 查询和写入的时候，静态分区键要用 `<static partition key> = <value>` 指定分区值；动态分区只需要给出分出分区键名称 `<dynamic partition key>`  
5. 一张表可同时被静态和动态分区键分区，只是动态分区键需要放在静态分区建的后面，因为HDFS上的动态分区目录下不能包含静态分区的子目录  

### Hive动态分区的参数设定
1. 开启动态分区
	```
	# 开启动态分区功能，默认false  
	set hive.exec.dynamic.partition = true  
	# 允许所有分区都是动态的，否则必须有静态分区字段，默认strict  
	set hive.exec.dynamic.partition.mode = nonstrict  
	```
2. 动态分区参数调优
	```
	# 每个mapper或reducer可以允许创建的最大动态分区个数，默认是100，超出则会报错  
	set hive.exec.max.dynamic.partitions.pernode = 1000
	# 一个动态分区语句可以创建的最大动态分区个数，默认是1000，超出报错
	set hive.exec.max.dynamic.partitions = 10000  
	# 全局可以创建的最大文件个数，默认是10000，超出报错  
	set hive.exec.max.created.files =100000  
	```


## Hive的内部表和外部表
### 什么是Hive的内部表和外部表
1. 没有`external`修饰，表数据保存在Hive默认的路径下，数据完全由Hive管理，删除表时元数据(metadata)和表数据都会一起删除  
2. 有`external`修饰，表数据保存在HDFS上，该位置由用户指定，删除表时，只会删除表的元数据(metadata)  

### Hive内部表和外部表的区别是什么
1. 内部表数据由Hive自身管理，外部表数据由HDFS管理  
2. 内部表数据存储的位置是`hive.metastore.warehouse.dir`，默认是 `/user/hive/warehouse`  
3. 外部表数据的存储位置由自己制定，如果没有LOCATION，Hive将在HDFS上的`/user/hive/warehouse`文件夹下以外部表的表名创建一个文件夹，并将属于这个表的数据存放在这里  
4. 删除内部表会直接删除元数据(metadata)及存储数据  
5. 删除外部表仅仅会删除元数据(metadata)，HDFS上的文件并不会被删除  
6. 对内部表的修改会将修改直接同步给元数据(metadata)，而对外部表的表结构和分区进行修改，则需要修复 `MSCK REPAIR TABLE table_name`  

### 生产环境中为什么建议使用外部表  
1. 因为外部表不会加载数据到Hive，减少数据传输，数据还能共享  
2. Hive不会修改数据，所以无需担心数据的损坏  
3. 删除表时，只删除表结构，不删除数据  


## Hive SQL
### Hive中的SQL如何转化成MapReduce任务的
1. `Antlr`定义SQL的语法规则，完成SQL词法，语法解析，将SQL转化为抽象语法树  
2. 遍历抽象语法树抽象出查询的基本组成单元 `QueryBlock`  
3. 遍历`QueryBlock` ，翻译为执行操作树`OperatorTree`  
4. 逻辑层优化器进行`OperatorTree`变换，合并不必要的`ReduceSinkOperator`，减少`shuffle`数据量  
5. 遍厉`OperatorTree`，翻译为`MapReduce`任务  
6. 物理层优化器进行`MapReduce`任务的变换，生成最终的执行计划  

### 什么情况下Hive不走MapReduce任务

### Hive中如何查询A表中B表不存在的数据  
**题目：A、B两表，找出ID字段中，存在A表，但是不存在B表的数据。A表总共13w数据，去重后大约3W条数据，B表有2W条数据，且B表的ID字段有索引**  
```
select * from  B
where (select count(1) as num from A where A.ID = B.ID) = 0
```

### Hive中有哪些连接查询以及如何使用
#### 当前版本请阅读以下参考资料，后期再行完善  
1. [Hive——join的使用](https://www.cnblogs.com/jnba/p/10673747.html)  
2. 更多进阶内容请自行百度拓展查阅  

### Hive中左连接和内连接的区别
1. 内连接：连接的键匹配上就连接，没有匹配上就过滤掉  
2. 左连接：以左表为基准，与右表做关联，关联上则连接，右表关联不上的则为null  

### Hive中左连接的底层原理  
**参考下面Hive查询的时候on和where有什么区别的理解二**  

### Hive查询的时候 ON 和 WHERE 有什么区别  
#### 共同点
1. on先执行，where后执行  
2. 并且where是对连接之后的结果进行的查询条件  
#### 第一种理解方式
1. 条件不为主表条件的时候，放在on和where的后面一样  
2. 条件为主表条件的时候，放在on后面，结果为主表全量，放在where后面的时候为主表条件筛选过后的全量  
```
1. select * from a left join b on a.id = b.id and a.dt=20181115;
2. select * from a left join b on a.id = b.id and b.dt=20181115;
3. select * from a join b on a.id = b.id and a.dt=20181115;
4. select * from a left join b on a.id = b.id  where a.dt=20181115;
sql1: 如果是left join 在on上写主表a的条件不会生效，全表扫描。
sql2: 如果是left join 在on上写副表b的条件会生效，但是语义与写到where 条件不同
sql3: 如果是inner join 在on上写主表a、副表b的条件都会生效
sql4: 建议这么写，大家写sql大部分的语义都是先过滤数据然后再join，所以在不了解join on+条件的情况下，条件尽量别写在on后，
直接写到where后就ok了，如果where条件后写b表的过滤条件，就变成了先left join出结果再按照b条件过滤数据  
```
#### 第二种理解方式
1. `on`是在生成连接表的起作用的，`where`是生成连接表之后对连接表再进行过滤  
2. 当使用`left join`时，无论`on`的条件是否满足，都会返回左表的所有记录，对于满足的条件的记录，两个表对应的记录会连接起来，对于不满足条件的记录，那右表字段全部是null  
3. 当使用`right join`时，类似，只不过是全部返回右表的所有记录  
4. 当使用`inner join`时，功能与`where`完全相同  
```
经过亲测后，更加深了对on和where的理解，得出以下结论：

1.ON后的条件如果有过滤主表的条件，则结果对于不符合该条件的主表数据也会原条数保留，只是不匹配右表数据而已。对于on后面对右表的过滤条件，连接时会用该条件直接过滤右表数据后再和左边进行左连接。总之，对于不满足on后面的所有条件的数据，左表会在结果数据中原条数保留数据，只是不匹配右表数据而已。不满足条件的右表数据各字段会直接以NULL连接主表。
2.ON后对左表的筛选条件对于结果行数会被忽略，但会影响结果中的匹配右表数据，因为只有符合左表条件的数据才会去和符合条件的右表数据进行匹配，不符合条件的左表数据会保留在最后结果中，但匹配的右表数据都是NULL.因此，对于需要过滤左表数据的话，需要把过滤条件放到where后面。
3.ON后的左表条件（单独对左表进行的筛选条件）对于结果行数无影响，还是会返回所有左表的数据，但和右表匹配数据时，系统只会拿左表符合条件（ON后的对左表过滤条件）的数据去和右表符合条件（ON后的对右表过滤条件）的数据进行匹配抓取数据，而不符合条件的左表数据还是会出现在结果列表中，只是对应的右表数据都是NULL。
4.ON后的右表条件（单独对右表进行的筛选条件）会先对右表进行数据筛选后再和左表做连接查询，对结果行数有影响（当左表对右表是一对多时），但不会影响左表的显示行数，然后拿符合条件的右表数据去和符合条件的左表数据进行匹配。
5.Where还是对连接后的数据进行过滤筛选，这个无异议。
6.匹配数据时无论左右表，都是拿符合ON后的过滤条件去做数据匹配，不符合的会保留左表数据，用NULL填充右表数据。

综上得出，ON后面对于左表的过滤条件，在最后结果行数中会被忽略，并不会先去过滤左表数据再连接查询，但是ON后的右表条件会先过滤右表数据再连接左表进行查询。
连接查询时，都是用符合ON后的左右表的过滤条件的数据进行连接查询，只有符合左右表过滤条件的数据才能正确匹配，剩下的左表数据会正常出现在结果集中，但匹配的右表数据是NULL。因此对于左表的过滤条件切记要放到Where后，对于右表的过滤条件要看情况了。如果需要先过滤右表数据就把条件放到ON后面即可。
```


## Hive 函数
### 关于 UDF/UDAF/UDTF 的提问
1. 如何使用UDF/UDAF/UDTF 
2. 为什么使用UDF/UDAF/UDTF   
3. 你写过什么样的UDF/UDAF/UDTF  
4. Hive自定义函数实现了什么函数
*上述四个问题自行 [参考资料](https://blog.csdn.net/sinat_30316741/article/details/113753651) 并结合工作中实际场景来作答，没有标准答案*

### Hive中如何去重
#### 第一种方式：使用 `DISTINCT`   
1. 对select 后面所有字段去重，并不能只对一列去重   
2. 当`DISTINCT`应用到多个字段的时候，`DISTINCT`必须放在开头，其应用的范围是其后面的所有字段，而不只是紧挨着它的一个字段，而且`DISTINCT`只能放到所有字段的前面  
3. `DISTINCT`对`NULL`是不进行过滤的，即返回的结果中是包含`NULL`值的  
4. 聚合函数中的`DISTINCT`,如`count()`会过滤掉为`NULL`  
#### 第二种方式：使用 `GROUP BY`  
1. 对`GROUP BY`后面所有字段去重，并不能只对一列去重  
#### 第三种方式：使用 `ROW_NUMBER() OVER` 窗口函数
1. 参考资料一：[一种巧妙的hive sql数据去重方法](https://blog.csdn.net/shuaiqig/article/details/116228534)  
2. 参考资料二：[Hive--数据去重及row_number()](https://blog.csdn.net/ABCDEFG0929/article/details/89190450)  
3. 参考资料三：[Hive(十一)--数据去重及row_number()](https://blog.csdn.net/yimingsilence/article/details/70140877)  

### Hive中排序函数的使用方式及区别  
1. `order by` 会对输入做全局排序，为保证全局的排序，因此只有一个reducer，会导致当输入规模较大时，需要较长的计算时间。
2. `sort by`不是全局排序，其在数据进入reducer前完成排序。因此，如果用`sort by`进行排序，则`sort by`只保证每个reducer的输出有序，不保证全局有序。
3. `distribute by 字段` 根据指定的字段将数据分到不同的reducer，且分发算法是hash散列，常用`sort by`结合使用，Hive要求`distribute by`语句要写在`sort by`语句之前。
4. `cluster by 字段` 除了具有`distribute by`的功能(既可以把数据分到不同的reduce)外，还会对该字段进行排序。但是排序只能是倒序排序，不能指定排序规则为`asc`或者`desc`
5. 因此：
	+ 当数据量规模较大时，不使用 `order by`，使用用 `distribute by + sort by`
	+ 如果 `distribute by` 和 `sort by` 字段是同一个时，此时，`cluster by = distribute by + sort by`

### Hive中部分高频函数 ———— `split` / `coalesce` / `collect list` / `collect set`  
1. [Hive ———— split](https://blog.csdn.net/qq_34105362/article/details/80408621)  
2. [Hive ———— coalesce](https://blog.csdn.net/weixin_42784951/article/details/114653922)  
3. [Hive ———— collect list/collect set](https://blog.csdn.net/LINBE_blazers/article/details/89198019)  

### Hive常用函数
1. [Hive常用的函数总结](https://blog.csdn.net/weixin_44318830/article/details/110738201)  
2. [Hive函数大全](https://blog.csdn.net/yezonghui/article/details/121293294)  


## Hive 运维
### 如何监控一个提交后的Hive状态  
1. 使用java代码提交Hive，通过HiveStatement获取日志数据并解析出`application_id`  
2. 就可以通过`application_id`去yarn上查看运行状态  


## Hive 优化
*该模块请参考我关于`Hive优化`的文章*  
1. 点击访问 ————> [Hive在工作中的调优总结](https://fx67ll.xyz/archives/hive-optimize-inwork)  
2. 点击访问 ————> [HiveSQL工作实战总结](https://fx67ll.xyz/archives/hive-sql-inwork)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/hive/hive.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***