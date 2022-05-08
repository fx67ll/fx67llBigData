# MySQL简单练习题汇总 🕹️0.2.1

整理一些力扣上的简单级别MySQL练习题供大家学习参考使用  
#### 目录
1. combine-two-tables（组合两个表）  
2. employees-earning-more-than-their-managers（超过经理收入的员工）  
3. duplicate-emails（查找重复的电子邮箱）  
4. customers-who-never-order（从不订购的客户）  
5. employee-bonus（员工奖金）  
6. delete-duplicate-emails（删除重复的电子邮箱）  

## combine-two-tables（组合两个表）
查询来报告`Person`表中每个人的姓、名、城市和状态。如果`personId`的地址不在`Address`表中，则报告为空`null`  
### 查询数据
1. Person 表
	```
	+-------------+---------+
	| 列名         | 类型     |
	+-------------+---------+
	| PersonId    | int     |
	| FirstName   | varchar |
	| LastName    | varchar |
	+-------------+---------+
	personId是该表的主键列。
	该表包含一些人的ID和他们的姓和名的信息。
	 ```
2. Address 表
	```
	+-------------+---------+
	| 列名         | 类型    |
	+-------------+---------+
	| AddressId   | int     |
	| PersonId    | int     |
	| City        | varchar |
	| State       | varchar |
	+-------------+---------+
	addressId是该表的主键列。
	该表的每一行都包含一个ID = PersonId的人的城市和州的信息。
	 ```
### 输出数据
	```
	输入: 
	Person表:
	+----------+----------+-----------+
	| personId | lastName | firstName |
	+----------+----------+-----------+
	| 1        | Wang     | Allen     |
	| 2        | Alice    | Bob       |
	+----------+----------+-----------+
	Address表:
	+-----------+----------+---------------+------------+
	| addressId | personId | city          | state      |
	+-----------+----------+---------------+------------+
	| 1         | 2        | New York City | New York   |
	| 2         | 3        | Leetcode      | California |
	+-----------+----------+---------------+------------+
	输出: 
	+-----------+----------+---------------+----------+
	| firstName | lastName | city          | state    |
	+-----------+----------+---------------+----------+
	| Allen     | Wang     | Null          | Null     |
	| Bob       | Alice    | New York City | New York |
	+-----------+----------+---------------+----------+
	解释: 
	地址表中没有 personId = 1 的地址，所以它们的城市和州返回null。
	addressId = 1 包含了 personId = 2 的地址信息。
	```
#### 题解：
```sql
SELECT a.firstName, a.lastName, b.city, b.state
FROM Person a LEFT JOIN Address b 
ON a.PersonId = b.PersonId;
```

## employees-earning-more-than-their-managers（超过经理收入的员工）
查询来查找收入比经理高的员工  
### 查询数据
Employee 表:  
```
+----+-------+--------+-----------+
| id | name  | salary | managerId |
+----+-------+--------+-----------+
| 1  | Joe   | 70000  | 3         |
| 2  | Henry | 80000  | 4         |
| 3  | Sam   | 60000  | Null      |
| 4  | Max   | 90000  | Null      |
+----+-------+--------+-----------+
```
### 输出数据 
```
+----------+
| Employee |
+----------+
| Joe      |
+----------+
```
#### 题解
```sql
SELECT t1.name AS Employee
FROM Employee t1 LEFT JOIN Employee t2 
ON t1.salary > t2.salary
WHERE t1.managerId = t2.id;
```

## duplicate-emails（查找重复的电子邮箱）
查找`Person`表中所有重复的电子邮箱
### 查询数据
```
+----+---------+
| Id | Email   |
+----+---------+
| 1  | a@b.com |
| 2  | c@d.com |
| 3  | a@b.com |
+----+---------+
```
### 输出数据 
```
+---------+
| Email   |
+---------+
| a@b.com |
+---------+
```
#### 题解
1. 思路一：使用`having`子句
	```sql
	SELECT Email
	FROM Person
	GROUP BY Email
	HAVING COUNT(Email)>=2;
	```
2. 思路二：使用`where`子句
	```sql
	WITH t1 AS (
	SELECT Email, 
		COUNT(Email) AS EmailCount
	FROM Person
	GROUP BY Email
	) 
	SELECT Email FROM t1 WHERE EmailCount>1;
	```

## customers-who-never-order（从不订购的客户）
查询所有从不订购任何东西的客户
### 查询数据
1. Customers 表：
	```
	+----+-------+
	| Id | Name  |
	+----+-------+
	| 1  | Joe   |
	| 2  | Henry |
	| 3  | Sam   |
	| 4  | Max   |
	+----+-------+
	```
2. Orders 表：
	```
	+----+------------+
	| Id | CustomerId |
	+----+------------+
	| 1  | 3          |
	| 2  | 1          |
	+----+------------+
	```
### 输出数据  
	```
	+-----------+
	| Customers |
	+-----------+
	| Henry     |
	| Max       |
	+-----------+
	```
#### 题解
1. 思路一，使用连接查询
```sql
WITH t1 AS(
SELECT CustomerId
FROM Orders
GROUP BY CustomerId
),
t2 AS(
SELECT t3.Name AS Customers
FROM Customers t3 LEFT JOIN Orders t4
ON t4.CustomerId = t3.Id
WHERE t4.CustomerId IS NULL
)
SELECT * FROM t2;
```
2. 思路二，使用`not in`子句
```sql
SELECT Customers.Name as Customers
FROM Customers
WHERE Customers.Id NOT IN
(
    SELECT customerid from orders
);
```

## employee-bonus（员工奖金）
选出所有`bonus<1000`的员工的`name`及其`bonus`  
### 查询数据
1. Employee 表
	```
	+-------+--------+-----------+--------+
	| empId |  name  | supervisor| salary |
	+-------+--------+-----------+--------+
	|   1   | John   |  3        | 1000   |
	|   2   | Dan    |  3        | 2000   |
	|   3   | Brad   |  null     | 4000   |
	|   4   | Thomas |  3        | 4000   |
	+-------+--------+-----------+--------+
	empId 是这张表单的主关键字
	```
2. Bonus 表
	```
	+-------+-------+
	| empId | bonus |
	+-------+-------+
	| 2     | 500   |
	| 4     | 2000  |
	+-------+-------+
	empId 是这张表单的主关键字
	```
### 输出数据
	```
	+-------+-------+
	| name  | bonus |
	+-------+-------+
	| John  | null  |
	| Dan   | 500   |
	| Brad  | null  |
	+-------+-------+
	```
#### 题解
```sql
SELECT e.name,b.bonus FROM
Employee e LEFT JOIN Bonus b
ON e.empId = b.empId 
WHERE b.bonus IS NULL OR b.bonus < 1000;
```

## delete-duplicate-emails（删除重复的电子邮箱）
删除所有重复的电子邮件，只保留一个id最小的唯一电子邮件  
### 查询数据
Person 表:
```
+----+------------------+
| id | email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
| 3  | john@example.com |
+----+------------------+
```
### 输出数据
```
+----+------------------+
| id | email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
+----+------------------+
解释: john@example.com重复两次。我们保留最小的Id = 1。
```
#### 题解
```sql
DELETE p1 
FROM Person p1,Person p2
WHERE p1.email = p2.email AND p1.id > p2.id	
```


**示例**
## title（中文标题）
### 查询数据
### 输出数据
#### 题解

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/sql/leecode/mysql-easy.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***