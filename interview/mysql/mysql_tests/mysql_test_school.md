# MySQL学生信息类试题

## 建表
```
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `CId` varchar(10) default NULL,
  `Cname` varchar(10) default NULL,
  `TId` varchar(10) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
-- ----------------------------
-- Table structure for sc
-- ----------------------------
DROP TABLE IF EXISTS `sc`;
CREATE TABLE `sc` (
  `SId` varchar(10) default NULL,
  `CId` varchar(10) default NULL,
  `score` int(4) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `SId` varchar(10) default NULL,
  `Sname` varchar(20) default NULL,
  `Sage` date default NULL,
  `Ssex` varchar(10) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `TId` varchar(10) default NULL,
  `Tname` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

## 初始化
```
**course**
INSERT INTO `course` VALUES ('01', 'CHINESE', '02');
INSERT INTO `course` VALUES ('02', 'MATH', '01');
INSERT INTO `course` VALUES ('03', 'ENGLISH', '03');
INSERT INTO `course` VALUES ('04', 'JAVA', '04');

**sc**
INSERT INTO `sc` VALUES ('01', '01', 80);
INSERT INTO `sc` VALUES ('01', '03', 99);
INSERT INTO `sc` VALUES ('02', '04', 50);
INSERT INTO `sc` VALUES ('02', '03', 80);
INSERT INTO `sc` VALUES ('03', '01', 80);
INSERT INTO `sc` VALUES ('03', '03', 80);
INSERT INTO `sc` VALUES ('04', '01', 50);
INSERT INTO `sc` VALUES ('04', '03', 20);
INSERT INTO `sc` VALUES ('05', '01', 100);
INSERT INTO `sc` VALUES ('06', '01', 31);
INSERT INTO `sc` VALUES ('06', '03', 34);
INSERT INTO `sc` VALUES ('07', '03', 98);
INSERT INTO `sc` VALUES ('07', '04', 55);
INSERT INTO `sc` VALUES ('01', '04', 50);
INSERT INTO `sc` VALUES ('01', '02', 10);
INSERT INTO `sc` VALUES ('02', '02', 70);
INSERT INTO `sc` VALUES ('03', '02', 50);
INSERT INTO `sc` VALUES ('04', '02', 80);
INSERT INTO `sc` VALUES ('05', '02', 73);
INSERT INTO `sc` VALUES ('06', '02', 99);
INSERT INTO `sc` VALUES ('07', '02', 100);
INSERT INTO `sc` VALUES ('08', '02', 59);
INSERT INTO `sc` VALUES ('09', '02', 80);
INSERT INTO `sc` VALUES ('08', '01', 40);
INSERT INTO `sc` VALUES ('08', '03', 35);

**student**
INSERT INTO `student` VALUES ('01', 'Zhao Lei', '1990-1-1', 'MAN');
INSERT INTO `student` VALUES ('02', 'Qian Dian', '1990-12-21', 'MAN');
INSERT INTO `student` VALUES ('03', 'Sun Feng', '1990-5-20', 'MAN');
INSERT INTO `student` VALUES ('04', 'Li Yun', '1990-8-6', 'MAN');
INSERT INTO `student` VALUES ('05', 'Zhou Mei', '1991-12-1', 'WOMAN');
INSERT INTO `student` VALUES ('06', 'Wu Lan', '1992-3-1', 'WOMAN');
INSERT INTO `student` VALUES ('07', 'Zheng Zhu', '1989-7-1', 'WOMAN');
INSERT INTO `student` VALUES ('08', 'Wang Ju', '1990-1-20', 'WOMAN');
INSERT INTO `student` VALUES ('09', 'Wang Ju', '2020-8-30', 'MAN');

