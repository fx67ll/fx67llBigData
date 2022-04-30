# HiveSQL工作记录 🕹️0.4.0  

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

### 工作思路以及语句
#### 统计环境设备的总污染告警次数
1. 由于有设备可能会有同一时间的告警记录，所以需要按告警时间去重后再统计
2. 如果使用distinct去重，如果表数据过大，且设备ID差异化很大，那么会有性能压力  
3. 所以使用group by子查询代替  
4. mysql中的`date_format`格式化需要这样写：`DATE_FORMAT(alarmTime, '%Y-%c-%d %T')`  
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
1. 使用窗口函数`ROW_NUMBER() OVER()`进行分组排序即可，[MySQL 替换 ROW_NUMBER() OVER (PARTITION ……) 函数](https://blog.csdn.net/liuyaru11/article/details/106685366)  
2. 多个子句查询可以使用视图和`WITH`语句 
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
|  deviceDistrictSection  |  alarmCount  |
|  :----:  |  :----:  |
|  设备地区号段  |  告警次数  |
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


## 统计所有告警设备和所有活跃告警设备的总数，以及平均监测值  
**活跃告警设备是指连续七天都有告警的设备**  
#### 连续N天登录等类似题目的解题思路
1. 日期减去一列数字得到的日期相等  
2. 后一个日期减去前一个日期的差值相等  

### 原始数据格式
|  deviceId  |  alarmDate  |  alarmValue  |
|  :----:  |  :----:  |  :----:  |
|  设备ID  |  告警日期  |  监测值  |
|  u01  |  2022-1-8  |  27  |
|  u02  |  2022-4-5  |  12  |
|  u03  |  2022-3-2  |  45  |
|  u01  |  2022-2-10  |  66  |
|  u02  |  2022-1-18  |  98  |
|  u01  |  2022-1-28  |  53  |
|  ...  |  ...  |

### 统计之后格式
|  类型  |  总数  |  平均值  |
|  :----:  |  :----:  |  :----:  |
|  所有告警设备  |  18398  |  34  |
|  活跃告警设备  |  3213  |  87  |