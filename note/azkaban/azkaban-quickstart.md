# Azkaban快速入门

## 数据源
业务系统中的数据库和日志数据  

## 如何选择市面上的任务调度工具
1. 简单的任务
Linux crontab是用来定期执行程序的命令
2. 复杂的任务
**Oozie/Azkaban/Airflow/DolphinScheduler**
Azkaban 是一个分布式工作流管理程序，解决Hadoop工作依赖性问题
Oozie 相比 Azkaban 是一个重量级的任务调度系统，功能全面，但配置使用也更复杂  
Airflow 使用python脚本  
DolphinScheduler 使用可视化的方式操作，国产，所以现在比较火  

### Azkaban和Oozie之间的区别
总体来说，ooize相比azkaban是一个重量级的任务调度系统，功能全面，但配置使用也更复杂，
如果可以不在意某些功能的缺失，轻量级调度器azkaban是很不错的候选对象  
1. 功能
	+ 两者均可以调度linux、mapreduce、pig、spark、java等脚本工作流任务    
	+ 两者均可以定时执行工作流任务  
2. 工作流定义
	+ Azkaban使用Properties文件定义工作流  
	+ Oozie使用XML文件定义工作流  
3. 工作流传参
	+ Azkaban支持直接传参，例如`${input}`  
	+ Oozie支持参数和EL表达式，例如`${fs:dirSize(myInputDir)}`
4. 定时执行
	+ Azkaban的定时执行任务是基于时间的
	+ Oozie的定时执行任务基于时间和输入数据，功能强大，但是带来配置的复杂度比较高  
5. 资源管理
	+ Azkaban有较严格的权限控制，如用户对工作流进行读/写/执行等操作  
	+ Oozie暂无严格的权限控制，多人协作就比较麻烦了  
6. 工作流执行
	+ Azkaban有三种运行模式：solo server mode、 two server mode、multiple executor mode  
	+ Oozie作为工作流服务器运行，支持多用户和多工作流  
7. 工作流管理
	+ Azkaban支持浏览器以及ajax方式操作工作流  
	+ Oozie支持命令行、HTTP REST、Java API、浏览器操作工作流  
8. 依赖  
	+ Azkaban依赖于MySql  
	+ Oozie依赖于hadoop  
#### 参考资料————Azkaban和Oozie之间的区别
1. [Azkaban和Oozie的区别](https://www.bbsmax.com/A/LPdoOPMyd3/)  
2. [Azkaban和Oozie的区别](https://blog.csdn.net/weixin_42350858/article/details/107706439)  


## 什么是Azkaban
Azkaban是由Linkedin公司推出的一个批量工作流任务调度器，主要用于在一个工作流内以一个特定的顺序运行一组工作和流程，
它的配置是通过简单的key:value对的方式，通过配置中的dependencies 来设置依赖关系。
Azkaban使用job配置文件建立任务之间的依赖关系，并提供一个易于使用的web用户界面维护和跟踪你的工作流。


## Azkaban的特点
1. 兼容任何版本的hadoop  
2. 易于使用的Web用户界面  
3. 简单的工作流的上传  
4. 方便设置任务之间的关系  
5. 调度工作流  
6. 模块化和可插拔的插件机制  
7. 认证/授权(权限的工作)  
8. 能够杀死并重新启动工作流  
9. 有关失败和成功的电子邮件提醒  


## 架构
#### AzkabanWebServer
`AzkabanWebServer`是整个Azkaban工作流系统的主要管理者，它用户登录认证、负责project管理、定时执行工作流、跟踪工作流执行进度等一系列任务  
#### AzkabanExecutorServer
负责具体的工作流的提交、执行，它们通过mysql数据库来协调任务的执行  
#### 关系型数据库（MySQL）
存储大部分执行流状态，`AzkabanWebServer`和`AzkabanExecutorServer`都需要访问数据库  


## 部署模式
1. Solo
	+ WebServer和ExecServer都启动在一个JVM中，就一个进程  
	+ 使用内置的H2数据库来存储元数据  
2. Two Server
	+ 一台机器模式：只有一台ExecutorServer  
	+ WebServer和ExecutorServer是不同的独立的进程  
	+ 使用MySQL存储元数据  
3. Multiple Executor
	+ 从3.+ 开始，支持多个Executor  
	+ 多台机器模式：多个ExecutorServer  
	+ WebServer和Executor可以不在一台机器上  
	+ 使用MySQL存储元数据  
#### 参考资料————Azkaban的部署模式
[Azkaban--部署模式](https://blog.csdn.net/qq_46893497/article/details/110847616)  
[azkaban的三种安装部署方式](http://t.zoukankan.com/haojia-p-12386222.html)  


## 安装依赖包及其作用
```
# 该包中就是所有的建表语句，主要是配置 MySQL
azkaban-db-3.84.4.tar.gz  

# 执行服务器（Executor）配置
azkaban-exec-server-3.84.4.tar.gz  

# 服务器（Web）配置
azkaban-web-server-3.84.4.tar.gz  
```

## 访问端口号
默认是*8443*，可以通过修改配置文件`azkaban.properties`的方式更改端口号  
端口号使用规则：`jetty.ssl.port > jetty.port`  
但是使用`jetty.ssl.port`的前提是`jetty.use.ssl = true`，这个配置表示开启ssl安全套接层，否则使用jetty.port端口  
```
# 示例配置文件
jetty.use.ssl=false
jetty.maxThreads=25

jetty.ssl.port=8443
jetty.port=8081

jetty.keystore=keystore
jetty.password=password
jetty.keypassword=password
jetty.truststore=keystore
jetty.trustpassword=password
```
#### 参考文档————访问端口号  
[Azkaban WebUI 端口号](https://lawson-t.blog.csdn.net/article/details/83151440)  


## 基本使用介绍
1. projects：最重要的部分，创建一个工程，所有flows将在工程中运行    
2. scheduling: 显示定时任务  
3. executing: 显示当前运行的任务  
4. history: 显示历史运行任务  


## Azkaban使用案例


#### 参考资料  
1. [azkaban简介及azkaban部署、原理和使用介绍](https://blog.csdn.net/wt334502157/article/details/116891032)  
2. [Azkaban快速入门系列(1) | Azkaban的简单介绍](https://blog.csdn.net/qq_16146103/article/details/106198309)  
3. [大数据---Azkaban快速入门](https://blog.csdn.net/qq_43785075/article/details/120576710)  
4. [Azkaban教程](https://blog.csdn.net/qq_41072814/article/details/116378508)  