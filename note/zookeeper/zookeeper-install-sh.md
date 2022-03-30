# Zookeeper 高可用集群安装

#### 简易记录，待后续补充拓展为自动化脚本并制作博客发布  

### 步骤
1. [官网](https://archive.apache.org/dist/zookeeper/)下载，本次使用版本***3.4.12***  
2. 上传到服务器的`/home/soft`文件夹下，home下一般是空的请自行创建`soft`文件夹  
3. 解压到`/home/hadoop`文件夹下，home下一般是空的请自行创建`hadoop`文件夹  
	+ `tar -zxvf /home/soft/zookeeper-3.4.12.tar.gz -C /home/hadoop/`
4. 修改配置  
	+ 进入zookeeper的conf目录，拷贝`zoo_sample.cfg`并重命名为`zoo.cfg`  
	```
	cd zookeeper-3.4.11/conf/
	cp zoo_sample.cfg zoo.cfg
	```
	+ 修改`zoo.cfg`，若原文件没有dataDir则直接添加  
	```
	vi /home/hadoop/zookeeper-3.4.12/conf/zoo.cfg
	
	# 添加下面这段内容到配置文件中
	// 这个路径会有默认路径直接修改就行了
	dataDir=/home/hadoop/zookeeper-3.4.12/data/zkData
	// 在最后添加，指定zookeeper集群主机及端口，机器数必须为奇数，ip和主机映射二选一
	server.1=211.149.131.22:2888:3888
	server.2=60.247.152.185:2888:3888
	server.3=211.149.136.244:2888:3888
	
	server.1=master188:2888:3888
	server.2=master189:2888:3888
	server.3=slave190:2888:3888
	```
	+ 在`/home/hadoop/zookeeper-3.4.12/data/zkData`目录下创建并编辑myid，三台机器分别是1/2/3  
5. 拷贝zookeeper到其他机器  
	```
	# 注意！！！在后续步骤中修改复制zookeeper到其他服务器的命令，原始命令会导致文件全部位于hadoop中，没有zookeepr-3.4.12的文件夹
	scp -r /home/hadoop/zookeeper-3.4.12/ root@master189:/home/hadoop/zookeeper-3.4.12/
	scp -r /home/hadoop/zookeeper-3.4.12/ root@slave190:/home/hadoop/zookeeper-3.4.12/
	```
6. 修改其他机器的myid文件，myid文件是作为当前机器在zookeeper集群的标识，注意要与`zoo.cfg`文件中的`server.`后面的数字要一致  

	

#### 常用命令
1. 进入Zookeeper目录 `cd /home/hadoop/zookeeper-3.4.12`  
2. 启动，停止，查看状态，重启（如果没有配置环境变量需要在bin目录下使用）  
	+ `/home/hadoop/zookeeper-3.4.12/bin/zkServer.sh start`  
	+ `/home/hadoop/zookeeper-3.4.12/bin/zkServer.sh stop`  
	+ `/home/hadoop/zookeeper-3.4.12/bin/zkServer.sh status`  
	+ `/home/hadoop/zookeeper-3.4.12/bin/zkServer.sh restart`  
	+ 如果配置了全局环境变量可以直接使用
	+ `zkServer.sh start/stop/status/restart`  
3. 如果启动失败，可以去bin目录下查看`zookeeper.out`日志文件  
	+ 清空日志文件命令 `echo "" > zookeeper.out`
	+ 查看日志文件 `vi /home/hadoop/zookeeper-3.4.12/bin/zookeeper.out`  

#### 如果jps命令没有，则说明是因为java环境未安装  
`yum install -y java-1.8.0-openjdk-devel.x86_64`  

#### 如果启动失败，需要考虑是否是防火墙端口未开放
[参考这篇文章，主要是2181、2888和3888端口](https://github.com/fx67ll/fx67llLinux/blob/master/serve-blog/2021/2021-11/ping-web.md)  

**需要开放的端口**
1. 2181：对cline端提供服务  
2. 3888：选举leader使用  
3. 2888：集群内机器通讯使用（Leader监听此端口）  

**修改防火墙端口**
1. `cd /etc/sysconfig`进入该目录，检查是否存储了`iptables`文件  
2. `vi iptables`使用`vim编辑器`修改`iptables`文件，按下`i`进入编辑模式  
3. 在初始端口那行下面添加下面几行，开放2181、2888/3888 端口  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 2181 -j ACCEPT`  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 2888 -j ACCEPT`  
	+ `-A INPUT -p tcp -m state --state NEW -m tcp --dport 3888 -j ACCEPT`  
4. `service iptables restart`重启防火墙即可  

#### 如果还有错误，那么要考虑`data/zkData`，目录下是否有myid文件，我之前之前但是后面莫名其妙没了  

#### 注意云服务器使用ssh映射别名可能会失败，改成ip就好了，如何使用别名以后再说吧  

#### 三台服务器分别启动之后查看状态，看到以下代码后显示为集群启动成功
```
ZooKeeper JMX enabled by default
Using config: /home/hadoop/zookeeper-3.4.12/bin/../conf/zoo.cfg
Mode: follower

ZooKeeper JMX enabled by default
Using config: /home/hadoop/zookeeper-3.4.12/bin/../conf/zoo.cfg
Mode: leader

ZooKeeper JMX enabled by default
Using config: /home/hadoop/zookeeper-3.4.12/bin/../conf/zoo.cfg
Mode: follower
```

#### Zoopkeeper环境变量配置待补充  

#### 查看Zookeeper日志
1. 首先需要自己有slf4j.jar和zookeeper.jar，直接拷贝到同一目录就行了  
2. 进入日志目录，注意地址可能会不同  
	+ `cd /home/hadoop/zookeeper-3.4.12/data/zkData/version-2`  
3. 使用jar包将二进制的log转换一下  
	+ `java -classpath .:slf4j-api-1.7.25.jar:zookeeper-3.4.12.jar org.apache.zookeeper.server.LogFormatter log.200000001 > log-200000001.txt`  

#### Zookeeper配置全局环境变量  
1. 修改环境变量 `vi /etc/profile`  
2. 在文件末尾添加以下配置项
	```
	export ZOOKEEPER_HOME=/home/hadoop/zookeeper-3.4.12
	export PATH=$PATH:$ZOOKEEPER_HOME/bin
	```
3. 退出编辑后 `source /etc/profile` 即可  