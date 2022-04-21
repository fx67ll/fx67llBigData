# Hbase 高可用集群安装

#### 简易记录，待后续补充拓展为自动化脚本并制作博客发布  

### 步骤
1. [官网](http://archive.apache.org/dist/hbase/)下载，本次使用版本***1.3.1***  
2. 上传到服务器的`/home/soft`文件夹下，home下一般是空的请自行创建`soft`文件夹  
3. 解压到`/home/hadoop`文件夹下，home下一般是空的请自行创建`hadoop`文件夹  
	+ `tar -zxvf /home/soft/hbase-1.3.1-bin.tar.gz -C /home/hadoop/`
4. 进入配置文件目录 `cd /home/hadoop/hbase-1.3.1/conf/`  
5. 修改hbase环境配置文件
	+ `vi /home/hadoop/hbase-1.3.1/conf/hbase-env.sh`  
	+ 注意配置文件中都有这些配置，找一下去掉注释符号`#`就行了  
	+ 创建一下pids文件夹 `mkdir /home/hadoop/hbase-1.3.1/data/pids -p`(-p 是递归创建，如果上级不存在则自动创建)  
	```
	// 配置JDK路径  
	export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64  
	// 保存pid文件  
	export HBASE_PID_DIR=/home/hadoop/hbase-1.3.1/data/pids  
	//修改HBASE_MANAGES_ZK，禁用HBase自带的Zookeeper，因为我们是使用独立的Zookeeper  
	export HBASE_MANAGES_ZK=false  
	```
6. 修改hbase核心配置文件   
	+ `vi /home/hadoop/hbase-1.3.1/conf/hbase-site.xml`  
	+ 默认文件进去之后是空的`<configuration></configuration>`  
	+ 注意配置后将`60000`端口全部在防火墙开放一下，但是之前暗转hadoop偷懒直接关闭了防火墙，所以这里不操作了  
	```
	<!-- 设置HRegionServers共享目录，请加上端口号 -->
	<property>
	  <name>hbase.rootdir</name>
	  <value>hdfs://master188:9000/hbase</value>
	</property>

	<!-- 指定HMaster主机 -->
	<property>
	  <name>hbase.master</name>
	  <value>hdfs://master188:60000</value>
	</property>

	<!-- 启用分布式模式 -->
	<property>
	  <name>hbase.cluster.distributed</name>
	  <value>true</value>
	</property>

	<!-- 指定Zookeeper集群位置 -->
	<property>
	  <name>hbase.zookeeper.quorum</name>
	  <value>master188:2181,master189:2181,slave190:2181</value>
	</property>

	<!-- 指定独立Zookeeper安装路径 -->
	<property>
	  <name>hbase.zookeeper.property.dataDir</name>
	  <value>/home/hadoop/zookeeper-3.4.12</value>
	</property>

	<!-- 指定ZooKeeper集群端口 -->
	<property>
	  <name>hbase.zookeeper.property.clientPort</name>
	  <value>2181</value>
	</property>
	```
7. 修改`regionservers`文件，因为当前是使用独立的`zookeeper集群`，所以要指定`RegionServers`所在机器  
	+ `vi /home/hadoop/hbase-1.3.1/conf/regionservers`  
	```
	master188
	master189
	slave190
	```
8. 拷贝hbase到其他机器  
	```
	# 注意！！！在后续步骤中修改复制hadoop到其他服务器的命令，原始命令会导致文件全部位于hadoop中，没有hadoop-2.6.5的文件夹
	scp -r /home/hadoop/hbase-1.3.1/ root@master189:/home/hadoop/hbase-1.3.1/
	scp -r /home/hadoop/hbase-1.3.1/ root@slave190:/home/hadoop/hbase-1.3.1/
	```
9. 在master188上启动hbase，包括进程HMaster、Regionserver  
	+ *注意一定要在active的主节点上启动，否则HMaster将在启动几秒后自动关闭*  
	+ `sh /home/hadoop/hbase-1.3.1/bin/start-hbase.sh`  
10. 在备用主节点master189上启动进程HMaster  
	+ `sh /home/hadoop/hbase-1.3.1/bin/hbase-daemon.sh start master`  
11. 关闭命令
	```
	sh /home/hadoop/hbase-1.3.1/bin/stop-hbase.sh  
	sh /home/hadoop/hbase-1.3.1/bin/hbase-daemon.sh stop master  
	sh /home/hadoop/hbase-1.3.1/bin/hbase-daemon.sh stop regionserver  
	sh /home/hadoop/hbase-1.3.1/bin/hbase-daemons.sh stop regionserver    
	```

#### 报错信息 `OpenJDK 64-Bit Server VM warning: ignoring option MaxPermSize=128m; support was removed in 8.0`  
[解决方案](https://blog.csdn.net/chengyuqiang/article/details/69902291)  

#### 报错信息 `hbase Class path contains multiple SLF4J bindings.`  
[解决方案](https://blog.csdn.net/qq_27575895/article/details/90238240)  
/home/hadoop/hbase-1.3.1/lib
/home/hadoop/hadoop-2.6.5/share/hadoop/common/lib

#### HMaster进程几秒后消失，查看日志发现必须再主节点active的时候启动，备用主节点active没用  
[解决方案](https://blog.csdn.net/yxf19034516/article/details/105736372)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/hbase/hbase-install-sh.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***