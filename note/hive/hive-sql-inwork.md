# HiveSQL工作记录 🕹️0.2.0  

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

### 统计之后格式
|  设备ID  |  告警月份  |  告警次数小计  |  告警次数累计  |
|  :----:  |  :----:  |  :----:  |  :----:  |
|  u01  |  2022-02  |  11  |  11  |
|  u01  |  2022-03  |  12  |  23  |

### 工作思路
1. 先根据设备ID和告警日期分组
2. 按月份统计可以用substr函数或者日期格式化函数
3. 再统计即可得出小计告警次数
4. 接着使用聚合窗口函数计算累计告警次数

### 工作语句
1. 第一种写法，使用`substr`截取字符串函数
```sql
SELECT *,
	   SUM(sumPart) over (PARTITION BY deviceId ORDER BY alarmMonth ROWS BETWEEN UNBOUNDED AND CURRENT ROW) AS sumAll
FROM
(SELECT deviceId,
	   substr(alarmDate,0,6) as alarmMonth,
	   SUM(alarmCount) as sumPart
FROM test
GROUP BY deviceId,
		 substr(alarmDate,0,6);)
```
2. 第二种写法，使用日期格式化函数
```sql
SELECT *,
	   SUM(sumPart) over (PARTITION BY deviceId ORDER BY alarmMonth ROWS BETWEEN UNBOUNDED AND CURRENT ROW) AS sumAll
FROM
(SELECT deviceId,
	    DATE_FORMAT(REGEXP_REPLACE(alarmDate,'/','-'), 'yyyy-MM') as alarmMonth,
	    SUM(alarmCount) as sumPart
FROM test
GROUP BY deviceId,
		 substr(alarmDate,0,6);)
```
#### 开窗函数中的界限说明
+ unbounded：无界限  
+ preceding：从分区第一行头开始，则为 `unbounded` *N为：相对当前行向后的偏移量*  
+ following ：与`preceding`相反,到该分区结束，则为 `unbounded` *N为：相对当前行向后的偏移量*  
+ current row：顾名思义，当前行，偏移量为0  


## 统计环境设备的污染告警次数，并输出告警次数前三的设备信息，包括设备名称，设备id，告警次数

## 指定条件下的筛选
### 统计每个月的告警次数，数值异常高频、中频、低频，**再想一个指标**
### 统计出2022年1月首次出现告警的设备数量