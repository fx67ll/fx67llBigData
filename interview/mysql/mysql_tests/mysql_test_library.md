# MySQL图书管理信息类试题

## 建表
```
CREATE TABLE book (
	b_no varchar(10),
	b_type varchar(20),
	b_name varchar(20),
	b_author varchar(20),
	b_publish varchar(20),
	b_price decimal(18,2)
);

CREATE TABLE reader (
	r_no varchar(20),
	r_unit varchar(20),
	r_name varchar(20),
	r_sex varchar(20),
	r_pro varchar(20),
	r_address varchar(20) 
);

CREATE TABLE borrow ( 
  r_no varchar(20), 
  b_no varchar(20), 
  b_time datetime
);
```

## 初始化
```
**book**
INSERT INTO book
VALUES
	( '445501', 'TP3/12', '数据库导论', '王强', '科学出版社', 17.90 );
INSERT INTO book
VALUES
	( '445502', 'TP3/12', '数据库导论', '王强', '科学出版社', 17.90 );
INSERT INTO book
VALUES
	( '445503', 'TP3/12', '数据库导论', '王强', '科学出版社', 17.90 );
INSERT INTO book
VALUES
	( '332211', 'TP5/10', '计算机基础', '李伟', '高等教育出版社', 18.00 );
INSERT INTO book
VALUES
	( '112266', 'TP3/12', 'FoxBASE', '张三', '电子工业出版社', 23.60 );
INSERT INTO book
VALUES
	( '665544', 'TS7/21', '高等数学', '刘明', '高等教育出版社', 20.00 );
INSERT INTO book
VALUES
	( '114455', 'TR9/12', '线性代数', '孙业', '北京大学出版社', 20.80 );
INSERT INTO book
VALUES
	( '113388', 'TR7/90', '大学英语', '胡玲', '清华大学出版社', 12.50 );
INSERT INTO book
VALUES
	( '446601', 'TP4/13', '数据库基础', '马凌云', '人民邮电出版社', 22.50 );
INSERT INTO book
VALUES
	( '446602', 'TP4/13', '数据库基础', '马凌云', '人民邮电出版社', 22.50 );
INSERT INTO book
VALUES
	( '446603', 'TP4/13', '数据库基础', '马凌云', '人民邮电出版社', 22.50 );
INSERT INTO book
VALUES
	( '449901', 'TP4/14', 'FoxPro大全', '周虹', '科学出版社', 32.70 );
INSERT INTO book
VALUES
	( '449902', 'TP4/14', 'FoxPro大全', '周虹', '科学出版社', 32.70 );
INSERT INTO book
VALUES
	( '118801', 'TP4/15', '计算机网络', '黄力钧', '高等教育出版社', 21.80 );
INSERT INTO book
VALUES
	( '118802', 'TP4/15', '计算机网络', '黄力钧', '高等教育出版社', 21.80 );

**reader**
INSERT INTO reader
VALUES
	( '111', '信息系', '王维利', '女', '教授', '1号楼' );
INSERT INTO reader
VALUES
	( '112', '财会系', '李立', '男', '副教授', '2号楼' );
INSERT INTO reader
VALUES
	( '113', '经济系', '张三', '男', '讲师', '3号楼' );
INSERT INTO reader
VALUES
	( '114', '信息系', '周华发', '男', '讲师', '1号楼' );
INSERT INTO reader
VALUES
	( '115', '信息系', '赵正义', '男', '工程师', '1号楼' );
INSERT INTO reader
VALUES
	( '116', '信息系', '李明', '男', '副教授', '1号楼' );
INSERT INTO reader
VALUES
	( '117', '计算机系', '李小峰', '男', '助教', '1号楼' );
INSERT INTO reader
VALUES
	( '118', '计算机系', '许鹏飞', '男', '教授', '1号楼' );
INSERT INTO reader
VALUES
	( '119', '计算机系', '刘大龙', '男', '副教授', '4号楼' );
INSERT INTO reader
VALUES
	( '120', '国际贸易', '李雪', '男', '副教授', '4号楼' );
INSERT INTO reader
VALUES
	( '121', '国际贸易', '李爽', '女', '讲师', '4号楼' );
INSERT INTO reader
VALUES
	( '122', '国际贸易', '王纯', '女', '讲师', '4号楼' );
INSERT INTO reader
VALUES
	( '123', '财会系', '沈小霞', '女', '助教', '2号楼' );
INSERT INTO reader
VALUES
	( '124', '财会系', '朱海', '男', '讲师', '2号楼' );
INSERT INTO reader
VALUES
	( '125', '财会系', '马英明', '男', '副教授', '2号楼' );

**borrow**
INSERT INTO borrow
VALUES
	( '112', '445501', '1997-3-19' );
INSERT INTO borrow
VALUES
	( '125', '332211', '1997-2-12' );
INSERT INTO borrow
VALUES
	( '111', '445503', '1997-8-21' );
INSERT INTO borrow
VALUES
	( '112', '112266', '1997-3-14' );
INSERT INTO borrow
VALUES
	( '114', '665544', '1997-10-21' );
INSERT INTO borrow
VALUES
	( '120', '114455', '1997-11-2' );
INSERT INTO borrow
VALUES
	( '120', '118801', '1997-10-18' );
INSERT INTO borrow
VALUES
	( '119', '446603', '1997-12-12' );
INSERT INTO borrow
VALUES
	( '112', '449901', '1997-10-23' );
INSERT INTO borrow
VALUES
	( '115', '449902', '1997-8-21' );
INSERT INTO borrow
VALUES
	( '118', '118801', '1997-9-10' );
```

## 练习题
1. 查找出价格位于10元和20元之间的图书种类，结果按出版单位和单价升序排序  
2. 找出藏书中各个出版社的册数、价值总额  
3. 求出各个出版社图书的最高价格、最低价格和总册数  
4. 查找所有借了书的读者的姓名以及所在单位  
5. 找出李某所借图书的所有图书的书名及借书日期  
6. 查询1997年10月以后借书的读者借书证号、姓名和单位  
7. 找出借阅了FoxPro大全一书的借书证号以及作者  
8. 分别找出借书人次超过1人次的单位及人次数  
9. 找出与赵正义在同一天借书的读者姓名、所在单位以及借书日期  
10. 求信息系当前借阅图书的读者人次数  
11. 找出当前至少借阅了2本书的读者及所在单位  
12. 找出姓李的读者姓名和所在单位  
13. 求科学出版社图书的最高单价、最低单价和平均单价  
14. 查找出高等教育出版社的所有图书及单价，结果按单价降序排列  
15. 列出图书库中所有藏书的书名以及出版单位  

## 练习题答案
[MySQL练习题及答案（图书管理数据库）](https://blog.csdn.net/wdy00000/article/details/122389471)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址]()，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***