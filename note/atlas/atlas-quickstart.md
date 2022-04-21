# Atlas快速入门 🕹️1.0.0

#### 先说一些废话
之前的公司在数据中台的项目启动会上调研决定启用了Atlas作为我们数据血缘管理的工具，让我给大家写了一份Atlas快速入门的文档。  
所以在这里我将这篇文档以一个纯新手的方式再一次优化，呈现给各位读者，希望能对您产生一些有益的效果。  


## 什么是Atlas
Atlas为组织提供了开放的元数据管理和治理功能，以建立其数据资产的目录，对这些资产进行分类和治理，并为数据科学家，分析师和数据治理团队提供围绕这些数据资产的协作功能。  
而如果想要对这些数据做好管理，光用文字、文档等东西是不够的，必须用图，Atlas就是把元数据变成图的工具。  
### 元数据管理应该具有的功能
1. 搜索和发现：数据表、字段、标签、使用信息  
2. 访问控制：访问控制组、用户、策略  
3. 数据血缘：管道执行、查询  
4. 合规性：数据隐私、合规性注释类型的分类  
5. 数据管理：数据源配置、摄取配置、保留配置、数据清除策略  
6. AI 可解释性、再现性：特征定义、模型定义、训练运行执行、问题陈述  
7. 数据操作：管道执行、处理的数据分区、数据统计  
8. 数据质量：数据质量规则定义、规则执行结果、数据统计  


## Atlas在项目中产生的作用
1. 数据血缘管理在数仓中的重要性非常高，他们可以帮助我们非常快速的查询到各种字段表之间的依赖关系  
2. 在我们知道数据血缘之后，如果发生一些突发状况，比如某两个表产生的指标出现了问题，就知道只需要重新跑这两个表就可以重新产出指标  
3. 也就是说，哪里出了问题哪里重跑我们的脚本文件即可，可以来说对我们的数据治理提供了非常大的帮助  


## 特性
1. Atlas支持各种Hadoop和非Hadoop元数据类型  
2. 提供了丰富的`REST API`进行集成  
3. 对数据血缘的追溯达到了字段级别，这种技术还没有其实类似框架可以实现  
4. 对权限也有很好的控制  
### 什么是`REST API`
+ REST API是一组关于如何构建Web应用程序API的架构规则、标准或指导，REST API遵循API原则的架构风格   
+ REST是专门针对Web应用程序而设计的，其目的在于降低开发的复杂度，提高系统的可伸缩性


## 架构
### 架构说明
说明元数据的存储是hbase，检索使用的索引solr，数据来源主要是hive和hbase，数据和atlas之间的通信使用的是kafka
```
元数据存储：hbase，可以采集也可以导出  
检索索引：solr
数据来源：hive（重点），hbase（重点），sqoop，falcon，storm
数据和atlas的通信通道：基于kafka的消息传递接口

Admin UI：web界面操作
Ranger Tag Based Policies：权限管理模块  
Bussiness Type：业务分类  
API：所有功能通过REST API向最终用户暴露，该API允许创建，更新和删除类型和实体  
Graph Engine 图形引擎：使用Graph模型持久保存它管理的元数据对象  
Type System 类型系统：用户为他们想要管理的元数据对象定义模型，Type System 称为实体和类型实例，表示受管理的实际元数据对象  
```

### 组件介绍
1. 采用Hbase存储元数据  
2. 采用Solr实现索引  
3. Ingest/Export 采集导出组件，Type System类型系统，Graph Engine图形引擎，共同构成Atlas的核心机制  
4. 所有功能通过API向用户提供，也可以通过Kafka消息系统进行集成  
5. Atlas支持各种源获取元数据：Hive，Hbase，Sqoop，Storm  
6. 还有优秀的UI支持  


## 安装和依赖
### 环境准备
> jdk8、hadoop@2.7.2  
> zookeeper@3.4.10  
> kafka@0.11.0.2  
> hbase@1.3.1  
> solr@5.2.1  
> hive@1.2.1、mysql  
> azkaban@2.5.0  
> atlas@0.84  

