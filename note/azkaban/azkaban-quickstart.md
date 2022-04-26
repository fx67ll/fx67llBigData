# Azkaban快速入门 🕹️1.0.0

#### 先说一些废话
因为之前自己有用过Azkaban作为自动化任务调度工具，所以想参考自己之前的记录，总结一下关于Azkaban的使用，方便大家使用Azkaban快速实现企业级自动化任务

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
#### 使用多Executor模式的注意事项
为确保所选的 Executor 能够准确的执行任务，我们须在以下两种方案任选其一，推荐使用方案二
1. 方案一：指定特定的 Executor（hadoop101）去执行任务  
	+ 在`MySQL`中`Azkaban`数据库`executors`表中，查询`hadoop101上的Executor的id`  
	+ 在执行工作流程时加入`useExecutor`属性  
2. 方案二：在`Executor`所在所有节点部署任务所需脚本和应用  


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


## 基本使用
### 主要功能
1. Projects：最重要的部分，创建一个工程，所有`flows`将在工程中运行    
2. Scheduling: 显示定时任务  
3. Executing: 显示当前运行的任务  
4. History: 显示历史运行任务  

### 基本流程步骤
1. 首先需要说明的是因为利用界面化操作，所以相关的文件直接在本地windows系统里去编辑，创建，打包zip即可  
2. 创建`xxx.project`工程文件  
	```
	# 作用：
	  # 表示采用新的 Flow-API 方式解析 flow 文件
	# 内容：
	  # 表示当前解析 flow 文件的 azkaban 版本为 2.0
	azkaban-flow-version: 2.0
	```
3. 创建`xxx.flow`流程任务文件  
	```
	# 作用：
	  # 表示作业调度过程
	# 内容：
	  # yaml 语法的编写
	  # name 表示 job 的名称
	  # type 表示 job 的类型
	  # command 表示你要执行作业的方式为命令，这里意思输入Hello World
	nodes:
	  - name: jobA
		type: command
		config:
		  command: echo "Hello World"
	```
4. 将上述两个文件压缩成一个`.zip`文件，并上传 *需要注意的是：压缩包的文件名称必须是英文*  
5. 上传后，如果想看`Job`的内容是什么，可以在`Job Command`中可以查看解析出任务内容  
6. 点击`Flows`中`Command`任务，可以进入到任务的具体界面，`Execute`可以执行任务，`Schedul`可以进行定时调度  
7. 执行后的任务中，点击`Job List`可以查看执行日志，`Flow Log`可以查看流程日志，绿色表示成功，蓝色表示正在执行，红色表示执行失败  
8. 任务执行后，可以在`History`中查看任务历史记录  

### 常见任务类型
#### 执行 shell 命令
```
type=command
command=echo 'hello-world'
```
#### 执行 shell 脚本
```
type=command
command=sh hello-world.sh
```
#### 执行 Spark 程序
```
type=command
command=/usr/install/spark/bin/spark-submit --class com.test.AzkabanTest test-0.1.0.jar
```
#### 执行 hive 命令、脚本
```
type=command
command=beeline -u jdbc:hive://localhost:7777 -n hive -p hive -f 'test.sql'
```
#### 执行 MapReduce 程序
```
type=command
command=${HADOOP_HOME}bin/hadoop jar hadoop-mapreduce-examples--0.1.0.jar
mapreduce-test ${input} ${output}
```

### 多任务依赖案例
使用`dependsOn`属性来表示依赖，他的值是一个数组
```
# 示例 basic.flow
# JobA 和 JobB 执行完了，才能执行 JobC

nodes:
  - name: jobA
  type: command
  config:
    command: echo "I’m JobA"

  - name: jobB
  type: command
  config:
    command: echo "I’m JobB"

  - name: jobC
    type: command
    # jobC 依赖 JobA 和 JobB
    dependsOn:
      - jobA
      - jobB
    config:
      command: echo "I’m JobC"
```

### 失败重试
#### 自动失败重试
使用`retries`和` retry.backoff`来配置重试次数，重试的时间间隔
```
# 示例 basic.flow，在任务中配置
nodes:
  - name: JobA
    type: command
    config:
      # 执行脚本
      command: sh xxx.sh
	  # 重试次数 3次
      retries: 3
	  # 重试间隔时间 10000ms 也就是10s
      retry.backoff: 10000
	  
# 示例 basic.flow，在flow的全局配置
config:
  retries: 3
  retry.backoff: 10000
nodes:
  - name: jobA
    type: command
    config:
      command: sh xxxq.sh
  - name: jobB
  type: command
  config:
    command: sh xxxb.sh
  - name: jobC
    type: command
    dependsOn:
      - jobA
      - jobB
    config:
      command: sh xxxc.sh
```
#### 手动失败重试
在`Flow View`界面右键点击需要重拾的`Job`，选择需要的重新执行的某个过程，`Enable`和`Disable`下面都分别有如下参数：
1. Parents：该作业的上一个任务
2. Ancestors：该作业前的所有任务
3. Children：该作业后的一个任务
4. Descendents：该作业后的所有任务
5. Enable All：所有的任务

