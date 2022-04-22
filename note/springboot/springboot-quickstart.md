# SpringBoot快速入门 🕹️0.1.0  


## 使用xml还是注解
1. 应用的基本配置使用xml，比如数据源和资源文件等  
2. 业务开发使用注解，比如service注入bean  
3. 但是xml越来越多导致越来越臃肿，最终发展到使用完全基于注解开发  


## 注解
### 声明Bean注解
> @Component 组件没有明确规定其角色，作用在类级别上声明当前类为一个业务组件，被`Spring IOC 容器`维护  
> @Service 在业务逻辑层（Service）类级别进行声明  
> @Registory 在数据访问层（Dao）类级别进行声明  
> @Controller 在展现层（MVC）使用，标注当前类为一个控制器  

### 注入Bean注解
> @Autowired 它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作，通过`@Autowired`的使用来消除set、get方法  
> @Inject 作用同上，是`JSR-330 标准`  
> @Resource 作用同上，是`JSR-250 标准`  
*以上三种注解在Set方法或属性上声明，一般情况下更习惯声明在属性上，代码简洁清晰*

### 配置与获取Bean注解
> @Configuration 将当前类声明为一个配置类，相当于一个xml配置文件  
> @ComponentScan 自动扫描包下标注有@Repository @Service @Controller  
> @Component 注解的类并有`Spring IOC 容器`进行实例化和维护  
> @Bean 作用于方法上，声明当前方法的返回值是一个`Bean对象`，相当于`xml文件`中`<bean>`声明当前方法返回一个`bean对象`  
> @Value 获取`properties文件`指定的`key/value`  
#### pom.xml
作用是添加坐标相关配置，主要是各种依赖jar包  

### 组合注解和元注解
所谓元注解其实就是可以注解到别的注解上的注解，被注解的注解称之为组合注解，组合注解具备元注解的功能，主要的作用是消除重复注解  

### 自定义注解
个性化的定义自己所需要的功能并声明一个注解，简化工程，可以参考文章————[SPRINGBOOT自定义注解](https://www.cnblogs.com/mizhiniurou/p/10890951.html)学习  

### 常用注解
可以参考文章————[SpringBoot常用注解集合](https://blog.csdn.net/qq_53324833/article/details/121079368)学习  


## 习惯大于配置目标
Spring Boot 的目标是快速运行，快速创建web应用，并独立机型部署（jar包方式，war包方式），相比于Spring框架是全新重写的框架  


## 核心配置
### 修改Banner图标
主要是通过修改`/src/main/resources`目录下的`banner.txt`文件，如果没有则默认使用SpringBoot初始Banner  
可以[个性化制作Banner的网站](http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20)制定相应的txt文件  

### 全局配置
默认是`application.properties`或者`application.yml`  
坐标依赖都配置在`pom.xml`中，如果添加了依赖以后标红可以使用`Maven -> Reload project`即可  
#### 入口类依靠组合注解`@SpringBootApplication`
> @SpringBootConfiguration 本身是一个配置类，启动类启动的时候会加载
> @EnableAutoConfiguration 组合了`@AutoConfigurationPackage`&`@Import(AutoConfigurationImportSelector.class)`  
> @AutoConfigurationPackage 底层是一个@Import(AutoConfigurationPackage.Registrar.class)，其会把启动类的包下组合都扫描到Spring容器中  
> @AutoConfigurationImportSelector 读取大量的自动配置类，完成自动配置，其读取的是classpath下的`META-INF/spring.factories`下的配置文件  
```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
```

### Profile配置————区分生产和开发环境
通过在`application.yml`中设置`spring.profiles.active=test/dev/prod`来动态切换不同环境，例如：
```
# 开发环境配置文件
application-dev.yml
server:
  prot: 8098

# 测试环境配置文件
application-test.yml
server:
  prot: 8097

# 生产环境配置文件
application-prod.yml
server:
  prot: 8099

# 主配置文件
application.yml
spring:
  profiles:
    active: dev
```

### 日志配置
SpringBoot默认使用`LogBack`日志系统，一般主流的日志都是用`log4j`日志系统  
#### 如果重复启动Spring项目，可能会有端口占用的报错
1. 思路是杀死占用端口的进程即可，主要是下面两个命令
2. 使用`netstat -aon|findstr "被占用的端口"`或者`tasklist |findstr "进程名称"`查询到端口的进程号  
3. 使用`taskkill /f /t /im "进程名称"`或者`taskkill /f /t /pid "进程PID"`杀死进程即可  


## 静态资源
默认配置下，我们可以在`resources`资源目录下存放web应用静态资源文件  
自定义静态资源路径，可以通过在`spring.resources.static-locations`后面追加一个配置`classpath:/你自定义的配置目录/`，例如：
```
# application.yml
spring:
  resources:
	# 多个目录使用逗号隔开
    static-loaction: classpath:/public/,classpath:/static/,classpath:/fx67ll/
```


## 打包和部署
### jar包
1. 一般用于编写依赖工具包
2. 打包
	+ 在IDEA`Run/Debug Configurations`下`Command line`配置`clean complie package -Dmaven.test.skip=true`执行打包命令  
	+ `target`目录得到待部署的项目文件  
2. 部署
	+ 在dos窗口中，执行命令`java -jar jar包所在的本地目录`  

### war包
1. 在生产环境中最为常见的部署方式  
2. 修改`pom.xml`，设置打包模式为war包
	```
	<groupId>com.fx67ll</groupId>
    <artifactId>springboot-quickstart</artifactId>
    <version>0.1.0</version>
	<!--设置为war包模式-->
    <packaging>war</packaging>
	```
3. 忽略内嵌Tomcat
	```
	<!--设置为外部已提供，表示忽略-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	```
4. 配置生成的war包名称
	```
	<build>
	<!--设置war包名称-->
        <finalName>fx67ll-springboot-quickstart-test</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
	```
5. 修改`Starter`类，添加容器启动加载文件（类似读取web.xml文件）  
	+ 这里通过继承`SpringBootServletInitiallizer`类并重写`configure`方法来实现  
	+ 在部署项目的时候指定外部Tomcat读取项目入口方法
	```
	@SpringBootApplication
	public class Starter extends SpringBootServletInitializer {
		
		public static void main(String[] args) {
			SpringApplication.run(Starter.class);
		}
		
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
			return builder.sources(Starter.class);
		}
	}
	```
6. 打包
	+ 在IDEA`Run/Debug Configurations`下`Command line`配置`clean complie package -Dmaven.test.skip=true`执行打包命令  
	+ `target`目录得到待部署的项目文件  
7. 部署并访问
	+ 放置到外部tomcat中，执行bin目录下start脚本即可  


[参考教程 ———— 两天搞定SpringBoot框架](https://www.bilibili.com/video/BV16i4y197zh)  
[参考文档 ———— JavaSpringBoot 中 @Autowired用法](https://blog.csdn.net/weixin_41290863/article/details/111568023)  
[参考文档 ———— SpringBoot - @Configuration、@Bean注解的使用详解（配置类的实现）](https://www.hangge.com/blog/cache/detail_2506.html)  
[参考文档 ———— 【Spring Boot】Spring基础 —— 组合注解与元注解](https://blog.csdn.net/the_ZED/article/details/105456946)  


我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/springboot/springboot-quickstart.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***