**teacher**
INSERT INTO `teacher` VALUES ('01', 'Li Pengfei');
INSERT INTO `teacher` VALUES ('02', 'Wang Wen');
INSERT INTO `teacher` VALUES ('03', 'Zhang Zhichao');
INSERT INTO `teacher` VALUES ('04', 'Ye Ping');
```

## 练习题
1. 查询**01**课程比**02**课程成绩高的所有学生的学号  
2. 查询平均成绩大于60分的同学的学号和平均成绩  
3. 查询所有同学的学号、姓名、选课数、总成绩  
4. 查询姓**李**的老师的个数  
5. 查询没学过**叶平**老师课的同学的学号、姓名  
6. 查询学过**01**并且也学过编号**02**课程的同学的学号、姓名  
7. 查询学过**Li Pengfei**老师所教的所有课的同学的学号、姓名  
8. 查询课程编号**02**的成绩比课程编号**01**课程低的所有同学的学号、姓名  
9. 查询所有课程成绩小于80分的同学的学号、姓名  
10. 查询没有学全所有课的同学的学号、姓名  
11. 查询至少有一门课与学号为**01**的同学所学相同的同学的学号、姓名  
12. 查询学过学号为**07**同学所有门课的其他同学的学号、姓名  
13. 查询和**07**号的同学学习的课程完全相同的其他同学的学号、姓名  
14. 删除学习**Li Pengfei**老师课的SC表记录  
15. 按平均成绩从高到低显示所有学生的**数据库**、**企业管理**、**英语**三门的课程成绩，按如下形式显示：MATH，ENGLISH，CHINESE  
16. 查询各科成绩最高和最低的分：以如下形式显示：course_id，max，min  
17. 按各科平均成绩从低到高和及格率的百分数从高到低顺序  
18. 查询如下课程平均成绩和及格率的百分数(用**1行**显示): math（01），chinese（02），english（03）  
19. 查询不同老师所教不同课程平均分从高到低显示  
20. 查询如下课程成绩第3名到第6名的学生成绩单：math(01)，chinese(02)，english(03)——student_id，student_name，math，chinese，english，avg_score  
21. 统计列印各科成绩，各分数段人数：课程ID，课程名称，[100-85]，[85-70]，[70-60]，[-60]  
22. 查询学生平均成绩及其名次  
23. 查询各科成绩前三名的记录：(不考虑成绩并列情况)  
24. 查询每门课程被选修的学生数  
25. 查询出只选修了2门课程的全部学生的学号和姓名  
26. 查询男生、女生人数  
27. 查询姓**王**的学生名单  
28. 查询同名同性学生名单，并统计同名人数  
29. 1990年出生的学生名单(注：Student表中Sage列的类型是datetime)  
30. 查询每门课程的平均成绩，结果按平均成绩升序排列，平均成绩相同时，按课程号降序排列  
31. 查询课程名称为**MATH**，且分数低于60的学生姓名和分数  
32. 查询所有学生的选课情况  
33. 查询每门课程成绩在70分以上的姓名、课程名称和分数  
34. 查询不及格的课程，显示学号、姓名、课程号、成绩  
35. 查询课程编号为03且课程成绩在80分以上的学生的学号和姓名  
36. 求选了课程的学生人数  
37. 查询选修**Li Pengfei**老师所授课程的学生中，成绩最高的学生姓名及其成绩  
38. 查询各个课程及相应的选修人数  
39. 查询不同课程成绩相同的学生的学号、课程号、学生成绩  
40. 查询每门课程成绩最好的前两名  
41. 统计每门课程的学生选修人数（超过5人的课程才统计）。要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列  
42. 检索至少选修3门课程的学生学号  
43. 查询全部学生都选修的课程的课程号和课程名  
44. 查询没学过**叶平**老师讲授的任一门课程的学生姓名  
45. 查询两门以上不及格课程的同学的学号及其平均成绩。(这里用groupby去重，因为distinct去掉的是全部属性)  
46. 检索**04**课程分数小于60，按分数降序排列的同学学号  
47. 删除**002**同学的**01**课程的成绩  

## 练习题答案
[MySQL-50道经典sql题汇总](https://blog.csdn.net/qq_41835813/article/details/108329971)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/interview/mysql/mysql_tests/mysql_test_school.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***