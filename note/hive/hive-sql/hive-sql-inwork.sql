-- 去掉 sql_mode 的 ONLY_FULL_GROUP_BY
set global sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'; 
set session sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- 创建测试用数据库表
USE hive_test;

DROP TABLE hive_test.test_00;

CREATE TABLE IF NOT EXISTS `test_00`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `deviceId` VARCHAR(100) NOT NULL,
   `alarmDate` VARCHAR(100) NOT NULL,
   `alarmCount` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_00 
( id, deviceId, alarmDate, alarmCount)
VALUES
( 1, "u01", "2022/01/08", 15),
( 2, "u01", "2022/02/03", 2),
( 3, "u01", "2022/02/05", 4),
( 4, "u01", "2022/02/07", 1),
( 5, "u01", "2022/03/12", 8),
( 6, "u02", "2022/01/03", 34),
( 7, "u02", "2022/02/03", 13),
( 8, "u03", "2022/02/03", 3),
( 9, "u03", "2022/01/03", 1),
( 10, "u03", "2021/05/03", 2);

SELECT * FROM test_00;

-- 统计每个用户的累计访问次数
-- 第一种方案
SELECT *,
	   SUM(sumPart) OVER (PARTITION BY deviceId ORDER BY alarmMonth) AS sumAll
FROM
(SELECT deviceId,
	   SUBSTR(alarmDate,1,7) AS alarmMonth,
	   SUM(alarmCount) AS sumPart
FROM test_00
GROUP BY deviceId,
		 alarmMonth) t;
-- 第二种方案
SELECT *,
	   SUM(sumPart) OVER (PARTITION BY deviceId ORDER BY alarmMonth) AS sumAll
FROM
(SELECT deviceId,
	    DATE_FORMAT(REGEXP_REPLACE(alarmDate,'/','-'), 'yyyy-MM') AS alarmMonth,
	    SUM(alarmCount) AS sumPart
FROM test_00
GROUP BY deviceId,
		 alarmMonth) t;


-- 创建测试用数据库表
USE hive_test;

DROP TABLE hive_test.test_01;

CREATE TABLE IF NOT EXISTS `test_01`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `deviceId` VARCHAR(100) NOT NULL,
   `alarmTime` VARCHAR(100) NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_01 
( id, deviceId, alarmTime)
VALUES
( 1, "u01", "2022/1/8/01/01/58"),
( 2, "u01", "2022/1/8/01/01/58"),
( 3, "u01", "2022/1/8/01/01/58"),
( 4, "u01", "2022/1/12/03/02/48"),
( 5, "u01", "2022/1/12/04/03/58"),
( 6, "u01", "2022/1/14/05/04/28"),
( 7, "u01", "2022/1/13/01/01/28"),
( 8, "u01", "2022/1/13/02/01/38"),
( 9, "u01", "2022/1/12/02/01/38"),
( 10, "u03", "2020/12/17/04/03/58"),
( 11, "u03", "2022/1/17/05/04/28"),
( 12, "u02", "2022/1/14/02/01/38"),
( 13, "u03", "2022/1/17/03/02/48"),
( 14, "u04", "2021/12/12/04/03/58"),
( 15, "u04", "2022/1/11/05/04/28"),
( 16, "u04", "2022/1/10/06/03/58"),
( 17, "u04", "2022/1/13/07/04/28"),
( 18, "u04", "2022/1/13/08/03/58"),
( 19, "u04", "2022/1/13/09/04/28"),
( 20, "u04", "2022/1/13/07/04/28"),
( 21, "u04", "2022/1/1/08/03/58"),
( 22, "u04", "2022/1/1/09/04/28"),
( 23, "u02", "2022/1/14/05/04/28"),
( 24, "u03", "2022/1/13/01/01/28"),
( 25, "u03", "2022/1/13/02/01/38"),
( 26, "u03", "2022/1/12/02/01/38"),
( 27, "u03", "2022/1/12/04/03/58"),
( 28, "u03", "2022/1/11/05/04/28"),
( 29, "u03", "2022/1/10/06/03/58"),
( 30, "u05", "2022/4/13/01/01/28"),
( 31, "u06", "2022/2/13/02/01/38"),
( 32, "u07", "2021/1/12/02/01/38"),
( 33, "u08", "2022/1/12/04/03/58"),
( 34, "u09", "2022/3/11/05/04/28"),
( 35, "u10", "2022/7/11/05/04/28");

