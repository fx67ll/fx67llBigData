# Hive面试大纲 🕹️0.2.3
# 2023 Hive 面试大纲

#### 先说一些废话
总结一下Hive面试宝典中的要点，方便读者快速过一遍Hive面试所需要的知识点。
本文请搭配 [Hive面试宝典](https://fx67ll.xyz/archives/interview-hive) 来食用更美味哟 ┗( ▔, ▔ )┛

#### 方便自己系统性回忆，根据`*`的数量来标记重要性
> `*` 简单了解  
> `**` 熟悉掌握  
> `***` 需要精通  

1. Hive的介绍（*）  
	+ Hive和Hadoop的关系  
	+ Hive的特点  
	+ Hive的缺点  
	+ Hive常见的应用场景  
	+ Hive和mysql的区别  
2. Hive的架构（*） 
3. Hive的数据（**）  
	+ Hive的数据模型  
	+ *Hive的底层如何存储Null值*  
	+ Hive中元数据`metadata`和元数据商店`metastore`)`的作用  
	+ Hive有哪些保存元数据`metadata`的方式  
	+ Hive元数据存储方式中，本地模式和远程模式的区别  
	+ Hive的数据类型  
	+ Hive的隐式类型转换规则  
	+ Hive数据存储所使用的文件格式  
	+ Hive中使用的压缩算法  
	+ 什么是数据可分割  
	+ 关于压缩模式说明  
4. Hive的安装与使用（*）  
	+ 如何在Hive中集成HBase  
	+ 如何通过 HiveSQL 来直接读写 HBase  
5. Hive的分区和分桶（**）  
	+ 什么是Hive分区  
	+ *Hive分区的优点*  
	+ *Hive分区的缺点*  
	+ 什么是Hive分桶  
	+ 关于Hive索引的说明  
	+ *Hive分桶的优点*  
	+ *Hive分桶的缺点*  
	+ Hive中静态分区和动态分区的区别  
	+ *Hive动态分区的参数设定*  
6. Hive的内部表和外部表（*）  
	+ 什么是Hive的内部表和外部表  
	+ Hive内部表和外部表的区别是什么  
	+ 生产环境中为什么建议使用外部表  
7. Hive SQL（***）  
	+ *Hive中的SQL如何转化成MapReduce任务的*  
	+ 什么情况下Hive不走MapReduce任务  
	+ *Hive中如何查询A表中B表不存在的数据*  
	+ Hive中有哪些连接查询以及如何使用  
	+ Hive中左连接和内连接的区别  
	+ Hive中左连接的底层原理  
	+ *Hive查询时候 ON 和 WHERE 有什么区别*  
8. Hive 函数（***）  
	+ *如何使用UDF/UDAF/UDTF*  
	+ *为什么使用UDF/UDAF/UDTF*  
	+ *你写过什么样的UDF/UDAF/UDT*  
	+ *Hive自定义函数实现了什么函数什么接口*   
	+ Hive中如何去重  
	+ Hive中排序函数的使用方式及区别  
	+ 请说明以下常用函数 `split` / `coalesce` / `collect list` / `collect set` 的功能  
	+ 请描述工作中常用的Hive常用函数及使用场景  
9. Hive 运维（*）  
	+ 如何监控一个提交后的Hive状态  
10. Hive 优化（***）  
	+ *请说明你在工作中如何进行Hive优化*  
	+ HiveSQL优化 ———— Hive单表查询优化  
	+ HiveSQL优化 ———— Hive多表查询优化  
	+ HiveSQL优化 ———— Hive其他查询优化  
	+ Hive数据倾斜 ———— 单表携带了 Group By 字段的查询  
	+ Hive数据倾斜 ———— 两表或多表的 join 关联时，其中一个表较小，但是 key 集中  
	+ Hive数据倾斜 ———— 两表或多表的 join 关联时，有 Null值 或 无意义值  
	+ Hive数据倾斜 ———— 两表或多表的 join 关联时，数据类型不统一  
	+ Hive数据倾斜 ———— 单独处理倾斜key  
	+ HiveJob优化 ———— HiveMap优化方案  
	+ HiveJob优化 ———— HiveReduce优化方案  
	+ Hive整体优化方案  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/hive/hive-list.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***