### 运行Java主类方法
JavaProcess 类型可以运行一个自定义主类方法，type 类型为 javaprocess，可用的配置为：
+ Xms：最小堆
+ Xmx：最大堆
+ classpath：类路径
+ java.class：要运行的`Java对象`，其中必须包含`Main`方法
+ main.args：`Main`方法的参数

#### 运行Java主类方法案例
```
# 示例 basic.flow
nodes:
  - name: test_java
    type: javaprocess
    config:
      Xms: 96M
      Xmx: 200M
      java.class: com.fx67ll.springboot.AzkabanTest
```

### 使用条件工作流
#### 使用运行时参数来使用条件
1. 基本原理
	+ 父Job将参数写入 `JOB_OUTPUT_PROP_FILE` 所指向的环境变量文件
	+ 子Job使用EL表达式 `${jobName:param}` 来获取父Job输出的参数并定义执行条件  
2. 支持的条件参数
	```
	== 等于
	!= 不等于
	> 大于
	>= 大于等于
	< 小于
	<= 小于等于
	&& 与
	|| 或
	! 非
	```
3. 案例：JobB依赖JobA，但是JobB不需要每天都执行，只需要每个周一执行  
	```
	# 示例 JobA.sh
	echo "do JobA"
	# 获取当前是周几
	wk=`date + %w`
	echo "{\"wk\":$wk}" >$JOB_OUTPUT_PROP_FILE

	# 示例 JobB.sh
	echo "do JobB"

	# 示例 basic.flow
	nodes:
	  - name: jobA
		type: command
		config:
		  command: sh JobA.sh
	  - name: jobB
		type: command
		dependsOn:
		  - jobA
		config:
		  command: sh JobB.sh
		  condition: ${JobA:wk} == 1
	```
#### 使用预定义宏
Azkaban 中预置了几个特殊的判断条件，称为预定义宏，预定义宏会根据所有父 Job 的完成情况进行判断，再决定是否执行，可用的预定义宏如下：
	```
	# all_success: 表示父 Job 全部成功才执行(默认)  
	# all_done：表示父 Job 全部完成才执行  
	# all_failed：表示父 Job 全部失败才执行  
	# one_success：表示父 Job 至少一个成功才执行  
	# one_failed：表示父 Job 至少一个失败才执行  

	# 示例 JobA.sh
	echo "do JobA"

	# 示例 JobB.sh
	echo "do JobB"

	# 示例 JobC.sh
	echo "do JobC"

	# 示例 basic.flow
	nodes:
	  - name: jobA
		type: command
		config:
		  command: sh JobA.sh
	  - name: jobB
		type: command
		config:
		  command: sh JobA.sh
		dependsOn:
		  - jobA
		  - jobB
		config:
		  command: sh JobC.sh
		  condition: one_success
		  
	# 提交的时候故意不提交 JobB.sh，以测试预定义宏是否生效  
	```

### 定时执行
在执行工作流时候，选择左下角`Schedule`按钮，在`Schedule Flow Options`配置即可  

