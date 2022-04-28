# Sqoop面试宝典 🕹️0.1.0

## 高频面试题
### Sqoop参数
```
/opt/module/sqoop/bin/sqoop import \
--connect \
--username \
--password \
--target-dir \
--delete-target-dir \
--num-mappers \
--fields-terminated-by   \
--query   "$2" ' and $CONDITIONS;'

```

### Sqoop导入导出Null存储一致性问题
Hive中的Null在底层是以“\N”来存储，而MySQL中的Null在底层就是Null，为了保证数据两端的一致性。
在导出数据时采用--input-null-string和--input-null-non-string两个参数。导入数据时采用--null-string和--null-non-string。

### Sqoop底层运行的任务是什么
只有Map阶段，没有Reduce阶段的任务。默认是4个MapTask。

### Sqoop一天导入多少数据
100万日活=》10万订单，1人10条，每天1g左右业务数据
Sqoop每天将1G的数据量导入到数仓。

### Sqoop数据导出的时候一次执行多长时间
每天晚上00:10开始执行，Sqoop任务一般情况20-30分钟的都有。取决于数据量（11:11，6:18等 活动在1个小时左右）。

### Sqoop在导入数据的时候数据倾斜
Sqoop 参数撇嘴： split-by：按照自增主键来切分表的工作单元。
num-mappers：启动N个map来并行导入数据，默认4个；

### Sqoop数据导出Parquet（项目中遇到的问题）
Ads层数据用Sqoop往MySql中导入数据的时候，如果用了orc（Parquet）不能导入，需转化成text格式
（1）创建临时表，把Parquet中表数据导入到临时表，把临时表导出到目标表用于可视化
（2）ads层建表的时候就不要建Parquet表

### Sqoop之导入到Hive时特殊字符导致数据变乱
方法1: sqoop的sql中对含有特殊字符的字段进行replace操作
方法2: 使用hive-drop-import-delims，这是sqoop官方提供的一个参数，导入到hive时，遇到特殊字符就会将改字符丢弃
Sqoop还提供了另一个参数--hive-delims-replacement，它会将特殊字符替换为我们设定的字符。