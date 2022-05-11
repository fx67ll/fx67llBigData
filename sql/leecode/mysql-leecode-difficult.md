# MySQL困难练习题汇总 🕹️0.5.0

整理一些力扣上的困难级别MySQL练习题供大家学习参考使用
#### 目录
1. department-top-three-salaries（部门工资前三高的所有员工）  
2. game-play-analysis-v（游戏玩法分析 V）  
3. human-traffic-of-stadium（体育馆的人流量）  
4. median-employee-salary（员工薪水中位数）  

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
### 测试数据
```sql
DROP TABLE leecode_test.test_employee;
DROP TABLE leecode_test.test_department;

CREATE TABLE IF NOT EXISTS `test_employee`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `name` VARCHAR(100) NOT NULL,
   `salary` INT NOT NULL,
   `departmentId` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_employee 
( id, name, salary, departmentId)
VALUES
( 1, "Joe", 85000, 1),
( 2, "Henry", 80000, 2),
( 3, "Sam", 60000, 2),
( 4, "Max", 90000, 1),
( 5, "Janet", 69000, 1),
( 6, "Randy", 85000, 1),
( 7, "Will", 70000, 1);

CREATE TABLE IF NOT EXISTS `test_department`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `name` VARCHAR(100) NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_department 
( id, name)
VALUES
( 1, "IT"),
( 2, "Sales");

SELECT * FROM test_employee;
SELECT * FROM test_department;
```
### 解题思路
1. 通过`dense_rank`函数查询按部门分组排序后的排名
2. 取排名前三的即可
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
#### Hive和MySQL中显示分区中的当前行号排序的窗口函数
1. `ROW_NUMBER()` 不管排名是否有相同的，都按照顺序，例如：1，2，3...n  
2. `RANK()` 排名相同的名次一样，同一排名有几个，后面排名就会跳过几次，例如：1，1，3...n  
3. `DENSE_RANK()` 排名相同的名次一样，且后面名次不跳跃，例如：1，1，2...n  


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
```
+------------+----------+----------------+
| install_dt | installs | Day1_retention |
+------------+----------+----------------+
| 2016-03-01 | 2        | 0.50           |
| 2017-06-25 | 1        | 0.00           |
+------------+----------+----------------+
```
### 测试数据
```sql
CREATE TABLE IF NOT EXISTS `test_activity`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `player_id` INT NOT NULL,
   `device_id` INT NOT NULL,
   `event_date` VARCHAR(100) NOT NULL,
   `games_played` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_activity 
( id, player_id, device_id, event_date, games_played)
VALUES
( 1, 1, 2, "2016-03-01", 5),
( 2, 1, 2, "2016-03-02", 6),
( 3, 2, 3, "2017-06-25", 1),
( 4, 3, 1, "2016-03-01", 0),
( 5, 3, 4, "2016-07-03", 5);

SELECT * FROM test_activity;
```
### 解题思路
1. 先使用`min`函数找出安装日期  
2. 上一个结果表和活动表左连接，找出符合条件：玩家id相同，且与安装日期差一天的玩家和日期记录  
3. 用安装日期分组，并计算总安装数，和每个日期的留存率  
4. 留存率的计算使用`count`函数统计符合条件的记录数，除以总安装数即可  
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


