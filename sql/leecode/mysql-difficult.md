# MySQL困难练习题汇总 🕹️0.1.0

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
),
t2 AS(
SELECT Department,
        Employee,
        Salary
FROM t1
WHERE salaryRank <=3
)
SELECT * FROM t2;
```

**示例**
## title（中文标题）
### 查询数据
### 输出数据
#### 题解

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/sql/leecode/mysql-difficult.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***