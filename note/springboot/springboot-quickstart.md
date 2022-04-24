# SpringBoot快速入门 🕹1.0.0  


## MVC
### 什么是MVC
1. MVC三层架构是指：视图层 View、服务层 Service，与持久层 Dao，它们分别完成不同的功能  
	+ View 层：用于接收用户提交请求的代码在这里编写  
	+ Service 层：系统的业务逻辑主要在这里完成  
	+ Dao 层：直接操作数据库的代码在这里编写  
2. 为了更好的降低各层间的耦合度，在三层架构程序设计中，采用面向抽象编程，即上层对下层的调用，是通过接口实现的，而下层对上层的真正服务提供者，是下层接口的实现类  
3. 服务标准（接口）是相同的，服务提供者（实现类）可以更换，这就实现了层间解耦合  

### MVC 架构程序的工作流程
1. 用户通过 View 页面向服务端提出请求，可以是表单请求、超链接请求、AJAX 请求等  
2. 服务端 Controller 控制器接收到请求后对请求进行解析，找到相应的 Model 对用户请求进行处理  
3. Model 处理后，将处理结果再交给 Controller  
4. Controller 在接到处理结果后，根据处理结果找到要作为向客户端发回的响应 View 页面，页面经渲染（数据填充）后，再发送给客户端  


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
**可以参考文章————[SpringBoot常用注解集合](https://blog.csdn.net/qq_53324833/article/details/121079368)详细学习，这里后期会补上说明**  
#### @RestController、@ResponseBody、@RequestBody
1. 相当于`@Controller + @ResponseBody`两个注解的结合，返回`JSON`数据不需要在方法前面加`@ResponseBody`注解了，
	但使用@RestController这个注解，就不能返回jsp、html页面，视图解析器无法解析jsp、html页面v
2. `@ResponseBody`表示该方法的返回结果直接写入`HTTP response body`中，一般在异步获取数据时使用（也就是AJAX），
	在使用`@RequestMapping`后，返回值通常解析为跳转路径，但是加上`@ResponseBody`后返回结果不会被解析为跳转路径，而是直接写入`HTTP response body`中，
	比如异步获取`JSON`数据，加上`@ResponseBody`后，会直接返回`JSON`数据  
3. `@RequestBody`将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象  
#### @MapperScan、@Mapper
1. @Mapper注解：
	+ 作用：在接口类上添加了@Mapper，在编译之后会生成相应的接口实现类  
	+ 添加位置：接口类上面  
	+ 如果想要每个接口都要变成实现类，那么需要在每个接口类上加上`@Mapper`注解，比较麻烦，解决这个问题用`@MapperScan`注解
2. @MapperScan注解：
	+ 作用：指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类  
	+ 添加位置：是在Springboot启动类上面添加  
	+ 添加`@MapperScan("com.winter.da")`注解以后，`com.winter.dao`包下面的接口类，在编译之后都会生成相应的实现类  


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


## 事务控制
### 声明式事务
**可以参考文章————[SpringBoot声明式事务的简单运用](https://blog.csdn.net/justry_deng/article/details/80828180)详细学习，这里后期会补上说明**
主要应用在新增修改删除上，应用注解即可  


## 全局异常
### 使用@ControllerAdvice配合@ExceptionHandler
**可以参考文章————[Springboot系列-@ControllerAdvice使用](https://blog.csdn.net/wangxinyao1997/article/details/103710843)详细学习，这里后期会补上说明**
此注解其实是一个增强的`Controller`，使用这个`Controller`，可实现三个方面的功能，因为这是SpringMVC提供的功能，所以可以在springboot中直接使用
1. 全局异常处理 （@ExceptionHandler）
2. 全局数据绑定 （@InitBinder）
3. 全局数据预处理 （@ModelAttribute）
```
package com.fx67ll.springboot.exceptions;

import com.fx67ll.springboot.po.vo.ResultInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class TestGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultInfo exceptionHandler(Exception exception) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(978);
        resultInfo.setMsg("全局异常拦截，操作失败！");
//        if (exception instanceof ParamsException) {
//            ParamsException paramsException = (ParamsException) exception;
//            resultInfo.setMsg(paramsException.getMsg());
//            resultInfo.setCode(paramsException.getCode());
//        }
        return resultInfo;
    }
}
```


## 数据校验
### 为什么要进行后端数据校验
数据的校验是交互式网站一个不可或缺的功能，前端的`js校验`可以涵盖大部分的校验职责，如用户名唯一性，生日格式，邮箱格式校验等等常用的校验。
但是一般前端传来的数据是不可信的，前端校验过了，后端也应该重新校验，因为不排除用户绕过浏览器直接通过`Http工具`向后端请求的情况。
所以服务端的数据校验也是必要的，可以防止脏数据落到数据库中，如果数据库中出现一个非法的邮箱格式，也会让运维人员头疼不已。

### 如何进行后端数据校验
1. `SpringBoot`中一般使用`Spring Validation`来进行后端数据校验，它是对`Hibernate Validation`进行了二次封装，
	在`SpringMVC`模块中添加了自动校验，并将校验信息封装进了特定的类中  
2. 在使用时我们只需要引入`spring-boot-starter-web`依赖即可，该模块会自动依赖`spring-boot-starter-validation`  

### Spring Validation 常用注解
> @Null：被注释的元素必须为`null`  
> @NotNull：被注释的元素不能为`null`，可以为空字符串  
> @AssertTrue：被注释的元素必须为`true`  
> @AssertFalse：被注释的元素必须为`false`  
> @Min(value)：被注释的元素必须是一个数字，其值必须大于等于指定的最小值  
> @Max(value)：被注释的元素必须是一个数字，其值必须小于等于指定的最大值  
> @DecimalMin(value)：被注释的元素必须是一个数字，其值必须大于等于指定的最小值  
> @DecimalMax(value)：被注释的元素必须是一个数字，其值必须小于等于指定的最大值  
> @Size(max,min)：被注释的元素的大小必须在指定的范围内  
> @Digits(integer,fraction)：被注释的元素必须是一个数字，其值必须在可接受的范围内  
> @Past：被注释的元素必须是一个过去的日期  
> @Future：被注释的元素必须是一个将来的日期  
> @Pattern(value)：被注释的元素必须符合指定的正则表达式  
> @Email：被注释的元素必须是电子邮件地址  
> @Length：被注释的字符串的大小必须在指定的范围内  
> @Range：被注释的元素必须在合适的范围内  
> @URL：被注解的元素必须是一个`URL`  
> @NotEmpty：用在集合类上，不能为`null`，并且长度必须大于0  
> @NotBlank：只能作用在`String`上，不能为`null`，而且调用`trim()`后，长度必须大于0  

### 自定义注解
**可以参考文章————[Spring自定义注解(validation)](https://blog.csdn.net/ileopard/article/details/123485111)详细学习，这里后期会补上说明**  

### 示例代码
1. `/com/fx67ll/springboot/controller/UserController.java`在传参的位置添加`@Vaild`注解表示这里的参数需要校验，需要注意JSON格式和表单格式传过来的参数异常会有些区别，需要在后面注意
	```
    // 添加用户
	@PutMapping("/adduser")
    public ResultInfo saveUser(@RequestBody @Valid User user) {
        ResultInfo resultInfo = new ResultInfo();
        userService.saveUser(user);
        return resultInfo;
    }
	```
2. 在`Bean`文件`/com/fx67ll/springboot/dao/User.java`中私有字段上使用注解来校验，不贴所有代码了，仅贴部分重点代码
	```
    @NotBlank(message = "用户名称不能为空！")
    private String userName;
	
    @NotBlank(message = "用户密码不能为空！")
    @Length(min = 6, max = 20, message = "密码长度最少六位且最多二十位！")
    private String userPwd;
	```
3. 在全局自定义异常拦截中`/com/fx67ll/springboot/exceptions/TestGlobalExceptionHandler.java`向用户返回错误代码和信息
	```
	package com.fx67ll.springboot.exceptions;

	import com.fx67ll.springboot.po.vo.ResultInfo;
	import org.springframework.web.bind.MethodArgumentNotValidException;
	import org.springframework.web.bind.annotation.ControllerAdvice;
	import org.springframework.web.bind.annotation.ExceptionHandler;
	import org.springframework.web.bind.annotation.ResponseBody;

	@ControllerAdvice
	public class TestGlobalExceptionHandler {

		@ExceptionHandler(value = Exception.class)
		@ResponseBody
		public ResultInfo exceptionHandler(Exception exception) {
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setCode(978);
			resultInfo.setMsg("全局异常拦截，操作失败！");
			// 全局数据校验，注意！！！使用 json 请求体调用接口，校验异常抛出 MethodArgumentNotValidException
			if (exception instanceof MethodArgumentNotValidException) {
				MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
				resultInfo.setCode(1023);
				resultInfo.setMsg(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
			}
			return resultInfo;
		}
	}
	```


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

### 热部署
热部署，就是在应用正在运行的时候升级软件，却不需要重新启动应用，主要应用在开发过程中  
#### 热部署原理
1. `spring-boot-devtools`是一个为开发者服务的一个模块，其中最重要的功能就是自动应用代码更改到最新的App上面去，
	原理是在发现代码有更改之后，重新启动应用，但是速度比手动停止后再启动还要更快，更快指的不是节省出来的手工操作的时间  
2. 其深层原理是使用了两个`ClassLoader`，一个`Classloader`加载那些不会改变的类（第三方Jar包），另一个`ClassLoader`加载会更改的类，称为`restart ClassLoader`，
	这样在有代码更改的时候，原来的`restart ClassLoader`被丢弃，重新创建一个`restart ClassLoader`，由于需要加载的类相比较少，所以实现了较快的重启时间，*大概在5秒以内*  
#### devtools原理
1. devtools会监听classpath下的文件变动，并且会立即重启应用（发生在保存时机）*注意：因为其采用的虚拟机机制，该项重启是很快的*  
2. devtools可以实现页面热部署（即页面修改后会立即生效，这个可以直接在`application`文件中配置`spring.thymeleaf.cache=false`来实现 *注意：不同的模板配置不一样*
#### 热部署主要步骤
1. 在`pom.xml`中添加依赖，同时添加`devtools`生效标志插件  
	```
	<!--热部署插件devtools-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<!--表示当前这个项目被继承之后，这个不向下传递-->
		<optional>true</optional>
	</dependency>
	
	<plugin>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-maven-plugin</artifactId>
		<!--在原有的基础上添加-->
		<configuration>
			<!--如果没有该配置，热部署插件devtools不生效-->
			<fork>true</fork>
		</configuration>
	</plugin>
	```
2. 修改`application.yml`全局配置文件，在`application.yml`中配置`spring.devtools.restart.enable=false`，此时`restart`类加载器还会初始化，但不会监视文件更新
	```
	spring:
	  # 热部署配置
	  devtools:
		restart:
		  enabled: true
		  # 设置重启的目录，添加目录的文件需要restart
		  additional-paths: src/main/java
		  # 解决项目启动重新编译后接口报404的问题
		  poll-interval: 3000
		  quiet-period: 1000
	```
3. 修改 IDEA 配置
	+ 修改了java类之后，IDEA 默认是不自动编译的，而`spring-boot-devtools`又是监测`classpath`下的文件发生变化才会重启应用，所以需要设置 IDEA 的自动编译  
	+ 设置自动配置 `File -> Settings -> Build -> Complier -> Build Project automatically`  
	+ ~~修改`Register`属性，执行快捷键`ctrl + shift + alt + /`，选择`Register`，勾上`Complier autoMake allow when app running`~~  
	+ *注意 IDEA 2021.2.3 版本中没有上面的选项*，迁移到了`File -> Settings -> Tools -> Advanced Settings -> Complier -> Allow auto-make to start......`
4. 配置完需要重启一下，然后有修改的话项目会自动更新，但是如果是自动触发的话，会造成频繁更新，对硬件有一定的负担，所以可以改成手动触发模式
	+ 点击右上角 `Run/Debug Configurations`  
	+ 选择下拉 `Configuration -> Spring Boot -> Running Application Update Policies -> On 'Update' action`  
	+ 选择 `Update classes and resources`  
	+ 如果有更新可以，使用快捷键 `Ctrl + F10` 重新编译  
5. 快捷键`Ctrl + F9`，使用热部署重新启动  


## 单元测试
### 依赖
```
<!--单元测试-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
```

### Service业务层————业务逻辑方法测试
*需要注意的是：*
1. 如果在和`main文件夹`平级的`test文件夹`下新建了`java文件夹`，但是无法新建`java class`文件
2. 那么就需要右键文件夹 `Mark Directory as -> Test Sources Root`之后，文件夹变绿即可  
```
# 示例代码
package com.fx67ll.springboot.service;

import com.fx67ll.springboot.Starter;
import com.fx67ll.springboot.po.User;
import com.fx67ll.springboot.query.UserQuery;
import com.fx67ll.springboot.srevice.UserService;
import com.github.pagehelper.PageInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Service业务方法测试
 *
 * Junit中的RunWith注解 表示该类是单元测试的执行类
 * SpringRunner 是 spring-test 提供的测试执行单元类（是Spring单元测试中SpringJUnit4ClassRunner的新名字）
 * SpringBootTest注解 是执行测试程序的引导类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class TestUserService {

    // 日志的使用
    private Logger logger = LoggerFactory.getLogger(TestUserService.class);

    @Resource
    private UserService userService;

    @Before
    public void before() {
        logger.info("单元测试开始......");
    }

    @Test
    public void testQueryUserById() {
        logger.info("测试根据用户id查询......");

        User user = userService.queryUserById(1);
        logger.info("用户记录: {}", user.toString());
    }

    @Test
    public void testSelectUserListByParams() {
        logger.info("测试根据分页条件查询用户列表......");

        UserQuery userQuery = new UserQuery();
        PageInfo<User> pageInfo = userService.selectUserListByParams(userQuery);
        logger.info(pageInfo.toString());
    }

    @After
    public void after() {
        logger.info("单元测试结束......");
    }
}
```

### controller控制层————接口方法测试
#### 使用MockMVC进行测试
`MockMvc`是由`spring-test`包提供，实现了对`Http请求`的模拟，能够直接使用网络的形式，转换到`Controller`的调用，使得测试速度快、不依赖网络环境。
同时提供了一套验证的工具，结果的验证十分方便  
#### 什么是Mock
在面向对象的程序设计中，模拟对象`mock object`是以可控的方式模拟真实对象行为的假对象。
在编程过程中，通常通过模拟一些输入数据，来验证程序是否达到预期结果  
#### 接口MockMvcBuilder
提供一个唯一的`build方法`，用来构造`MockMvc`。
主要有两个实现：`StandaloneMockMvcBuilder`和`DefaultMockMvcBuilder`，分别对应两种测试方式，
即独立安装和集成Web环境测试（并不会集成真正的`web环境`，而是通过相应的`Mock API`进行模拟测试，无须启动服务器）。
MockMvcBuilders提供了对应的创建方法`standaloneSetup`方法和`webAppContextSetup`方法，在使用时直接调用即可。
```
# 示例代码
# PS：虽然提示测试通过，但是控制台一直没有打印出返回信息的记录，后期有空看看
package com.fx67ll.springboot.controller;

import com.fx67ll.springboot.Starter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
@AutoConfigureMockMvc
public class TestUserController {

    // 日志的使用
    private Logger logger = LoggerFactory.getLogger(TestUserController.class);

    @Autowired
    private MockMvc mockMvc;

    /**
     * 模拟测试用户列表查询
     * 其实就在模拟真实环境下前端对后端发起的请求
     */
    @Test
    public void apiTestSelectUserListByParams() throws Exception {

        logger.info("开始模拟发送查询用户列表的请求......");

        // 构建请求
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/list")
                .contentType("text/html") // 设置请求头信息
                .accept(MediaType.APPLICATION_JSON); // 设置请求Accept头信息

        // 发送请求
        ResultActions perform = mockMvc.perform(requestBuilder);

        // 校验请求结果
        perform.andExpect(MockMvcResultMatchers.status().isOk());

        // 获取执行完成后返回的结果
        MvcResult mvcResult = perform.andReturn();

        // 得到执行后的响应
        MockHttpServletResponse response = mvcResult.getResponse();

        // 打印结果
        logger.info(String.valueOf(response.getContentLength()));
        logger.info("响应状态: ", response.getStatus());
        logger.info("响应信息: ", response.getContentAsString());

        logger.info("结束模拟发送查询用户列表的请求......");
    }

    @Test
    public void apiTestQueryUserByUsername() throws Exception {

        logger.info("开始模拟根据用户名查询用户记录的请求......");

        // 构建请求并发送
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/name/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // 打印结果
        logger.info("响应状态: ", mvcResult.getResponse().getStatus());
        logger.info("响应信息: ", mvcResult.getResponse().getContentAsString());

        logger.info("结束模拟根据用户名查询用户记录的请求......");
    }
}
```


## Swagger2文档工具
### 依赖
在`pom.xml`中添加以下代码
```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.9.2</version>
</dependency>
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.9.2</version>
</dependency>
```

### 常用注解
**可以参考文章————[swagger2 注解说明](https://blog.csdn.net/xiaojin21cen/article/details/78654652)详细学习，这里后期会补上说明**  
#### @Api
*主要是用在请求类上，用于说明该类的作用*
```
# 示例
@Api(tags = "xx模块")
```
#### @ApiOperation
*主要是用在请求的方法上，说明方法的作用*
```
# 示例
@ApiOperation(value = "xx方法的作用", notes = "xx方法的备注说明")
```
#### @ApiImplicitParams、@ApiImplicitParam
*主要是用在请求的方法上，说明方法的参数*
```
# 详细参数说明
@ApiImplicitParams：用在请求的方法上，包含一组参数说明
	@ApiImplicitParam：对单个参数的说明	    
	    name：参数名
	    value：参数的说明、描述
	    required：参数是否必须必填
	    paramType：参数放在哪个地方
	        · query --> 请求参数的获取：@RequestParam
	        · header --> 请求参数的获取：@RequestHeader	      
	        · path（用于restful接口）--> 请求参数的获取：@PathVariable
	        · body（请求体）-->  @RequestBody User user
	        · form（普通表单提交）	   
	    dataType：参数类型，默认String，其它值dataType="Integer"	   
	    defaultValue：参数的默认值
	
# 单个参数示例	
@ApiImplicitParam(name = "xxx", value = "xxx", required = true, paramType = "path", dataType = "String", defaultValue = "")

# 多个参数示例
@ApiImplicitParams({
	@ApiImplicitParam(name = "xxxa", value = "xxxa", required = true, paramType = "body", dataType = "String", defaultValue = ""),
	@ApiImplicitParam(name = "xxxb", value = "xxxb", required = true, paramType = "body", dataType = "String", defaultValue = ""),
})
```
#### @ApiResponses、@ApiResponse
*主要是用在请求的方法上，说明错误响应的信息*
```
# 详细参数说明
@ApiResponses：响应状态的说明。是个数组，可包含多个 @ApiResponse
	@ApiResponse：每个参数的说明
	    code：数字，例如400
	    message：信息，例如"请求参数没填好"
	    response：抛出异常的类
	
# 多个参数示例，一般响应都是多个code，所以不写单个参数的示例了
@ApiResponses({
		@ApiResponse(code = 200, message = "请求成功"),
		@ApiResponse(code = 578, message = "请求参数错误"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
})

```
#### @ApiModel、@ApiModelProperty 
1. @ApiModel 经常用于请求的入参对象和响应返回值对象的描述  
	+ 入参是对象，即 @RequestBody 时， 用于封装请求（包括数据的各种校验）数据  
	+ 返回值是对象，即 @ResponseBody 时，用于返回值对象的描述  
2. @ApiModelProperty 用于每个属性上面，说明属性的含义
```
# 示例
@ApiModel(description = "用户实体类")
public class User {
    @ApiModelProperty(value = "用户名", required = true, example = "0")
    private Integer id;
    
	@ApiModelProperty(value = "用户ID", required = true, example = "fx67ll")
    private String userName;
    
	@ApiModelProperty(value = "用户密码", required = true, example = "xxxxxxxx")
    private String userPwd;
}
```


## 分布式缓存工具Ehcache
### 什么是Ehcache
`EhCache`是一个`纯Java`的进程内缓存框架，具有快速、精干等特点，是`Hibernate`中默认`CacheProvider`。  
`Ehcache`是一种广泛使用的开源`Java分布式缓存`，主要面向通用缓存，`Java EE`和`轻量级容器`。  
它具有内存和磁盘存储，缓存加载器，缓存扩展，缓存异常处理程序，一个`gzip`缓存`servlet`过滤器，支持`REST API`和`SOAP API`等特点。  

### SpringCache相关注解
SpringBoot缓存实现内部使用SpringCache实现缓存控制，这里集成Ehcache实际上是对SpringCache抽象的一种实现
**可以参考文章————[Spring Cache 简介](https://www.jianshu.com/p/9d3c58ecf8ff)详细学习，这里后期会补上说明**  
#### @EnableCaching
开启缓存功能，一般放在启动类上  
#### @CacheConfig
当我们需要缓存的地方越来越多，你可以使用`@CacheConfig(cacheNames = {"cacheName"})`注解在`Class`之上来统一指定`value`的值，
这时可省略`value`，如果你在你的方法依旧写上了`value`，那么依然以方法的`value`值为准  
#### @Cacheable
根据方法对其返回结果进行缓存，下次请求时，如果缓存存在，则直接读取缓存数据返回；如果缓存不存在，则执行方法，并把返回的结果存入缓存中，一般用在查询方法上  
*注意`value`后面要使用`ehcache.xml`文件中所列的`cache.name`*
```
# 单个参数示例代码
@Cacheable(value = "fx67llCache", key = "#xxx")

# 多个参数示例，采用拼接的方式
@Cacheable(value = "fx67llCache", key = "#xxx.xxx + '-' + #xxx.xxx + '-' + #xxx.xxx")
```
#### @CachePut
使用该注解标志的方法，每次都会执行，并将结果存入指定的缓存中。其他方法可以直接从响应的缓存中读取缓存数据，而不需要再去查询数据库，一般用在新增方法上  
```
# 示例代码
@CachePut(value = "fx67llCache", key = "#xxx.xxx")
```
#### @CacheEvict
使用该注解标志的方法，会清空指定的缓存，一般用在更新或者删除方法上  
```
# 示例代码
@CacheEvict(value = "fx67llCache", key = "#xxx")
```
#### @Caching
该注解可以实现同一个方法上同时使用多种注解  

### Ehcache的使用
1. 在`pom.xml`添加依赖
	```
	<!--Ehcache工具依赖-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-cache</artifactId>
	</dependency>
	<dependency>
		<groupId>net.sf.ehcache</groupId>
		<artifactId>ehcache</artifactId>
	</dependency>
	```
2. 添加`ehcache.xml`文件
	```
	<?xml version="1.0" encoding="UTF-8"?>
	<ehcache name="fx67llCache">
		<!--
		   diskStore：为缓存路径，ehcache分为内存和磁盘两级，此属性定义磁盘的缓存位置。参数解释如下：
		   user.home – 用户主目录
		   user.dir  – 用户当前工作目录
		   java.io.tmpdir – 默认临时文件路径
		 -->
		<diskStore path="D:\Java\test-ehcache-cache"/>

		<!--
		   defaultCache：默认缓存策略，当ehcache找不到定义的缓存时，则使用这个缓存策略。只能定义一个。
		 -->

		<!--
		  name:缓存名称。
		  maxElementsInMemory:缓存最大数目
		  maxElementsOnDisk：硬盘最大缓存个数。
		  eternal:对象是否永久有效，一但设置了，timeout将不起作用。
		  overflowToDisk:是否保存到磁盘，当系统当机时
		  timeToIdleSeconds:设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
		  timeToLiveSeconds:设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
		  diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
		  diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
		  diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
		  memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
		  clearOnFlush：内存数量最大时是否清除。
		  memoryStoreEvictionPolicy:可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
			   FIFO，first in first out，这个是大家最熟的，先进先出。
			   LFU， Less Frequently Used，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
			   LRU，Least Recently Used，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
	   -->
		<defaultCache
				maxElementsInMemory="10000"
				eternal="false"
				timeToIdleSeconds="120"
				timeToLiveSeconds="120"
				maxElementsOnDisk="10000000"
				diskExpiryThreadIntervalSeconds="120"
				memoryStoreEvictionPolicy="LRU"/>

		<cache
				name="fx67llCache"
				eternal="false"
				maxElementsInMemory="100"
				overflowToDisk="false"
				diskPersistent="false"
				timeToIdleSeconds="0"
				timeToLiveSeconds="300"
				memoryStoreEvictionPolicy="LRU"/>

	</ehcache>
	```
3. 在`application.yml`添加缓存配置
	```
	# Ehcache 缓存配置
	cache:
	  ehcache:
	    config: classpath:ehcache.xml
	```
4. 在入口类添加`@EnableCaching`注解，表示开启缓存  
5. Java Bean 对象实现序列化，`public class User implements Serializable`  
6. 在需要使用的地方使用现关注解，实现缓存可以减少从数据库查询的次数  


## 定时调度工具Quartz
**可以参考文章————[Quartz定时调度](https://blog.csdn.net/yesirwu/article/details/97683166)详细学习，这里后期会补上说明**  
### 什么是Quartz
在日常项目运行中，我们总会有需求在某一时间段周期性的执行某个动作，比如每天在某个时间段导出报表，或者每隔多久统计一次现在在线的用户量等。
在SpringBoot中有Java自带的`java.util.Timer`类，也可以在启动类添加`@EnableScheduling`注解引入定时任务环境

### Quartz的使用
1. 在`pom.xml`添加依赖
	```
	<!--Quartz工具依赖-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-quartz</artifactId>
	</dependency>
	```
2. 添加`job包`并编写`job任务`，实现`job接口`，并在`execute方法`中实现自己的业务逻辑  
	```
	package com.fx67ll.springboot.jobs;

	import org.quartz.*;
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	import java.text.SimpleDateFormat;
	import java.util.Date;

	public class TestQuartzJob implements Job {

		private Logger logger = LoggerFactory.getLogger(TestQuartzJob.class);

		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

			// 获取整理好的日期时间
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 查询触发器名称和触发器属于哪个分组
			TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();

			//打印日志
			logger.info("当前触发器是: " + triggerKey.getName() + "，它所属的组别是: " + triggerKey.getGroup() +
					"----------触发时间: " + simpleDateFormat.format(new Date()) +
					"-->" + "Hello fx67ll Spring Boot Quartz......");
		}
	}
	```
3. 构建调度配置类，创建JobDetail实例并定义Trigger注册到scheduler，启动scheduler开启调度  
	```
	package com.fx67ll.springboot.conf;

	import com.fx67ll.springboot.jobs.TestQuartzJob;
	import org.quartz.*;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;

	@Configuration
	public class QuartzCOnf {
		@Bean
		/**
		 * 具体的可以被执行的调度程序
		 */
		public JobDetail jobDetailTestQuartz(){
			return JobBuilder.newJob(TestQuartzJob.class).storeDurably().build();
		}

		@Bean
		/**
		 * 第一个测试触发器，主要是配置参数提示什么时候调用
		 * 应用场景有比如定时发送邮件之类的
		 */
		public Trigger triggerTestQuartzFirst(){
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					// 每五秒执行一次
					.withIntervalInSeconds(1)
					// 永久重复，一直执行下去
					.repeatForever();
			return TriggerBuilder.newTrigger()
					// 设置触发器名称和分组
					.withIdentity("triggerTestQuartzFirst","groupTestQuartz")
					.withSchedule(simpleScheduleBuilder)
					.forJob(jobDetailTestQuartz())
					.build();
		}

		@Bean
		/**
		 * 第二个测试触发器
		 */
		public Trigger triggerTestQuartzSecond(){
			return TriggerBuilder.newTrigger()
					// 设置触发器名称和分组
					.withIdentity("triggerTestQuartzSecond","groupTestQuartz")
					// 这里是通过定义表达式来表示每5秒执行一次，后续再深入研究下
					.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? *"))
					.forJob(jobDetailTestQuartz())
					.build();
		}
	}
	```



## 附录
### 操作代码目录说明
|  springboot-quickstart  |  springboot-mybatis  |  springboot-mybatis-crud  |  springboot-mybatis-crud-prod  |
|  :----:  |  :----:  |  :----:  |  :----:  |
|  快速入门  |  整合mybatis  |  整套crud操作  |  生产环境开发  |
#### 操作代码资源地址
1. [springboot-quickstart](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-quickstart)  
2. [springboot-mybatis](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis)  
3. [springboot-mybatis-crud](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis-crud)  
4. [springboot-mybatis-crud-prod](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis-crud-prod)  

### 参考资料
1. [参考教程 ———— 两天搞定SpringBoot框架](https://www.bilibili.com/video/BV16i4y197zh)  
2. [参考文档 ———— JavaSpringBoot 中 @Autowired用法](https://blog.csdn.net/weixin_41290863/article/details/111568023)  
3. [参考文档 ———— SpringBoot - @Configuration、@Bean注解的使用详解（配置类的实现）](https://www.hangge.com/blog/cache/detail_2506.html)  
4. [参考文档 ———— 【Spring Boot】Spring基础 —— 组合注解与元注解](https://blog.csdn.net/the_ZED/article/details/105456946)  
5. [参考文档 ———— @RestController 和 @Controller 的区别](https://blog.csdn.net/nimoyaoww/article/details/82999057)  
6. [参考文档 ———— MapperScan注解详解](https://blog.csdn.net/weixin_44093802/article/details/115601973)  
7. [参考文档 ———— Mapper.xml详解](https://blog.csdn.net/qq_36850813/article/details/80037363)  
8. [参考文档 ———— MVC三层架构（详解）](https://blog.csdn.net/qq_48508278/article/details/122648284)  
9. [参考文档 ———— 配置devtools热部署](https://www.cnblogs.com/charlottepl/p/14694865.html)  
10. [参考文档 ———— (十三)SpringBoot2.0热部署Devtools原理](https://blog.csdn.net/IT_hejinrong/article/details/89155308)  
11. [参考文档 ———— 2021版IDEA没有compiler.automake.allow.when.app.running](https://blog.csdn.net/qq_52978553/article/details/122376118)  
12. [参考文档 ———— SpringBoot基础之MockMvc单元测试](https://blog.csdn.net/wo541075754/article/details/88983708)  
13. [参考文档 ———— Ehcache详细解读](http://www.blogjava.net/libin2722/articles/406569.html)  
14. [参考文档 ———— spring boot接入ehcache](https://blog.csdn.net/xiongzhichao/article/details/52349121)  
15. [参考文档 ———— SpringBoot(十二)： validation常用注解](https://blog.csdn.net/mingyuli/article/details/120434810)  
16. [参考文档 ———— SpringBoot之——Validator校验相关的注解](https://blog.csdn.net/weixin_49716609/article/details/116003488)  
17. [参考文档 ———— 强悍的Spring之spring validation](https://blog.csdn.net/steven2xupt/article/details/87452664)  
18. [json格式校验并显示错误_使用 Spring Validation 优雅地进行参数校验](https://blog.csdn.net/weixin_39542850/article/details/111169911)  
 

我是 [fx67ll.com](https://fx67ll.com)，如果您发现本文有什么错误，欢迎在评论区讨论指正，感谢您的阅读！  
如果您喜欢这篇文章，欢迎访问我的 [本文github仓库地址](https://github.com/fx67ll/fx67llBigData/blob/main/note/springboot/springboot-quickstart.md)，为我点一颗Star，Thanks~ :)  
***转发请注明参考文章地址，非常感谢！！！***