## human-traffic-of-stadium（体育馆的人流量）
### 查询数据
Stadium 表:
```
+------+------------+-----------+
| id   | visit_date | people    |
+------+------------+-----------+
| 1    | 2017-01-01 | 10        |
| 2    | 2017-01-02 | 109       |
| 3    | 2017-01-03 | 150       |
| 4    | 2017-01-04 | 99        |
| 5    | 2017-01-05 | 145       |
| 6    | 2017-01-06 | 1455      |
| 7    | 2017-01-07 | 199       |
| 8    | 2017-01-09 | 188       |
+------+------------+-----------+
```
### 输出数据
```
+------+------------+-----------+
| id   | visit_date | people    |
+------+------------+-----------+
| 5    | 2017-01-05 | 145       |
| 6    | 2017-01-06 | 1455      |
| 7    | 2017-01-07 | 199       |
| 8    | 2017-01-09 | 188       |
+------+------------+-----------+
解释：
id 为 5、6、7、8 的四行 id 连续，并且每行都有 >= 100 的人数记录。
请注意，即使第 7 行和第 8 行的 visit_date 不是连续的，输出也应当包含第 8 行，因为我们只需要考虑 id 连续的记录。
不输出 id 为 2 和 3 的行，因为至少需要三条 id 连续的记录。
```
### 测试数据
```sql
CREATE TABLE IF NOT EXISTS `test_stadium`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `visit_date` VARCHAR(100) NOT NULL,
   `people` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_stadium 
( id, visit_date, people)
VALUES
( 1, "2017-01-01", 10),
( 2, "2017-01-02", 109),
( 3, "2017-01-03", 150),
( 4, "2017-01-04", 99),
( 5, "2017-01-05", 145),
( 6, "2017-01-06", 1455),
( 7, "2017-01-07", 199),
( 8, "2017-01-09", 188);

SELECT * FROM test_stadium;
```
### 解题思路
1. 先使用`where`条件找出人数大于等于100的记录  
2. 再使用每行记录`id`，减去每行记录的序号，得出`临时列(id_temp)`  
3. 用临时列分组，统计连续相同的记录列的`id`数量，大于等于3就是我们需要的`临时列(id_temp)`  
4. 最后再使用`where in 找出的临时列id_temp`，查询相应数据即可  
#### 题解
```sql
WITH t1 AS(
SELECT *,
		(id - ROW_NUMBER() OVER()) AS id_temp
FROM test_stadium
WHERE people >= 100
),
t2 AS(
SELECT id_temp
FROM t1
GROUP BY id_temp
HAVING COUNT(id) >= 3
),
t3 AS(
SELECT *
FROM t1
WHERE id_temp IN (SELECT * FROM t2)
)
SELECT id,
        visit_date,
        people
FROM t3;
```
#### Hive和MySQL中的 WHERE...IN/NOT IN... 用法详解
1. 需要注意的是，Hive中`wherein`是隐藏了`is not null`的条件，所以如果有空值存在的话，`wherein`和`wherenotin`并不能查出所有记录数  
2. 基本使用方式：
	```sql
	WHERE column IN (value1,value2,...)  
	WHERE column NOT IN (value1,value2,...)  
	```
3. `in`后面可以是
	+ 记录集，*注意在子查询中返回的结果必须是一个字段列表项*  
	```sql
	SELECT * FROM table1 WHERE id IN(SELECT id FROM table2 WHERE sex=0);
	```
	+ 字符串，`in`列表项不仅支持数字，也支持字符甚至时间日期类型等，并且可以将这些不同类型的数据项混合排列而无须跟`column`的类型保持一致  
	```sql
	SELECT * FROM table WHERE name IN('aaa','bbb','ccc','ddd','eee','ffff'');
	SELECT * FROM table WHERE id IN(1,2,'3','c');
	```
	+ 数组  
4. 一个`in`只能对一个字段进行范围比对，如果要指定更多字段，可以使用`and`或`or`逻辑运算符，
	使用`and`或`or`逻辑运算符后，`in`还可以和其他如`like、>=、=`等运算符一起使用  
	```sql
	SELECT * FROM table WHERE id IN(1,2) OR name IN('admin','fx67ll');
	```
5. 如果`in`的列表项是确定的，那么可以用多个`or`来代替，一般认为，如果是对索引字段进行操作，使用`or`效率高于`in`，
	但对于列表项不确定的时候（如需要子查询得到结果），就必须使用`in`运算符。另外，对于子查询表数据小于主查询的时候，也是适用`in`运算符的  4
	```sql
	SELECT * FROM table WHERE id IN (2,3,5)
	// 等效为：
	SELECT * FROM table WHERE (id=2 OR id=3 OR id=5)
	```
#### 类似子句`WHERE...BETWEEN...`的用法  
1. 选取介于两个值之间的数据范围  
2. 基本使用方式：
	```sql
	WHERE column BETWEEN value1 AND value2;
	WHERE column NOT BETWEEN value1 AND value2;
	```
3. 在`MySQL`中，`between`包含了`value1`和`value2`边界值，请检查你的数据库是如何处理`between`边界值的  
#### 查询函数`FIND_IN_SET(str,strlist)`
1. 它不同于`like`模糊查询，它是以`,`来分隔值  
2. `str`要查询的字符串，`strlist`查询字段名参数以`,`分隔，如`(1,2,6,8)`，查询字段`strlist`中包含`str`的结果，返回结果为`null`或记录  
3. 示例：
	```sql
	// 查询column字段中包含 1 这个参数的记录  
	SELECT * FROM table WHERE FIND_IN_SET('1',column);
	```
	