### 告警
#### 邮件告警
**可以参考视频教程————[大数据Azkaban教程](https://www.bilibili.com/video/BV1KS4y1M7yQ?p=17)详细学习，这里后期会补上说明**  
#### 电话告警
**可以参考视频教程————[大数据Azkaban教程](https://www.bilibili.com/video/BV1KS4y1M7yQ?p=18)详细学习，这里后期会补上说明**  


## YAML
### 什么是YAML 
YAML（YAML 不是标记语言）是一种非常灵活的格式，几乎是 JSON 的超集，已经被用在一些著名的项目中，如 Travis CI、Circle CI 和 AWS CloudFormation。
YAML 的库几乎和 JSON 一样无处不在。除了支持注释、换行符分隔、多行字符串、裸字符串和更灵活的类型系统之外，YAML 也支持引用文件，以避免重复代码。  

### YAML简介
1. YAML语言的设计参考了JSON，XML和SDL等语言，YAML 强调以数据为中心，简洁易读，编写简单  
2. YAML 语言（发音 /ˈjæməl/ ）的设计目标，就是方便人类读写，它实质上是一种通用的数据串行化格式  
3. YAML 有一个小的怪癖，所有的 YAML 文件开始行都应该是 `---`，这是 YAML 格式的一部分， 表明一个文件的开始   
**有意思的命名**
YAML 全称是 "YAML Ain’t a Markup Language"（YAML不是一种置标语言）的递归缩写。
在开发的这种语言时，YAML 的意思其实是："Yet Another Markup Language"（仍是一种置标语言）  

### 语法特点
```
大小写敏感
通过缩进表示层级关系
禁止使用 tab 缩进，只能使用空格键
缩进的空格数目不重要，只要相同层级左对齐即可
使用 # 表示注释
```

### 支持的数据结构
1. 对象：键值对的集合，又称为映射（mapping）/ 哈希（hashes） / 字典（dictionary）  
2. 数组：一组按次序排列的值，又称为序列（sequence） / 列表（list）  
3. 纯量（scalars）：单个的、不可再分的值  

### 语法说明
1. 引号
	```
	a. 双引号""：不会转义字符串里面的特殊字符，特殊字符作为本身想表示的意思。
	name: "123\n123" 
	输出： 123 换行 123
	
	b. 单引号''：会将字符串里面的特殊字符转义为字符串处理
	name: "123\n123"
	输出： 123\n123
	
	c. 如果不加引号将会转义特殊字符，当成字符串处理
	```
2. 文本块
	```
	a. |：使用|标注的文本内容缩进表示的块，可以保留块中已有的回车换行
	value: |
	   hello
	   world!
	输出：hello 换行 world！
	
	b. +表示保留文字块末尾的换行，-表示删除字符串末尾的换行  
	value: |
	hello
	
	value: |-
	hello
	
	value: |+
	hello
	输出：hello\n hello hello\n\n(有多少个回车就有多少个\n)
	注意 "|" 与 文本之间须另起一行
	
	c. >：使用 > 标注的文本内容缩进表示的块，将块中回车替换为空格，最终连接成一行
	value: > hello
	world!
	输出：hello 空格 world！
	注意 ">" 与 文本之间的空格，使用定界符""（双引号）、''（单引号）或回车表示的块，最终表示成一行  
	```
3. 锚点与引用
	```
	使用 & 定义数据锚点（即要复制的数据），使用 * 引用锚点数据（即数据的复制目的地）  
	
	name: &a yaml
	book: *a
	books: 
	   - java
	   - *a
	   - python
	
	输出book： yaml
	输出books：[java,yaml,python]
	
	注意 * 引用部分不能追加内容
	```
4. 纯量与数据类型约定
	```
	a. 纯量是最基本的、不可再分的值  
	
	b. 字符串
	使用''或""或不使用引号
	value0: 'hello World!'
	value1: "hello World!"
	value2: hello World!
	
	c. 布尔值
	true或false表示  
	
	d. 数字
	12 # 整数 
	014 # 八进制整数 
	0xC ＃ 十六进制整数 
	13.4 ＃ 浮点数 
	1.2e+34 ＃ 指数 
	.inf空值 ＃ 无穷大
	
	c. 空值
	null或~表示
	
	d. 日期
	使用 iso-8601 标准表示日期
	date: 2018-01-01t16:59:43.10-05:00
	在springboot中yaml文件的时间格式 date: yyyy/MM/dd HH:mm:ss
	
	e. 强制类型转换
	YAML 允许使用个感叹号!，强制转换数据类型，单叹号通常是自定义类型，双叹号是内置类型  
	money: !!str
	123
	date: !Boolean
	true
	
	f. 内置类型：
	!!int # 整数类型 
	!!float # 浮点类型 
	!!bool # 布尔类型 
	!!str # 字符串类型 
	!!binary # 也是字符串类型 
	!!timestamp # 日期时间类型 
	!!null # 空值 
	!!set # 集合 
	!!omap,!!pairs # 键值列表或对象列表
	!!seq # 序列，也是列表 !!map # 键值表
	```
5. 对象
	```
	Map（属性和值）（键值对）的形式：
	key: 空格 v ：表示一堆键值对，空格不可省略  
	
	car:
	    color: red
	    brand: BMW
	
	一行写法
	car:{
	   color: red，brand: BMW}
	
	相当于json：
	{"color":"red","brand":"BMW"}
	
	例如表示url属性值  
	url: https://www.liuluanyi.cn 
	转为 JavaScript 如下:
	{ url: 'https://www.liuluanyi.cn'}
	
	YAML 也允许另一种写法，将所有键值对写成一个行内对象  
	host: { ip: 10.1.1.1, port: 2222 } 
	转为 JavaScript 如下:
	{ host: { ip: '10.1.1.1', port: 2222 } }
	```
6. 数组
	```
	a. 一组连词线开头的行，构成一个数组  
	brand:
	   - audi
	   - bmw
	   - ferrari
	
	一行写法
	brand: [audi,bmw,ferrari]
	相当于json
	["auri","bmw","ferrari"]
	
	b. 数组对象：列表中的所有成员都开始于相同的缩进级别，并且使用一个 --- 作为开头  
	---
	ipaddr:
	- 120.168.117.21
	- 120.168.117.22
	- 120.168.117.23
	转为 JavaScript 如下:
	ipaddr: [ '120.168.117.21', '120.168.117.22', '120.168.117.23' ]
	
	c. 数据结构的子成员是一个数组，则可以在该项下面缩进一个空格。
	-
	 - source
	 - destination
	 - services
	转为 JavaScript 如下:
	[ [ 'source', 'destination', 'services' ] ]
	
	d. 数组也可以采用行内(或者流式)表示法。
	services: [FTP, SSH]
	companies: [{id: 1,name: company1,price: 200W},{id: 2,name: company2,price: 500W}]
	转为 JavaScript 如下:
	{ services: [ 'FTP', 'SSH' ] }
	{ companies: 
	   [ { id: 1, name: 'company1', price: '200W' },
	     { id: 2, name: 'company2', price: '500W' } ] }
	
	f. 对象和数组复合使用
	languages:
	 - Ruby
	 - Perl
	 - Python 
	websites:
	 YAML: yaml.org 
	 Ruby: ruby-lang.org 
	 Python: python.org 
	转为 JavaScript 如下:
	{ languages: [ 'Ruby', 'Perl', 'Python' ],
	  websites: { YAML: 'yaml.org', Ruby: 'ruby-lang.org', Python: 'python.org' } }
	```
7. 常量
	```
	布尔值 boolean: 
	    - TRUE  #true,True都可以
	    - FALSE  #false，False都可以
	
	浮点数 float:
	    - 3.14
	    - 6.8523015e+5  #可以使用科学计数法
	
	整数 int:
	    - 123
	    - 0b1010_0111_0100_1010_1110    #二进制表示
	
	Null null:
	    nodeName: 'node'
	    parent: ~  #使用~表示null
	
	字符串 string:
	    - 哈哈
	    - 'Hello world'  #可以使用双引号或者单引号包裹特殊字符
	    - newline
	      newline2    #字符串可以拆成多行，每一行会被转化成一个空格
	
	时间 date:
	    - 2018-02-17    #日期必须使用ISO 8601格式，即yyyy-MM-dd
	
	日期 datetime: 
	    -  2018-02-17T15:02:31+08:00    #时间使用ISO 8601格式，时间和日期之间使用T连接，最后使用+代表时区
	
	转为 JavaScript 如下:
	{ boolean: [ true, false ],
	  float: [ 3.14, 685230.15 ],
	  int: [ 123, 685230 ],
	  null: { nodeName: 'node', parent: null },
	  string: [ '哈哈', 'Hello world', 'newline newline2' ],
	  date: [ Sat Feb 17 2018 08:00:00 GMT+0800 (中国标准时间) ],
	  datetime: [ Sat Feb 17 2018 15:02:31 GMT+0800 (中国标准时间) ] }
	```
8. 特殊符号总结
	```
	a. YAML 允许使用两个感叹号，强制转换数据类型  
	test1: !!str 123
	test2: !!str true
	转为 JavaScript 如下:
	{ test1: '123', test2: 'true' }
	
	b. … 和---配合使用，在一个配置文件中代表一个文件的结束：
	---
	time: 20:03:20
	player: Sammy Sosa
	action: strike (miss)
	...
	---
	time: 20:03:47
	player: Sammy Sosa
	action: grand slam
	...
	
	c. >在字符串中折叠换行，| 保留换行符，这两个符号是YAML中字符串经常使用的符号
	this: |
	  Foo
	  Bar
	that: >
	  Foo
	  Bar
	转为 JavaScript 如下:
	{ this: 'Foo\nBar\n', that: 'Foo Bar\n' }
	
	d. 引用，重复的内容在YAML中可以使用&来完成锚点定义，使用 * 来完成锚点引用
	defaults: &defaults
	  adapter:  postgres
	  host:     localhost
	development:
	  database: myapp_development
	  <<: *defaults
	test:
	  database: myapp_test
	  <<: *defaults
	转为 JavaScript 如下:
	{ defaults: { adapter: 'postgres', host: 'localhost' },
	  development: 
	   { database: 'myapp_development',
	     adapter: 'postgres',
	     host: 'localhost' },
	  test: 
	   { database: 'myapp_test',
	     adapter: 'postgres',
	     host: 'localhost' } }
	
	注意，不能独立的定义锚点，比如不能直接这样写： &SS Sammy Sosa
	另外，锚点能够定义更复杂的内容，比如：
	default: &default
	    - Mark McGwire
	    - Sammy Sosa
	hr: *default
	那么hr相当于引用了default的数组，注意，hr: *default 要写在同一行  
	```

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/azkaban/azkaban-quickstart.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***