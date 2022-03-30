# Hadoop 高可用集群安装

#### 简易记录，待后续补充拓展为自动化脚本并制作博客发布  

### 步骤
1. [官网](https://archive.apache.org/dist/hadoop/common/)下载，本次使用版本***2.6.5***  
2. 上传到服务器的`/home/soft`文件夹下，home下一般是空的请自行创建`soft`文件夹  
3. 解压到`/home/hadoop`文件夹下，home下一般是空的请自行创建`hadoop`文件夹  
	+ `tar -zxvf /home/soft/hadoop-2.6.5.tar.gz -C /home/hadoop/`
4. 进入配置文件目录 `cd /home/hadoop/hadoop-2.6.5/etc/hadoop`
5. 配置hadoop环境配置，修改JDK地址  
	+ 先使用下面的方法获取yum安装后的java路径 `/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64`  
	+ `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/hadoop-env.sh`  
	+ 找到地址那行修改地址 `export JAVA_HOME=../../../usr/lib/jvm/java-1.8.0-openjdk-1.8.0.322.b06-1.el7_9.x86_64` 
6. 修改hadoop核心配置 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/core-site.xml`  
	+ 默认文件进去之后是空的`<configuration></configuration>`  
	```
	  <!-- hdfs地址，ha模式中是连接到nameservice  -->
	  <property>
	    <name>fs.defaultFS</name>
	    <value>hdfs://ns1</value>
	  </property>
	  <!-- 这里的路径默认是NameNode、DataNode、JournalNode等存放数据的公共目录，也可以单独指定 -->
	  <property>
	    <name>hadoop.tmp.dir</name>
	    <value>/home/hadoop/hadoop-2.6.5/tmp</value>
	  </property>
	
	  <!-- 指定ZooKeeper集群的地址和端口。注意，数量一定是奇数，且不少于三个节点-->
	  <property>
	    <name>ha.zookeeper.quorum</name>
	    <value>master188:2181,master189:2181,slave190:2181</value>
	  </property>
	```
7. 修改hdfs核心配置 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/hdfs-site.xml`  
	+ 默认文件进去之后是空的`<configuration></configuration>`  
	+ 注意配置后将`9000、50070、8485`端口全部在防火墙开放一下
	```
	<!-- 指定副本数，不能超过机器节点数  -->
	<property>
	  <name>dfs.replication</name>
	  <value>3</value>
	</property>

	<!-- 为namenode集群定义一个services name -->
	<property>
	  <name>dfs.nameservices</name>
	  <value>ns1</value>
	</property>

	<!-- nameservice 包含哪些namenode，为各个namenode起名 -->
	<property>
	  <name>dfs.ha.namenodes.ns1</name>
	  <value>master188,master189</value>
	</property>

	<!-- 名为master188的namenode的rpc地址和端口号，rpc用来和datanode通讯 -->
	<property>
	  <name>dfs.namenode.rpc-address.ns1.master188</name>
	  <value>master188:9000</value>
	</property>

	<!-- 名为master189的namenode的rpc地址和端口号，rpc用来和datanode通讯 -->
	<property>
	  <name>dfs.namenode.rpc-address.ns1.master189</name>
	  <value>master189:9000</value>
	</property>

	<!--名为master188的namenode的http地址和端口号，用来和web客户端通讯 -->
	<property>
	  <name>dfs.namenode.http-address.ns1.master188</name>
	  <value>master188:50070</value>
	</property>

	<!-- 名为master189的namenode的http地址和端口号，用来和web客户端通讯 -->
	<property>
	  <name>dfs.namenode.http-address.ns1.master189</name>
	  <value>master189:50070</value>
	</property>

	<!-- namenode间用于共享编辑日志的journal节点列表 -->
	<property>
	  <name>dfs.namenode.shared.edits.dir</name>
	  <value>qjournal://master188:8485;master189:8485;slave190:8485/ns1</value>
	</property>

	<!-- 指定该集群出现故障时，是否自动切换到另一台namenode -->
	<property>
	  <name>dfs.ha.automatic-failover.enabled.ns1</name>
	  <value>true</value>
	</property>

	<!-- journalnode 上用于存放edits日志的目录 -->
	<property>
	  <name>dfs.journalnode.edits.dir</name>
	  <value>/home/hadoop/hadoop-2.6.5/tmp/data/dfs/journalnode</value>
	</property>

	<!-- 客户端连接可用状态的NameNode所用的代理类 -->
	<property>
	  <name>dfs.client.failover.proxy.provider.ns1</name>
	  <value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
	</property>

	<!-- 一旦需要NameNode切换，使用ssh方式进行操作 -->
	<property>
	  <name>dfs.ha.fencing.methods</name>
	  <value>sshfence</value>
	</property>

	<!-- 如果使用ssh进行故障切换，使用ssh通信时用的密钥存储的位置 -->
	<property>
	  <name>dfs.ha.fencing.ssh.private-key-files</name>
	  <value>/root/.ssh/id_rsa</value>
	</property>

	<!-- connect-timeout超时时间 -->
	<property>
	  <name>dfs.ha.fencing.ssh.connect-timeout</name>
	  <value>30000</value>
	</property>
	```
8. 修改mapreduce核心配置 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/mapred-site.xml`  
	+ 注意！！！这个文件默认没有，只有一个模板文件，直接vi新建之后再粘贴过来就行了  
	```
	<?xml version="1.0"?>
	<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
	<!--
	  Licensed under the Apache License, Version 2.0 (the "License");
	  you may not use this file except in compliance with the License.
	  You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	  Unless required by applicable law or agreed to in writing, software
	  distributed under the License is distributed on an "AS IS" BASIS,
	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	  See the License for the specific language governing permissions and
	  limitations under the License. See accompanying LICENSE file.
	-->

	<!-- Put site-specific property overrides in this file. -->

	<configuration>
	  <!-- 采用yarn作为mapreduce的资源调度框架 -->
	  <property>
	    <name>mapreduce.framework.name</name>
	    <value>yarn</value>
	  </property>
	</configuration>
	```
9. 修改yarn核心配置 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/yarn-site.xml`  
	+ 默认文件进去之后是空的`<configuration></configuration>`  
	```
	<!-- 启用HA高可用性 -->
	<property>
	  <name>yarn.resourcemanager.ha.enabled</name>
	  <value>true</value>
	</property>

	<!-- 指定resourcemanager的名字 -->
	<property>
	  <name>yarn.resourcemanager.cluster-id</name>
	  <value>yrc</value>
	</property>

	<!-- 使用了2个resourcemanager,分别指定Resourcemanager的地址 -->
	<property>
	  <name>yarn.resourcemanager.ha.rm-ids</name>
	  <value>rm1,rm2</value>
	</property>

	<!-- 指定rm1的地址 -->
	<property>
	  <name>yarn.resourcemanager.hostname.rm1</name>
	  <value>master188</value>
	</property>

	<!-- 指定rm2的地址  -->
	<property>
	  <name>yarn.resourcemanager.hostname.rm2</name>
	  <value>master189</value>
	</property>

	<!-- 指定当前机器master188作为rm1 -->
	<property>
	  <name>yarn.resourcemanager.ha.id</name>
	  <value>rm1</value>
	</property>
	
	<!-- 后续修改一：指定二号机器master189作为rm2 -->
	<!-- <property>
	  <name>yarn.resourcemanager.ha.id</name>
	  <value>rm2</value>
	</property> -->
	
	<!-- 后续操作二：删除slave190中的指定某某机器为某某 -->

	<!-- 指定zookeeper集群机器 -->
	<property>
	  <name>yarn.resourcemanager.zk-address</name>
	  <value>master188:2181,master189:2181,slave190:2181</value>
	</property>

	<!-- NodeManager上运行的附属服务，默认是mapreduce_shuffle -->
	<property>
	  <name>yarn.nodemanager.aux-services</name>
	  <value>mapreduce_shuffle</value>
	</property>
	```
10. 修改从机列表 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/slaves`，默认进去显示的的localhost，直接删掉就行了  
	```
	# 别名映射模式
	master188
	master189
	slave190
	```
11. 拷贝hadoop到其他机器  
	```
	# 注意！！！在后续步骤中修改复制hadoop到其他服务器的命令，原始命令会导致文件全部位于hadoop中，没有hadoop-2.6.5的文件夹
	scp -r /home/hadoop/hadoop-2.6.5/ root@master189:/home/hadoop/hadoop-2.6.5/
	scp -r /home/hadoop/hadoop-2.6.5/ root@slave190:/home/hadoop/hadoop-2.6.5/
	```
12. 修改master189的yarn核心配置 `vi /home/hadoop/hadoop-2.6.5/etc/hadoop/yarn-site.xml`  
	```
	  <!-- 指定当前机器master189作为rm2 -->
	  <property>
	    <name>yarn.resourcemanager.ha.id</name>
	    <value>rm2</value>
	  </property>
	```
13. 删除slave190的yarn核心配置  
14. 接下来可以开始准备启动hadoop集群  
	+ 第一步，三台机器先删除`logs`和`tmp`文件夹  
	+ 第二步，所有机器开启journalnode，如需单独开启，去掉`daemons后的s`即可  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/hadoop-daemons.sh start journalnode`  
	+ 第三步，master188格式化hdfs
	+ `hdfs namenode -format`  
	+ `hdfs zkfc -formatZK`  
	+ 第四步，启动master188namenode  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/hadoop-daemon.sh start namenode`  
	+ 第五步，在master189上同步主从namenode信息，注意要先配置hadoop环境变量，并且不可以再格式化，具体参考这篇
	+ [文章：](https://www.cnblogs.com/yehuili/p/9942453.html)  
	+ `hdfs namenode -bootstrapStandby`  
	+ 第六步，master188分步骤启动hdfs，Yarn，ZookeeperFailoverController  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/start-dfs.sh`  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/start-yarn.sh`  
	+ 上述两步骤可以用一个命令：`sh /home/hadoop/hadoop-2.6.5/sbin/start-all.sh`  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/hadoop-daemon.sh start zkfc`  
	+ 第七步，master189上手动启动ResourceManager，ZookeeperFailoverController  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/yarn-daemon.sh start resourcemanager`  
	+ `sh /home/hadoop/hadoop-2.6.5/sbin/hadoop-daemon.sh start zkfc`  
	+ 查看进程 `jps`  
	+ 查看Namenode、ResourceManager状态  
	```
	hdfs haadmin -getServiceState master188
	yarn rmadmin -getServiceState rm1 
	hdfs haadmin -getServiceState master189
	yarn rmadmin -getServiceState rm2
	```
	+ 通过网址查看
	```
	[master188的hadoop](http://211.149.131.22:50070/)  
	[master189的hadoop](http://60.247.152.185:50070/)  
	[master188的yarn](http://211.149.131.22:8088/)  
	```
	+ 停止服务
	```
	sh /home/hadoop/hadoop-2.6.5/sbin/stop-all.sh  
	sh /home/hadoop/hadoop-2.6.5/sbin/yarn-daemon.sh stop resourcemanager  
	sh /home/hadoop/hadoop-2.6.5/sbin/hadoop-daemon.sh stop zkfc  
	```

#### yum安装java后，查看java安装目录
1. `which java`  
2. `ls -lrt /usr/bin/java`  
3. `ls -lrt /etc/alternatives/java`  

#### vim中删除多行  
1. 命令行模式下输入`set nu`，显示行号  
2. 命令行模式下输入`删除开始行号,删除行号 d`，例如`1,10 d`就是批量删除第一行到第十行  

#### 配置主机名映射，解决报错`Namenode for remains unresolved for ID`  
1. 查看主机名 `hostname` 或 `uname -n`  
2. 修改主机名 `vi /etc/hostname` 或 `hostnamectl set-hostname 主机名`  
3. 配置主机映射 `vi /etc/hosts`，添加 `ip hostname`，例如 `211.149.131.22 master188`  
	```
	# 每台机器都要配置以下主机名映射
	211.149.131.22  master188
	60.247.152.185  master189
	211.149.136.244  slave190
	211.149.131.22:8485  master188:8485
	60.247.152.185:8485  master189:8485
	211.149.136.244:8485  slave190:8485
	```
4. 测试 `ping 主机名`，例如 `ping master188`  

#### 配置全局环境变量，解决 `hdfs: command not found`  
1. 修改环境变量 `vi /etc/profile`  
2. 在文件末尾添加以下配置项
	```
	export HADOOP_HOME=/home/hadoop/hadoop-2.6.5/bin
	export PATH=$PATH:$HADOOP_HOME
	```
3. 退出编辑后 `source /etc/profile` 即可  

#### 开放端口`9000、50070、8485`  
**需要开放的端口**
1. 9000：自定义的？？？  
2. 50070：hdfs的NameNode的http访问端口  
3. 8485：hdfs的rpc服务    

**修改防火墙端口**
1. `vi /etc/sysconfig/iptables`使用`vim编辑器`修改`iptables`文件，按下`i`进入编辑模式  
2. 在初始端口那行下面添加下面几行，开放2181、2888/3888 端口  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 9000 -j ACCEPT`  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 50070 -j ACCEPT`  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 8485 -j ACCEPT`  
3. `service iptables restart`重启防火墙即可  

#### 增加ssh监听的端口，错误！！！不要使用！！！会导致启动失败！！！  
1. 修改ssh监听的配置文件 `vi /etc/ssh/sshd_config`  
2. 添加 `Port 端口`，例如 `Port 8485`，有几个添加几个  
3. 重启ssh服务 `service sshd restart`  
4. 查看端口占用情况 `lsof -i:8485`  

#### 杀掉进程
1. `jps`显示进程号  
2. 直接 `kill -9 进程号` 即可