## median-employee-salary（员工薪水中位数）
### 查询数据
Employee 表:
```
+----+---------+--------+
| id | company | salary |
+----+---------+--------+
| 1  | A       | 2341   |
| 2  | A       | 341    |
| 3  | A       | 15     |
| 4  | A       | 15314  |
| 5  | A       | 451    |
| 6  | A       | 513    |
| 7  | B       | 15     |
| 8  | B       | 13     |
| 9  | B       | 1154   |
| 10 | B       | 1345   |
| 11 | B       | 1221   |
| 12 | B       | 234    |
| 13 | C       | 2345   |
| 14 | C       | 2645   |
| 15 | C       | 2645   |
| 16 | C       | 2652   |
| 17 | C       | 65     |
+----+---------+--------+
```
### 输出数据
```
+----+---------+--------+
| id | company | salary |
+----+---------+--------+
| 5  | A       | 451    |
| 6  | A       | 513    |
| 12 | B       | 234    |
| 9  | B       | 1154   |
| 14 | C       | 2645   |
+----+---------+--------+
```
### 测试数据
```sql
CREATE TABLE IF NOT EXISTS `test_company`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `company` VARCHAR(100) NOT NULL,
   `salary` INT NOT NULL,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO test_company 
( id, company, salary)
VALUES
( 1, "A", 2341),
( 2, "A", 341),
( 3, "A", 15),
( 4, "A", 15341),
( 5, "A", 451),
( 6, "A", 513),
( 7, "B", 15),
( 8, "B", 13),
( 9, "B", 1154),
( 10, "B", 1345),
( 11, "B", 1221),
( 12, "B", 234),
( 13, "C", 2345),
( 14, "C", 2645),
( 15, "C", 2645),
( 16, "C", 2652),
( 17, "C", 65);

SELECT * FROM test_company;
```
### 解题思路
1. 查出按公司分组后的记录总数  
2. 使用IN子句或BETWEEN子句找出中位数即可  
#### 题解
```sql
-- 外连接查询
WITH t1 AS(
SELECT *,
        ROW_NUMBER() OVER(PARTITION BY company ORDER BY salary) AS temp_rank
FROM test_company
),
t2 AS(
SELECT *,
		COUNT(*) AS count_rank
FROM t1
GROUP BY company 
)
SELECT t1.id,
		t1.company,
		t1.salary
FROM t1 RIGHT JOIN t2
ON t1.company = t2.company
AND t1.temp_rank BETWEEN t2.count_rank/2 AND t2.count_rank/2 + 1;
-- AND t1.temp_rank IN (count_rank/2, count_rank/2+1, count_rank/2+0.5);

-- 内连接查询
WITH t1 AS(
SELECT *,
        ROW_NUMBER() OVER(PARTITION BY company ORDER BY salary) AS temp_rank
FROM test_company
),
t2 AS(
SELECT *,
		COUNT(*) AS count_rank
FROM t1
GROUP BY company 
)
SELECT t1.id,
		t1.company,
		t1.salary
FROM t1, t2
WHERE t1.company = t2.company
AND t1.temp_rank BETWEEN t2.count_rank/2 AND t2.count_rank/2 + 1;
-- AND t1.temp_rank IN (count_rank/2, count_rank/2+1, count_rank/2+0.5);

-- 优化一下单表IN子句和BETWEEN子句
WITH t1 AS(
SELECT *,
        ROW_NUMBER() OVER(PARTITION BY company ORDER BY salary) AS temp_rank,
        COUNT(salary) OVER(PARTITION BY company) AS count_rank
FROM test_company
)
SELECT id,company,salary 
FROM t1 WHERE temp_rank IN (count_rank/2, count_rank/2+1, count_rank/2+0.5);
-- FROM t1 WHERE temp_rank BETWEEN count_rank/2 AND count_rank/2 + 1;

-- 使用自定义参数实现窗口函数row_number
WITH t1 AS(
SELECT table_test.*,
		(@i:=CASE WHEN @company_pre=table_test.company THEN @i+1 ELSE 1 END) AS temp_rank,
		(@company_pre:=company) AS company_rank
FROM test_company AS table_test, 
(SELECT @i:=0, @company_pre:='') AS table_temp
GROUP BY company,id
ORDER BY company,salary DESC
),
t2 AS(
SELECT *,
		COUNT(*) AS count_company
FROM t1
GROUP BY company
)
SELECT t1.id,
		t1.company,
		t1.salary
FROM t1, t2
WHERE t1.company = t2.company
AND t1.temp_rank BETWEEN t2.count_company/2 AND t2.count_company/2 + 1;
```
#### `MySQL@5.7`自定义实现`MySQL@8.0`窗口函数`ROW_NUMBER() OVER()`  
1. row_number
	```sql
	-- 直接使用上方案例表
	SELECT *, ROW_NUMBER() OVER(PARTITION BY company ORDER BY salary) AS temp_rank
	FROM test_company
	```
2. 自定义实现
	```sql
	SELECT table_test.*,
			(@i:=CASE WHEN @company_pre=table_test.company THEN @i+1 ELSE 1 END) AS temp_rank,
			(@company_pre:=company) AS company_rank
	FROM test_company AS table_test, 
	(SELECT @i:=0, @company_pre:='') AS table_temp
	GROUP BY company,id
	ORDER BY company,salary
	```


**示例**
## title（中文标题）
### 查询数据
### 输出数据
### 测试数据
### 解题思路
#### 题解

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/sql/leecode/mysql-difficult.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***