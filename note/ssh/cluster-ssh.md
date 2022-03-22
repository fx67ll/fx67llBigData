# 服务器集群配置互相ssh免密登录

[参考文档————ssh免密登录服务器和scp的使用](https://juejin.cn/post/6844903848402927629)

## 查看本地有没有
ls ~/.ssh
## 没有就生成，执行命令后在~/.ssh/目录下，会新生成两个文件：id_rsa.pub和id_rsa。前者是你的公钥，后者是你的私钥
ssh-keygen
## 把公钥放到远程服务器，注意集群中的每个服务器都需要配置
ssh-copy-id user@remote -p port


### ssh复制秘钥代码，注意单独执行并且每个都需要单独输入密码
```
ssh-copy-id root@211.149.131.22 -p 22000
ssh-copy-id root@60.247.152.185 -p 22000
ssh-copy-id root@211.149.136.244 -p 22000
```

### `vi ~/.ssh/config`，ssh通信配置文件，注意集群中的每个服务器都需要配置
```
Host master188
HostName 211.149.131.22
User root
Port 22000

Host master189
HostName 60.247.152.185
User root
Port 22000

Host slave190
HostName 211.149.136.244
User root
Port 22000
```