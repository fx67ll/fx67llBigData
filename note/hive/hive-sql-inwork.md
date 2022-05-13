# HiveSQL工作记录 🕹️0.5.1  

记录一些工作中有意思的统计指标，当然做过一些简化方便大家阅读，后续会不断更新，欢迎关注追踪~

## 问题类型
1. 连续问题  
	+ 两种思路  
	+ 第一种：日期减去一列数字得出日期相同，主要是通过`row_number`窗口函数  
	+ 第二种：后一个日期减去前一个日期差值相等，用的较少，可以用`lag/lead`窗口函数解决  
2. 分组问题  
	+ 主要使用`lag(col,1,0)`分组将每行移到下一行，再按既定规则分组排序即可  
	+ [后面抽空试一下](https://www.bilibili.com/video/BV1Y34y1C7mV?p=34)  
3. 间隔连续问题，比如每断一天也算连续  
	+ 两种思路：
	+ 第一种：连续使用两次连续问题的求法即可，差了几次可以连续`row_number`几次，*这种无限套娃不推荐使用*  
	+ 第二种：连续差值小于要求数即可，比如断一天也可，只要每行间隔小于2天即可  
4. 打折日期交叉问题，两段活动期重复日期去除  
5. 同时在线问题  


## 统计每个设备的累计告警次数
### 原始数据格式
|  deviceId  |  alarmDate  |  alarmCount  |
|  :----:  |  :----:  |  :----:  |
|  设备ID  |  告警日期  |  告警次数  |
|  u01  |  2022/1/8  |  5  |
|  u02  |  2022/1/8  |  7  |
|  u03  |  2022/1/8  |  3  |
|  u01  |  2022/1/12  |  2  |
|  u02  |  2022/1/12  |  1  |
|  u01  |  2022/1/14  |  9  |
|  ...  |  ...  |  ...  |

### 统计之后格式
|  设备ID  |  告警月份  |  告警次数小计  |  告警次数累计  |
|  :----:  |  :----:  |  :----:  |  :----:  |
|  u01  |  2022-02  |  11  |  11  |
|  u02  |  2022-03  |  12  |  23  |
|  ...  |  ...  |  ...  |  ...  |

### 工作思路
1. 先根据设备ID和告警日期分组
2. 按月份统计可以用substr函数或者日期格式化函数
3. 再统计即可得出小计告警次数
4. 接着使用聚合窗口函数计算累计告警次数

### 工作语句
1. 第一种方案，使用`substr`截取字符串函数
```sql
SELECT *,
	   SUM(sumPart) OVER (PARTITION BY deviceId ORDER BY alarmMonth) AS sumAll
FROM
(SELECT deviceId,
	   SUBSTR(alarmDate,1,7) AS alarmMonth,
	   SUM(alarmCount) AS sumPart
FROM test_00
GROUP BY deviceId,
		 alarmMonth) t;
```
2. 第二种方案，使用日期格式化函数，使用`date_format`函数的字符串必须满足yyyy-MM-dd格式，所以必须先用`regexp_replace`替换`/`为`-`
```sql
SELECT *,
	   SUM(sumPart) OVER (PARTITION BY deviceId ORDER BY alarmMonth) AS sumAll
FROM
(SELECT deviceId,
	    DATE_FORMAT(REGEXP_REPLACE(alarmDate,'/','-'), 'yyyy-MM') AS alarmMonth,
	    SUM(alarmCount) AS sumPart
FROM test_00
GROUP BY deviceId,
		 alarmMonth) t;
```
#### 开窗函数中的界限说明
+ unbounded：无界限  
+ preceding：从分区第一行头开始，则为 `unbounded` *N为：相对当前行向后的偏移量*  
+ following ：与`preceding`相反,到该分区结束，则为 `unbounded` *N为：相对当前行向后的偏移量*  
+ current row：顾名思义，当前行，偏移量为0  


## 统计环境设备每天的总污染告警次数，并输出每个设备告警次数排名前三的日期
### 原始数据格式
|  deviceId  |  alarmTime  |
|  :----:  |  :----:  |
|  设备ID  |  告警时间  |
|  u01  |  2022/1/8/08/04/58  |
|  u02  |  2022/1/8/12/05/38  |
|  u03  |  2022/1/8/17/01/12  |
|  u01  |  2022/1/12/12/04/53  |
|  u02  |  2022/1/12/13/45/34  |
|  u01  |  2022/1/14/02/12/51  |
|  ...  |  ...  |

### 统计之后格式
|  设备ID  |  告警次数累计  |
|  :----:  |  :----:  |
|  u01  |  3  |
|  u02  |  2  |
|  u03  |  1  |
|  ...  |  ...  |

### 工作思路
#### 统计环境设备的总污染告警次数
1. 由于有设备可能会有同一时间的告警记录，所以需要按告警时间去重后再统计
2. 如果使用distinct去重，如果表数据过大，且设备ID差异化很大，那么会有性能压力  
3. 所以使用group by子查询代替  
4. mysql中的`date_format`格式化需要这样写：`DATE_FORMAT(alarmTime, '%Y-%c-%d %T')`  
#### 输出每个设备告警次数排名前三的日期
1. 使用窗口函数`ROW_NUMBER() OVER()`进行分组排序即可，[MySQL 替换 ROW_NUMBER() OVER (PARTITION ……) 函数](https://blog.csdn.net/liuyaru11/article/details/106685366)  
2. 多个子句查询可以使用视图和`WITH`语句 

### 工作语句
#### 统计环境设备的总污染告警次数
```sql
SELECT deviceId,
		COUNT(alarmTime) AS alarmCount 
FROM
--- http://c.biancheng.net/mysql/date_format.html
(SELECT deviceId,
		DATE_FORMAT(REGEXP_REPLACE(alarmTime,'/','-'), 'yyyy-MM-dd HH:mm:ss') AS alarmTime
FROM test_01
GROUP BY deviceId,alarmTime
ORDER BY alarmTime) t
GROUP BY deviceId;
```
#### 输出每个设备告警次数排名前三的日期
```sql
SELECT * 
FROM 
(SELECT deviceId,
		alarmDate,
		alarmCount,
		ROW_NUMBER() OVER(PARTITION BY deviceId ORDER BY alarmCount DESC) AS alarmRank
FROM 
(SELECT deviceId,
		alarmDate,
		COUNT(alarmDate) AS alarmCount
FROM
(SELECT deviceId,
		DATE_FORMAT(alarmTime, 'yyyy-MM-dd') AS alarmDate,
		DATE_FORMAT(alarmTime, 'yyyy-MM-dd HH:mm:ss') AS alarmTime
FROM test_01
GROUP BY deviceId,alarmTime
ORDER BY deviceId,alarmTime) t1
GROUP BY deviceId,alarmDate) t2) t3
WHERE alarmRank<=3;

-- 使用WITH语句优化一下
WITH t1 AS (
SELECT deviceId,
	DATE_FORMAT(alarmTime, 'yyyy-MM-dd') AS alarmDate,
	DATE_FORMAT(alarmTime, 'yyyy-MM-dd HH:mm:ss') AS alarmTime
FROM test_01
GROUP BY deviceId,alarmTime
ORDER BY deviceId,alarmTime),
t2 AS (
	SELECT deviceId,
	alarmDate,
	COUNT(alarmDate) AS alarmCount
FROM t1
GROUP BY deviceId,alarmDate),
t3 AS (
SELECT deviceId,
	alarmDate,
	alarmCount,
	ROW_NUMBER() OVER(PARTITION BY deviceId ORDER BY alarmCount DESC) AS alarmRank
FROM t2)
SELECT * FROM t3 WHERE alarmRank<=3;
```
#### COUNT(1)和COUNT(*)的区别
1. 从执行结果来说
	+ `COUNT(1)`和`COUNT(*)`之间没有区别，因为`COUNT(*)`和`COUNT(1)`都不会去过滤空值  
	+ 但`COUNT(列名)`就有区别了，因为`COUNT(列名)`会去过滤空值  
2. 从执行效率来说
	+ 他们之间根据不同情况会有些许区别，MySQL会对`COUNT(*)`做优化  
	+ 如果列为主键，`COUNT(列名)`效率优于`COUNT(1)`  
	+ 如果列不为主键，`COUNT(1)`效率优于`COUNT(列名)`  
	+ 如果表中存在主键，`COUNT(主键列名)`效率最优  
	+ 如果表中只有一列，则`COUNT(*)`效率最优  
	+ 如果表有多列，且不存在主键，则`COUNT(1)`效率优于`COUNT(*)`  


## 统计每个月的总告警次数，总告警设备数，以及能够连续七天数值正常设备数量  
**这个指标待定，当前的表可能不适用**


## 统计出2022年1月首次出现告警的设备数量  
### 原始数据格式
|  deviceId  |  alarmTime  |
|  :----:  |  :----:  |
|  设备ID  |  告警时间  |
|  u01  |  2022/1/8/08/04/58  |
|  u02  |  2022/2/8/12/05/38  |
|  u03  |  2021/9/8/17/01/12  |
|  u01  |  2022/1/12/12/04/53  |
|  u02  |  2022/4/12/13/45/34  |
|  u01  |  2022/5/14/02/12/51  |
|  ...  |  ...  |

### 统计之后格式
|  设备ID  |  首次告警时间  |
|  :----:  |  :----:  |
|  xxx  |  2022/1/8/08/04/58  |
|  xxx  |  2022/1/8/12/05/38  |
|  xxx  |  2022/1/8/17/01/12  |
|  ...  |  ...  |

### 工作思路
1. 先用`date_format`格式化所有设备告警时间为`yyyy-MM`的日期格式  
2. 运用`min`函数得出每个设备最早告警日期
3. 当最早告警日期是`2022年1月`的时候即为我们所需要知道的设备记录  

### 工作语句
```sql
WITH t1 AS (
SELECT *,
		DATE_FORMAT(alarmTime, 'yyyy-MM') AS alarmMonth 
FROM test_01
),
t2 AS (
SELECT deviceId,
		alarmTime,
		MIN(alarmMonth) AS firstAlarmMonth
FROM t1
GROUP BY deviceId
)
SELECT * FROM t2 WHERE firstAlarmMonth='2022-1';
```


## 根据设备地区编号段对告警次数进行排序
**有一个5000万的设备信息表，一个2亿记录的告警记录表**
### 原始数据格式
1. 设备信息表  
|  deviceId  |  deviceName  |  deviceDistrict  |
|  :----:  |  :----:  |  :----:  |
|  设备ID  |  设备名称  |  设备所属地区  |
|  u01  |  xx01  |  210000  |
|  u02  |  xx02  |  210010  |
|  u03  |  xx03  |  210025  |
|  ...  |  ...  |  ...  |
2. 告警记录表  
|  deviceId  |  alarmTime  |
|  :----:  |  :----:  |
|  设备ID  |  告警时间  |
|  u01  |  2022/1/8/08/04/58  |
|  u02  |  2022/2/8/12/05/38  |
|  u03  |  2021/9/8/17/01/12  |
|  u01  |  2022/1/12/12/04/53  |
|  u02  |  2022/4/12/13/45/34  |
|  u01  |  2022/5/14/02/12/51  |
|  ...  |  ...  |

### 统计之后格式
|  设备地区号段  |  告警次数  |
|  :----:  |  :----:  |
|  210000-210010  |  2  |
|  210010-210020  |  8  |
|  210020-210030  |  4  |
|  210040-210050  |  7  |
|  ...  |  ...  |

### 工作思路
1. 先根据设备ID分组`count`统计报警次数
2. 再使用casewhen条件语句，或者使用`concat/floor/ceil`函数动态划分，根据分段统计不同设备位于什么地区号段
3. 最后连接查询，并根据地区号段，使用`sum`函数统计总告警次数即可

### 工作语句
```sql
-- 第一种方案
WITH t1 AS(
SELECT deviceId,
		COUNT(alarmTime) AS alarmCount
FROM test_01
GROUP BY deviceId
),
t2 AS(
SELECT deviceId,
		deviceDistrict,
		-- 如果地区编号是字符串可以先转换再比较，不然会触发隐式转换，导致全表扫描无法使用索引
		-- CONVERT(deviceDistrict, UNSIGNED)>=210000
		CASE WHEN deviceDistrict>=210000 AND deviceDistrict<210010 THEN '210000-210010'
		 	WHEN deviceDistrict>=210010 AND deviceDistrict<210020 THEN '210010-210020'
			WHEN deviceDistrict>=210020 AND deviceDistrict<210030 THEN '210020-210030'
		 	WHEN deviceDistrict>=210030 AND deviceDistrict<210040 THEN '210030-210040'
		 	WHEN deviceDistrict>=210040 AND deviceDistrict<210050 THEN '210040-210050'
		 	WHEN deviceDistrict>=210050 AND deviceDistrict<210060 THEN '210050-210060'
		 	WHEN deviceDistrict>=210060 AND deviceDistrict<210070 THEN '210060-210070'
		END deviceDistrictSection
FROM test_02
),
t3 AS (
	SELECT t2.deviceDistrictSection AS deviceDistrictSection,
			SUM(t1.alarmCount) AS alarmCount
	FROM t1 LEFT JOIN t2 
	ON t1.deviceId = t2.deviceId
	GROUP BY deviceDistrictSection
	ORDER BY deviceDistrictSection
)
SELECT * FROM t3;

-- 第二种方案
WITH t1 AS(
SELECT deviceId,
		COUNT(alarmTime) AS alarmCount
FROM test_01
GROUP BY deviceId
),
t2 AS(
SELECT deviceId,
		deviceDistrict,
		CONCAT(FLOOR(deviceDistrict/10)*10, '-', (FLOOR(deviceDistrict/10)+1)*10) AS deviceDistrictSection
FROM test_02
),
t3 AS (
	SELECT t2.deviceDistrictSection AS deviceDistrictSection,
			SUM(t1.alarmCount) AS alarmCount
	FROM t1 LEFT JOIN t2 
	ON t1.deviceId = t2.deviceId
	GROUP BY deviceDistrictSection
	ORDER BY deviceDistrictSection
)
SELECT * FROM t3;

-- 第二种方案的函数测试
SELECT FLOOR(210015/10)*10 AS x;  -- 210015
SELECT CEIL(210015/10)*10 AS y;  -- 210020
SELECT CONCAT(FLOOR(210015/10)*10, '-', CEIL(210015/10)*10);  -- 210010-210020
SELECT CONCAT(FLOOR(210020/10)*10, '-', CEIL(210020/10)*10);  -- 210020-210020
SELECT CONCAT(FLOOR(210020/10)*10, '-', (FLOOR(210020/10)+1)*10);  -- 210020-210030
```
#### 拼接函数`concat`/`concat_ws`/`group_concat`的区别说明
1. `concat`
	+ 将多个字符串连接成一个字符串  
	+ `concat(str1, str2,...)`  
	+ 返回结果为连接参数产生的字符串，如果有任何一个参数为null，则返回值为null  
2. `concat_ws`
	+ 和concat()一样，将多个字符串连接成一个字符串，但是可以一次性指定分隔符  
	+ 第一个参数指定分隔符，`concat_ws(separator, str1, str2, ...)`  
	+ 返回结果为连接参数产生的字符串。需要注意的是分隔符不能为null，如果为null，则返回结果为null  
3. `group_concat`
	+ 将`group by`产生的同一个分组中的值连接起来，返回一个字符串结果  
	+ `group_concat( [distinct] 要连接的字段 [order by 排序字段 asc/desc  ] [separator '分隔符'] )`  
	+ *说明：*通过使用`distinct`可以排除重复值；如果希望对结果中的值进行排序，可以使用`order by`子句；`separator`是一个字符串值，缺省为一个逗号  
#### 拼接函数`floor`/`ceil`/`round`的区别说明
1. `floor`  
	+ 在英文中，是地面，地板的意思，有下面的意思；所以此函数是向下取整，它返回的是小于或等于函数参数，并且与之最接近的整数  
	+ 向下取整的时候，正数，则取其整数部位，抹除小数部位  
	+ 负数，则取其整数加一  
	+ 整数，则不变  
2. `ceil`  
	+ 在英文中，是天花板的意思，有向上的意思；所以此函数是向上取整，它返回的是大于或等于函数参数，并且与之最接近的整数  
	+ 向上取整的时候，正数，则直接将当前整数加一  
	+ 负数，则将整数后面的数据抹除  
	+ 整数，则不变  
3. `round`  
	+ 在英文中是有大约，环绕，在某某四周，附近的意思，所以，可以取其大约的意思，在函数中是四舍五入  
	+ 四舍五入的时候，正数，小数位大于5，则整数位加一，小数位小于5，则整数位不变，抹除小数位  
	+ 负数，小数位小于5，则整数位不变，抹除小数位，小数位大于5，则整数位加一  
	+ 整数，则不变  
#### MySQL中保留两位小数
1. `round(x,d)` 四舍五入保留小数  
	+ `round(x)`其实就是`round(x,0)`，也就是默认d为0，默认不保留小数，d为保留几位小数  
	+ d可以是负数，这时是指定小数点左边的d位整数位为0，同时小数位均为0，例如：`round(114.6, -1) -> 110`  
2. `truncate(x,d)` 函数返回被舍去至小数点后d位的数字x，和`round`函数类似，但是没有四舍五入  
3. `format(x,d)` 强制保留d位小数，整数部分超过三位的时候以逗号分割，并且返回的结果是`string`类型的  
4. `convert(value,type)` 类型转换，相当于截取，例如：  
	+ convert(100.3465, decimal(10,2)) -> 100.35  
	+ convert(100, decimal(10,2)) -> 100  
	+ convert(100.4, decimal(10,2)) -> 100.4
#### Hive中保留两位小数  
1. `round(column_name,2)` 四舍五入截取 *这种方法慎用，有时候结果不是你想要的*  
2. `regexp_extract(column_name,'([0-9]*.[0-9][0-9])',1)` 正则匹配截取，不做四舍五入，只是单纯的当作字符串截取  
3. `cast(column_name as decimal(10,2))` cast函数截取 *推荐使用*  


## 统计所有告警设备和所有活跃告警设备的总数，以及平均监测值  
**活跃告警设备是指连续三天都有告警的设备**  
#### 连续N天登录等类似题目的解题思路
1. 日期减去一列数字得到的日期相等  
2. 后一个日期减去前一个日期的差值相等  

### 原始数据格式
|  deviceId  |  alarmDate  |  alarmValueAvgDaily  |
|  :----:  |  :----:  |  :----:  |
|  设备ID  |  告警日期  |  当日平均监测值  |
|  u01  |  2022-1-8  |  27  |
|  u02  |  2022-4-5  |  12  |
|  u03  |  2022-3-2  |  45  |
|  u01  |  2022-2-10  |  66  |
|  u02  |  2022-1-18  |  98  |
|  u01  |  2022-1-28  |  53  |
|  ...  |  ...  |

### 统计之后格式
|  类型  |  总数  |  总均值  |
|  :----:  |  :----:  |  :----:  |
|  所有告警设备  |  18398  |  34  |
|  活跃告警设备  |  3213  |  87  |

### 工作思路
1. 首先使用`group by`去除重复日期的重复数据，用`max`函数取最大值  
2. 然后使用`group by`去除重复设备数，分别查询设备总数和总平均值，再用左连接将查询结果拼接，保存结果查询  
3. 接着处理统计活跃告警设备，先用`row_number`函数查询分组编号，再使用`date_sub`函数用告警日期减去分组编号，得出一组临时告警日期用于判定是否是活跃告警设备  
4. 如果有连续相同日期说明是活跃告警设备，所以接着使用`count`函数和`having`条件统计过滤有大于等于三天的连续相同日期的设备与告警日期，注意同时要计算均值  
5. 左后统计活跃告警设备总数和平均值，并和第二步中的结果`union all`即可  

### 工作语句
```sql
WITH 
-- 首先去除重复日期的重复数据，这里取最大值
t1 AS(
SELECT deviceId,
		alarmDate,
		MAX(alarmValueAvgDaily) AS alarmValueAvgDaily
FROM test_03
GROUP BY deviceId, alarmDate
),
-- 去除重复设备数
t2 AS(
SELECT *
FROM t1
GROUP BY deviceId
),
-- 查询设备总数
t3 AS(
SELECT '告警设备总数与均值' AS type,
		COUNT(deviceId) AS allDeviceCount
FROM t2
),
-- 查询总均值
t4 AS(
SELECT ROUND(AVG(alarmValueAvgDaily)) AS alarmValueAvgAll
FROM t1
),
-- 查询分组后的排序编号
t5 AS(
SELECT *,
		ROW_NUMBER() OVER(PARTITION BY deviceId ORDER BY alarmDate) AS alarmDateRank
FROM t1
),
-- 查询告警日期减去分组后排序编号之后的日期，如果有连续相同的说明是连续的天数
t6 AS(
SELECT *,
		DATE_SUB(alarmDate, INTERVAL alarmDateRank DAY) AS alarmDateSub
FROM t5
),
-- 查询连续天数大于3天的设备，以及这些活跃设备的平均值
t7 AS(
SELECT deviceId,
		ROUND(AVG(alarmValueAvgDaily))  AS alarmValueAvgActive,
		alarmDateSub,
		COUNT(*) AS  alarmDateSubCount
FROM t6
GROUP BY deviceId, alarmDateSub
HAVING alarmDateSubCount>=3  
),
t8 AS(
SELECT '活跃告警设备总数与均值' AS type,
		COUNT(deviceId) AS allDeviceCount,
		ROUND(AVG(alarmValueAvgActive)) AS alarmValueAvgActiveAll
FROM t7
)
-- 统计完成所有告警设备以及平均监测值
SELECT * FROM t3 LEFT JOIN t4 ON t4.alarmValueAvgAll IS NOT NULL
UNION ALL
-- 统计完成活跃告警设备以及平均监测值
SELECT * FROM t8;
```
#### 合并操作符`union`和`union all`之间的区别
1. 相同之处
	+ 都是用于合并两个或多个`select`语句的结果组合成单个结果集  
	+ 操作符内部的每个`select`语句必须拥有相同数量的，列也必须拥有相似的数据类型，同时每个`select`语句中的列的顺序必须相同  
2. 不同之处
	+ 对重复结果的处理：`union`在进行表连接后会筛选掉重复的记录，`union all`不会去除重复记录  
	+ 对排序的处理：`union`将会按照字段的顺序进行排序，`union all`只是简单的将两个结果合并后就返回  
	+ 从效率上说，`union all`要比 `union`快很多，所以，如果可以确认合并的两个结果集中不包含重复数据且不需要排序时的话，那么就使用`union all`  
#### Hive和MySQL中的日期函数
1. [MySQL Date 函数](https://www.runoob.com/sql/sql-dates.html)、[MySQL 日期函数](https://www.runoob.com/mysql/mysql-functions.html)  
2. [【hive 日期函数】Hive常用日期函数整理](https://blog.csdn.net/u013421629/article/details/80450047)  
3. *后期切记整理链接资料，若忘记请读者提醒！！！感谢！！！*


## 统计2022年1月8日下午16点-17点，每个接口调用量top10的ip地址  
### 原始数据格式
|  time  |  interface  |  ip  |
|  :----:  |  :----:  |  :----:  |
|  时间  |  接口  |  访问IP  |
|  2021/1/8 15:01:28  |  /api/user/login  |  110.25.3.56  |
|  2021/1/8 15:21:12  |  /api/device/alarm  |  23.21.33.87  |
|  2021/1/8 15:51:34  |  /api/device/record  |  45.76.21.543  |
|  ...  |  ...  |

### 统计之后格式
|  接口  |  访问IP  |  访问次数  |  排名  |
|  :----:  |  :----:  |  :----:  |  :----:  |
|  /api/user/login  |  110.25.3.56  |  89  |  1  |
|  /api/device/alarm  |  23.21.33.87  |  123  |  1  |
|  /api/device/record  |  45.76.21.543  |  23  |  1  |
|  ...  |  ...  |  ...  |  ...  |

#### 此题作为开放题供大家查阅，后面有空再继续写  


## 附录资料
### Hive和MySQL中部分函数的区别
1. date_format()  
	+ Hive `date_format(date date / timestamp time / string 'xxxx-xx-xx', format 'yyyy-MM-dd')`，只能识别用`-`连接的日期字符串  
	+ MySQL `date_format(date, format)`，具体的format规则请查询[参考资料](http://c.biancheng.net/mysql/date_format.html)  
2. date_sub()  
	+ Hive `date_sub(date date / timestamp time, int days)`  
	+ MySQL `date_sub(date, interval 时间间隔 type)`，具体的type规则请查询[参考资料](https://www.runoob.com/sql/func-date-sub.html)  

### Hive和MySQL常用日期函数
1. `date_add()` 向日期添加指定的时间间隔  
2. `date_sub()` 从日期减去指定的时间间隔  
3. `datediff()` 返回两个日期之间的天数  

### Hive中`order by`/`distribute by`/`sort by`/`group by`/`partition by`之间的区别说明  
1. order by  
	+ `order by`会对数据进行全局排序，和oracle、mysql等数据库中的`order by`效果一样  
	+ 需要注意的是，hive执行过程中它只在一个`reduce`中进行，所以数据量特别大的时候效率非常低  
	+ `group by`分组之后是会组内聚合的，而`distribute by`和`partition by`仅仅是分组了，并未有聚合操作  
2. distribute by  
	+ `distribute by`是控制在`map`端如何拆分数据给`reduce`端的  
	+ hive会根据`distribute by`后面列，对应`reduce`的个数进行分发，默认是采用`hash算法`  
3. sort by  
	+ `sort by`为每个`reduce`产生一个排序文件      
	+ 在有些情况下，你需要控制某个特定行应该到哪个`reducer`，这通常是为了进行后续的聚集操作`distribute by`刚好可以做这件事  
	+ 因此，`distribute by`经常和`sort by`配合使用  
4. group by  
	+ 和`distribute by`类似 都是按`key值`划分数据 都使用`reduce`操作
	+ 唯一不同的是，`distribute by`只是单纯的分散数据，`distribute by col` 是按照`col列`把数据分散到不同的`reduce`  
	+ 而`group by`把相同`key值`的数据聚集到一起，后续必须是聚合操作  
5. cluster by  
	+  按列分桶建表使用  
	+ `distribute by` 和 `sort by` 合用就相当于`cluster by`，但是`cluster by`不能指定排序为`asc(升序)`或`desc(倒序)`的规则，只能是升序排列  
6. partition by  
	+ 按所分区名分区建表使用  
	+ 通常查询时会对整个数据库查询，而这带来了大量的开销，因此引入了`partition`的概念  
	+ 在建表的时候通过设置`partition`的字段，会根据该字段对数据分区存放，更具体的说是存放在不同的文件夹  
	+ 这样通过指定设置`partition`的字段条件查询时可以减少大量的开销  
	+ 区内排序用`order by`

### MySQL多表查询时如何将`NULL`置为`0`
使用IFNULL`("字段", 0)`函数即可  

### Hive中如何处理NULL值和空字符串
1. Hive表中默认将`NULL`存为`\N`，可查看表的源文件（`hadoop fs -cat`或者`hadoop fs -text`），文件中存储大量`\N`，这样造成浪费大量空间  
2. 但Hive的`NULL`有时候是必须的
	+ Hive中`insert`语句必须列数匹配，不支持不写入，没有值的列必须使用`NULL`占位  
	+ Hive表的数据文件中按分隔符区分各个列，空列会保存`NULL（\n）`来保留列位置，
		但外部表加载某些数据时如果列不够，如表13列，文件数据只有2列，则在表查询时表中的末尾剩余列无数据对应，自动显示为`NULL`  
3. 所以，NULL转化为空字符串，可以节省磁盘空间
	+ 建表时直接指定
	```
	# 第一种方式
	ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'  
	WITH SERDEPROPERTIES ('serialization.null.format' = '')  
	# 第二种方式
	 ROW FORMAT DELIMITED NULL DEFINED AS ''  
	```
	+ 修改已存在的表
	```
	ALTER TABLE hive_tb SET SERDEPROPERTIES('serialization.null.format' = '');
	```
4. 使用函数处理NULL值
	+ `NVL(expr1,expr2)` 如果第一个参数为`NULL`那么显示第二个参数的值，如果第一个参数的值不为`NULL`，则显示第一个参数本来的值  
	+ `Coalesce(expr1, expr2, expr3….. exprn)` 返回表达式中第一个非空表达式，如果所有自变量均为`NULL`，则 COALESCE 返回`NULL`  
	```
	SELECT COALESCE(NULL,null,3,4,5); 　　-- 结果为：3
	SELECT COALESCE(NULL,null,'',3,4,5);   -- 结果为：''
	SELECT COALESCE(NULL,null,null,NULL);  -- 结果为：null
	```


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hive/hive-sql-inwork.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***