### 依赖集成
1. Atlas集成外部Hbase，主要是通过修改以下配置文件来实现  
	+ 进入`/soft/atlas/conf/atlas-application.properties` -> `atlas.graph.storage.hostname = 集群主机名`，修改atals存储数据主机  
	+ 进入`/soft/atals/conf/hbase`，添加hbase集群的配置文件到`${Atlas_Home}`，示例命令`ln -s /soft/hbase/conf/ /soft/atlas/conf/hbase/`，相当于建立软连接  
	+ 在`/soft/atlas/conf/atlas-env.sh`中添加`HBASE_CONF_DIR`，也就是Hbase配置文件路径`export HBASE_CONF_DIR = /soft/atlas/conf/hbase/conf`  
2. Atlas集成外部Slor，参考视频[大数据技术之atlas视频教程](https://www.bilibili.com/video/BV18y4y1r7vC?p=6)  
3. Atlas集成外部Kafka，参考视频[大数据技术之atlas视频教程](https://www.bilibili.com/video/BV18y4y1r7vC?p=6)  
4. Atlas集成外部Hive，参考视频[大数据技术之atlas视频教程](https://www.bilibili.com/video/BV18y4y1r7vC?p=6)  
5. Atlas修改 WEB UI 访问端口之类的，参考视频[大数据技术之atlas视频教程](https://www.bilibili.com/video/BV18y4y1r7vC?p=6)  


### 什么是`ln -s`命令  
ln命令是linux系统中一个非常重要命令，英文全称是"link"，即链接的意思，它的功能是为某一个文件在另外一个位置建立一个同步的链接  

### 集群启动顺序
`Hadoop -> Zookeeper -> Kafka -> Hbase -> Solr -> 重启Atlas服务`  


## 导入Hive元数据  
主要是通过执行`.sh`脚本文件来导入，后续可以引入`Azkaban来做全流程执行job`  
1. 生成数据  
2. 编写Azkaban程序运行job  
	+ import.job  
	+ ods.job  
	+ dwd.job  
	+ dws.job  
	+ ads.job  
	+ export.job  
	+ 将上述6个文件压缩成一个指标job的文件，例如`target-job.zip`  
3. 创建Azkaban工程并执行，后续就可以通过Atals动态查看各种血缘依赖关系，主要是图形化的界面  


## 使用 Atlas 的 REST API 进行二次开发  
Atlas有丰富的开放接口供大家使用以进行二次开发，对于我们公司后续如果有非常重大的个性化需求项目的开发，能够提供很好的帮助，这里是[官方的api说明文档地址](https://atlas.apache.org/api/v2/index.html)  


## WEB UI 界面上的重点操作说明  
### 登录界面(Login)  
主要会有公司配发的不同权限的使用账号和密码  

### 搜索界面(Search)  
在Search界面中提供了Basic(基础)和Advanced(高级)两种UI界面。  
可查询的数据类型包括但不限于`hive_db、hive_table、hdfs_path`等等，点击橘色方框中的箭头即可查看选择可供查找的数据类型。  

### 基础界面(Basic)  
在Basic界面中，可以进行更加细粒度的划分查询，还可以保存常用的查询条件组合。  
包括的查询条件有：Type（查询的数据类型）、Classification（查询数据的所属分类）、Term（查询数据的术语）、Text（查询数据的name），  
在Basic查询界面中还可以对`Type`和`ClassiFication`进行。  

### 高级界面(Advanced)
在Advanced界面中，查询条件包括：Type（查询的数据类型）、Query（查询语句），也可以保存常用的查询条件组合  

### 分类界面(Classification)  
分类界面中可以进行分类标签的管理，在这个界面中可以新建一个分类，或者在一个分类下新建出一个小分类，也可以删除一个已有的分类，
通过分类的设定，并为数据添加相应的分类标签，可以方便数据的管理与查找。  
#### 查看分类
点击相应的分类便可以看到拥有该分类和其子分类的数据，而在查询界面中点击数据中的分类标签，即可跳转到分类界面的相应分类下  
#### 添加分类
在Atlas中一个数据可以拥有多个分类标签，在查询界面中，可以通过在数据的`Classification`列点击`+`为该数据添加分类  
#### 分类传播选项(propagate)
分类传播使数据所关联的分类能延血缘关系，使其这个数据的后代也能得到其父类数据的分类标签。  
为第一个数据打上分类标签后其数据的子类也得到了该分类的标签，
更改第一个数据的分类标签，子类的分类标签也会受到更改，删除第一个数据的分类标签，其子类对应的分类标签也会删除。  
**总结来说数据的分类标签能沿着血缘关系传播到其子类**
1. 情况一：删除其父类实体时，子类所得到的父类的标签会被删除  
2. 情况二：当子类到父类之间的血缘关系被破坏时，子类也会失去父类的分类标签  
3. 情况三：即使子类到父类之间的一条血缘链被破坏，只要还有另一条血缘链存在且能使子类连接到父类，那么子类还能保存其父类的分类标签  

### 术语表界面(Glossary)，也就是词汇  
在Atlas的Glossary界面下，可以创建术语表，并且在术语表中可以创建一些"单词"，这些"单词"能彼此进行关联和分类。  
以便业务用户在使用的时候，即使这些"单词"在不同的术语表中也能很好的理解它们。此外，这些术语也是可以映射到数据资产中的，即可以给数据添加术语标签。  
术语表抽象出了和数据相关的专业术语，使得用户能以他们更熟悉的方式去查找和使用数据。  
*需要注意的是如果为数据添加术语时，该术语已经设置了分类标签，那么这个术语的分类标签会被数据继承：*  
即数据的分类标签中，会被加上术语的分类标签，这样做是因为，我们可以为术语单独创建一个分类，然后为数据打上术语标签时，数据也会继承这个分类。  
然后我们通过查看这个分类的数据时就能找到被打上这个术语标签的所有数据及术语本身。  
#### 术语视图允许用户执行以下术语操作
1. 创建，更新和删除术语  
2. 添加，删除和更新与术语关联的分类  
3. 添加，删除和更新术语的分类  
4. 在术语之间创建各种关系  
5. 查看与术语关联的实体  
#### 类别视图允许用户执行以下术语操作
1. 创建，更新和删除类别和子类别  
2. 将术语与类别相关联  
3. Category的作用就是方便术语的管理与使用  

### 如何进行权限管理
#### 配置文件模式配置权限管理，也是我们公司使用的模式
1. 启用`Atlas Simple Authprizer` 权限管理 需要在`/atlas/conf/` 中配置文件 atlas-application.properties`  
```
atlas.authorizer.impl = simple
atlas.authorizer.simple.authz.policy.file = /atlas/conf/atlas-simple-authz-policy.json
```
2. 在`/atlas/conf`目录中有两个文件`atlas-simple-authz-policy.json`和`users-credentials.properties`  
	+ `atlas-simple-authz-policy.json` 文件用于配置权限、用户权限、用户组权限  
	+ `users-credentials.properties` 用于配置用户名、用户所属用户组、用户密码，*需要注意的是：密码采用sha256编码*  
#### 使用外部插件实现可视化权限管理  
1. 使用ranger可以实现一个可视化的Atlas权限的管理，需要在`/atlas/conf`目录中配置`atlas-application.properties`
```
atlas.authorizer.impl = ranger
```
2. Atlas Ranger Authorizer支持三种资源层次结构，以控制对类型，实体和管理操作的访问  
3. Ranger会生成审核日志，包含用户访问的详细信息  


[参考视频一 ———— 大数据技术之atlas视频教程](https://www.bilibili.com/video/BV18y4y1r7vC)  
[参考文档一 ———— 数据治理之元数据管理的利器——Atlas入门宝典](https://blog.csdn.net/xiangwang2206/article/details/121072961)  
[参考文档一 ———— 数仓元数据管理之Atlas 整合hive HBase Sqoop（2.1.0）](https://blog.csdn.net/xiaohu21/article/details/109230773)  
[参考文档一 ———— Apache atlas使用说明（UI功能详解）](https://blog.csdn.net/x_iaoa_o/article/details/109581930)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/atlas/atlas-quickstart.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***