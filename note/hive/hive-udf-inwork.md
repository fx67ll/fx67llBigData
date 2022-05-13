# Hive中UDF函数在工作中的应用  🕹️0.1.0  

#### Hive中你用过UDF解决过哪些问题（重点处理下）
1. [hive之UDF编程详解](https://blog.csdn.net/qq_32641659/article/details/89421696)  
2. 查阅更多资料，，并准备至少三个生产环境可能会遇到的问题  

## UDF/UDAF/UDTF之间的区别
1. UDF：单行进入，单行输出
	+ UDF 操作作用于单个数据行，并且产生一个数据行作为输出  
	+ 大多数函数都属于这一类（比如数学函数和字符串函数）  
2. UDAF：多行进入，单行输出
	+ UDAF 接受多个输入数据行，并产生一个输出数据行  
	+ 像`COUNT`和`MAX`这样的函数就是聚集函数  
3. UDTF：单行输入，多行输出
	+ UDTF 操作作用于单个数据行，并且产生多个数据行或者一个表作为输出
	+ 比如`lateral view explore()` 


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hive/hive-udf-inwork.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***