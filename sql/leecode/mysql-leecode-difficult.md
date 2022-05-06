# MySQL困难练习题汇总 🕹️0.2.0

整理一些力扣上的困难级别MySQL练习题供大家学习参考使用

## department-top-three-salaries（部门工资前三高的所有员工）
找出每个部门中收入前三高的员工
### 查询数据
1. Employee 表:
	```
	+----+-------+--------+--------------+
	| id | name  | salary | departmentId |
	+----+-------+--------+--------------+
	| 1  | Joe   | 85000  | 1            |
	| 2  | Henry | 80000  | 2            |
	| 3  | Sam   | 60000  | 2            |
	| 4  | Max   | 90000  | 1            |
	| 5  | Janet | 69000  | 1            |
	| 6  | Randy | 85000  | 1            |
	| 7  | Will  | 70000  | 1            |
	+----+-------+--------+--------------+
	```
2. Department  表:
	```
	+----+-------+
	| id | name  |
	+----+-------+
	| 1  | IT    |
	| 2  | Sales |
	+----+-------+
	```
### 输出数据
	```
	+------------+----------+--------+
	| Department | Employee | Salary |
	+------------+----------+--------+
	| IT         | Max      | 90000  |
	| IT         | Joe      | 85000  |
	| IT         | Randy    | 85000  |
	| IT         | Will     | 70000  |
	| Sales      | Henry    | 80000  |
	| Sales      | Sam      | 60000  |
	+------------+----------+--------+
	```
#### 题解：
```sql
WITH t1 AS(
SELECT d.name AS Department,
    e.name AS Employee,
    e.salary AS Salary,
    DENSE_RANK() OVER(PARTITION BY d.name ORDER BY e.salary DESC) AS salaryRank
FROM Employee e LEFT JOIN Department d
ON e.departmentId = d.id
)
SELECT Department,
        Employee,
        Salary
FROM t1
WHERE salaryRank <=3;
```


## game-play-analysis-v（游戏玩法分析 V）
1. 玩家的 安装日期 定义为该玩家的第一个登录日  
2. 玩家的 第一天留存率 定义为：
	假定安装日期为 X 的玩家的数量为 N ，其中在 X 之后的一天重新登录的玩家数量为 M ，M/N 就是第一天留存率，四舍五入到小数点后两位  
3. 编写一个 SQL 查询，报告所有安装日期、当天安装游戏的玩家数量和玩家的第一天留存率  
### 查询数据
Activity 表：
```
+-----------+-----------+------------+--------------+
| player_id | device_id | event_date | games_played |
+-----------+-----------+------------+--------------+
| 1         | 2         | 2016-03-01 | 5            |
| 1         | 2         | 2016-03-02 | 6            |
| 2         | 3         | 2017-06-25 | 1            |
| 3         | 1         | 2016-03-01 | 0            |
| 3         | 4         | 2016-07-03 | 5            |
+-----------+-----------+------------+--------------+
```
### 输出数据
Result 表：
```
+------------+----------+----------------+
| install_dt | installs | Day1_retention |
+------------+----------+----------------+
| 2016-03-01 | 2        | 0.50           |
| 2017-06-25 | 1        | 0.00           |
+------------+----------+----------------+
```
#### 题解
```sql
WITH t1 AS(
SELECT player_id,
        MIN(event_date) AS install_dt
FROM Activity
GROUP BY player_id
)
SELECT t1.install_dt,
        COUNT(*) AS installs,
        ROUND(COUNT(t2.event_date)/COUNT(*), 2) AS Day1_retention
FROM t1 
LEFT JOIN Activity t2
ON t1.player_id = t2.player_id
AND DATEDIFF(t2.event_date, t1.install_dt) = 1
GROUP BY t1.install_dt;
-- 窗口函数替代写法 GROUP BY 分组写法
-- MIN(event_date) OVER(PARTITION BY player_id) AS install_dt
```
```sql
-- 不知道自己思路错在哪里，找个大佬看看
WITH t1 AS(
SELECT player_id,
        MIN(event_date) AS install_dt
FROM Activity
GROUP BY player_id
ORDER BY player_id
),
t2 AS(
SELECT install_dt,
        COUNT(player_id) AS installs
FROM t1
GROUP BY install_dt
ORDER BY install_dt
),
t3 AS(
SELECT player_id,
        event_date,
        ROW_NUMBER() OVER(PARTITION BY player_id ORDER BY event_date) AS date_rank
FROM Activity
),
t4 AS(
SELECT *,
        DATE_SUB(event_date, INTERVAL date_rank DAY) AS date_temp
FROM t3
),
t5 AS(
SELECT *,
        COUNT(*) AS date_temp_count
FROM t4
GROUP BY player_id,date_temp
HAVING date_temp_count >= 2
),
t6 AS(
SELECT event_date AS install_dt_stay,
        COUNT(player_id) AS installs_stay
FROM t5
GROUP BY install_dt_stay
ORDER BY install_dt_stay
)
SELECT t2.install_dt AS install_dt,
        t2.installs AS installs,
        ROUND(IFNULL(t6.installs_stay/t2.installs, 0), 2) AS Day1_retention
FROM t2 LEFT JOIN t6
ON t2.install_dt = t6.install_dt_stay
ORDER BY install_dt;
```


**示例**
## title（中文标题）
### 查询数据
### 输出数据
#### 题解

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/sql/leecode/mysql-difficult.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***