SELECT * FROM test_01;

-- 统计环境设备的总污染告警次数
SELECT deviceId,
		COUNT(alarmTime) AS alarmCount 
FROM
(SELECT deviceId,
		DATE_FORMAT(alarmTime, '%Y-%c-%d %T') AS alarmTime
FROM test_01
GROUP BY deviceId,alarmTime
ORDER BY alarmTime) t
GROUP BY deviceId;

-- 统计每个设备告警次数排名前三的日期（需要mysql8）
SELECT * 
FROM t3
WHERE alarmRank<=3;

-- 统计每个设备告警次数排名前三的日期（使用WITH语句优化一下）
WITH t1 AS (
SELECT deviceId,
	DATE_FORMAT(alarmTime, '%Y-%c-%d') AS alarmDate,
	DATE_FORMAT(alarmTime, '%Y-%c-%d %T') AS alarmTime
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


-- 统计出2022年1月首次出现告警的设备数量
WITH t1 AS (
SELECT *,
		DATE_FORMAT(alarmTime, '%Y-%c') AS alarmMonth 
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


-- 创建测试用数据库表
USE hive_test;

DROP TABLE hive_test.test_02;

CREATE TABLE IF NOT EXISTS `test_02`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `deviceId` VARCHAR(100) NOT NULL,
   `deviceName` VARCHAR(100) NOT NULL,
   `deviceDistrict` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_02 
( id, deviceId, deviceName, deviceDistrict)
VALUES
( 1, "u01", "xx01", 210000),
( 2, "u02", "xx02", 210010),
( 3, "u03", "xx03", 210026),
( 4, "u04", "xx04", 210047),
( 5, "u05", "xx05", 210063),
( 6, "u06", "xx06", 210030),
( 7, "u07", "xx07", 210032),
( 8, "u08", "xx08", 210020),
( 9, "u09", "xx09", 210066),
( 10, "u10", "xx10", 210040);

SELECT * FROM test_02;


-- 根据设备地区号段对告警次数进行排序
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


-- 创建测试用数据库表
USE hive_test;

DROP TABLE hive_test.test_03;

CREATE TABLE IF NOT EXISTS `test_03`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `deviceId` VARCHAR(100) NOT NULL,
   `alarmDate` VARCHAR(100) NOT NULL,
   `alarmValueAvgDaily` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_03 
( id, deviceId, alarmDate, alarmValueAvgDaily)
VALUES
( 1, "u01", "2022-01-08", 135),
( 2, "u01", "2022-01-08", 134),
( 3, "u01", "2022-02-05", 74),
( 4, "u01", "2022-02-06", 61),
( 5, "u01", "2022-02-07", 38),
( 6, "u02", "2022-01-03", 34),
( 7, "u02", "2022-01-04", 133),
( 8, "u03", "2022-01-03", 73),
( 9, "u03", "2022-02-04", 61),
( 10, "u03", "2022-01-04", 228),
( 11, "u03", "2022-02-05", 128),
( 12, "u03", "2022-01-06", 228),
( 13, "u03", "2022-02-06", 238),
( 14, "u03", "2022-02-06", 239),
( 15, "u04", "2022-01-03", 23),
( 16, "u04", "2022-01-04", 26),
( 17, "u04", "2022-01-05", 39),
( 18, "u04", "2022-01-06", 41),
( 19, "u04", "2022-04-03", 38),
( 20, "u04", "2022-05-03", 89);

SELECT * FROM test_03;

-- 统计所有告警设备和所有活跃告警设备的总数，以及平均监测值
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