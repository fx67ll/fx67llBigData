# MySQL在工作中的优化方案总结 🕹1.0.0

MySQL查询性能优化是一个很大的课题，往往需要结合实际情况来制定优化策略，一般的步骤不外乎就是先`explain`分析，然后尽可能地利用索引，避免全表扫描，避免索引失效等


## 数据库结构优化
1. 最小数据长度，一般说来数据库的表越小，那么它的查询速度就越快，因此为了提高表的效率，应该将表的字段设置的尽可能小，
	比如身份证号，可以设置为`char(18)`就不要设置为`varchar(18)`  
2. 用最简单数据类型，能使用`int`类型就不要使用`varchar`类型，因为`int`类型比`varchar`类型的查询效率更高  
3. 尽量少定义`text`类型`text`类型的查询效率很低，如果必须要使用`text`定义字段，
	可以把此字段分离成子表，需要查询此字段时使用联合查询，这样可以提高主表的查询效率  


## SQL优化
1. 尽量用单表查询，禁止多于三表的`join`  
	+ MySQL其实在设计上是让连接和断开都很轻量级，在返回一个小的查询结果方面很高效  
	+ 如果想用一个复杂的查询，而这个查询涉及了多个表的关联，那其实性能还远不如将这个查询分解成的多个简单查询  
	+ 对于一个大查询可以采用分而治之，将大查询切分成小查询，每个查询功能完全一样，只完成一小部分，每次只返回一小部分查询结果  
	+ 将MySQL多表关联查询拆分成多个单表查询，然后将查询结果在应用程序逻辑层进行处理，可以提升性能  
2. 禁止使用`selct * from table`，尽量只获取需要的字段  
3. 避免多余的排序，因为涉及到排序，会增加大量的 CPU 运算，加大资源消耗及延迟  
4. 全模糊查询无法使用索引，尽量避免，因为索引是从左到右  
5. 使用`in`代替`or`  
6. 禁止隐式转换  
7. 禁止使用负向查询，例如`not in`、`!=`、`not like`  
8. 尽量使用`union all`代替`union`  
	+ union 对两个结果集进行并集操作，不包括重复行，相当于distinct，同时进行默认规则的排序，涉及到排序，会增加大量的 CPU 运算，加大资源消耗及延迟  
	+ union all 对两个结果集进行并集操作，包括重复行，即所有的结果全部显示，不管是不是重复  
9. 尽量早过滤，比如尽早使用where、limit、order by等等到子查询当中以减少数据量  
10. 如果希望知道结果集的行数，最好使用`count(*)`，而不是`count(结果集中的某一列)`  
	+ 字段有索引：count(*) ≈ count(1) > count(字段) > count(主键 id)  
	+ 字段无索引：count(*) ≈ count(1) > count(主键 id) > count(字段) 


## 索引优化
### 如果索引扫描的数据超过30%，那么索引会失效，变成全表扫描，避免给选择性的低的字段建立索引  

### 对索引进行函数操作之后，索引会失效，执行的是全表查询  
```sql
CREATE TABLE t1(
	id INT NOT NULL PRIMARY KEY,
	i INT,
	dt DATE
);
CREATE INDEX idx1 ON t1(dt);

EXPALIN
SELECT *
FROM t1
WHERE YEAR(dt) = '2022';

# 优化替换为索引的范围扫描，找到值之后再通过回表id查找数据
SELECT *
FROM t1
WHERE dt BETWEEN date '2022-01-01' AND date '2022-12-31';
```

### 多个索引，使用查询谓词`where`和排序谓词`order by`均不会影响索引  
```sql
CREATE TABLE t2(
	id INT NOT NULL PRIMARY KEY,
	i INT,
	dt DATE,
	v VARCHAR(50)
);
CREATE INDEX idx2 ON t2(i, dt);

EXPALIN
SELECT *
FROM t2
WHERE i = 99
ORDER BY dt DESC
LIMIT 5;
```

### 多个索引，使用`where条件`会有最左匹配原则  
```sql
CREATE TABLE t3(
	id INT NOT NULL PRIMARY KEY,
	col1 INT,
	col2 INT,
	col3 VARCHAR(50)
);
CREATE INDEX idx3 ON t3(col1, col2);

# 有最左的第一个列col1，所以该查询会使用索引查询  
EXPALIN
SELECT *
FROM t3
WHERE col1 = 99
AND col2 = 10;

# 无最左的第一个列col1，所以该查询不会使用索引查询 
EXPALIN
SELECT *
FROM t3
WHERE col2 = 10;
```

### 对于索引使用前后字符串均模糊查询，会执行全表扫描  
```sql
CREATE TABLE t4(
	id INT NOT NULL PRIMARY KEY,
	col1 INT,
	col2 VARCHAR(50)
);
CREATE INDEX idx4 ON t4(col2);

# 索引扫描是对每个索引进行从左到右处理  
EXPALIN
SELECT *
FROM t4
WHERE col2 LIKE "%sql%";

# 如果前面确定后面模糊查询，那么就会执行索引扫描  
EXPALIN
SELECT *
FROM t4
WHERE col2 LIKE "sql%";
```

### 多个索引，查询中有字段无索引，会导致覆盖索引 
```sql
CREATE TABLE t5(
	id INT NOT NULL PRIMARY KEY,
	col1 INT,
	col2 INT,
	col5 VARCHAR(50)
);
CREATE INDEX idx5 ON t5(col1, col3);

# 第一种查询性能高于第二种
# 第一种查询
EXPALIN
SELECT col3,
		COUNT(*)
FROM t35
WHERE col1 = 99
GROUP BY col3;

# 第二种查询
EXPALIN
SELECT col3,
		COUNT(*)
FROM t35
WHERE col1 = 99
AND col2 = 10
GROUP BY col3;
```


## 系统硬件优化
MySQL对硬件的要求主要体现在三个方面：磁盘、网络和内存  
1. 磁盘
	+ 磁盘寻道能力（磁盘I/O）,以目前高转速SCSI硬盘(7200转/秒)为例，这种硬盘理论上每秒寻道7200次，这是物理特性决定的，没有办法改变。
	+ 磁盘应该尽量使用有高性能读写能力的磁盘，比如固态硬盘，这样就可以减少 I/O 运行的时间，从而提高了 MySQL 整体的运行效率。
	+ 磁盘也可以尽量使用多个小磁盘而不是一个大磁盘，因为磁盘的转速是固定的，有多个小磁盘就相当于拥有多个并行运行的磁盘一样。
2. 网络
	+ 保证网络宽带的通畅（低延迟）以及够大的网络带宽是 MySQL 正常运行的基本条件，如果条件允许的话也可以设置多个网卡，以提高网络高峰期 MySQL 服务器的运行效率。
	+ DNS配置尽量使用`skip-name-resolve`来减少因解析带来的不必要麻烦。
	+ 检查网络的`ping`丢包率。
	+ 通过优化`/etc/sysctl.cnf `中的网络参数，提升性能。
3. 内存
	+ MySQL服务器的内存越大，那么存储和缓存的信息也就越多，而内存的性能是非常高的，从而提高了整个MySQL的运行效率。


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/sql/blog/sql-optimize.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***