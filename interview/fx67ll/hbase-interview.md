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


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/hbase/hbase.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***