# SpringBootå¿«éå¥é¨ ð¹1.0.0

#### åè¯´ä¸äºåºè¯
è½ç¶æçå·¥ä½ä¸­æ´å¤çæ¯ä¸æ°æ®åºæäº¤éï¼ä½æ¯ä½ä¸ºä¸ä¸ª`Coder`ï¼æè§å¾ææ¡ååç«¯ç`Web`ææ¯æ¥è¯´æ¯éå¸¸æå¿è¦çã  
ä¸ä»å¯ä»¥å¸®å©æä»¬å¨å·¥ä½ä¸­æ´å¥½ççè§£å¶ä»å²ä½ä¸ä½ å¯¹æ¥çäººä»çå·¥ä½çç¹ï¼ä¹è½å¨å¬å¸éè¦äººæçæ¶åæä¸ºä¸ä¸ªæåçåºæ¥å¸®æï¼æ¯å¦ä¹åå¬å¸çæ°æ®ä¸­å°æå°±åä¸æ¶æåé¨åå¼åä»»å¡ï¼
æ´éè¦çæ¯æç§ä¸éå¯ä»¥è¿ç¨ä¸äºå¿«éæ¡æ¶æ¥æ­å»ºä¸äºæææçç½ç«ï¼æ¯å¦æç[ä¸ªäººä¸»é¡µ](https://fx67ll.com)å[ä¸ªäººåå®¢](https://fx67ll.xzy)é½æ¯æèªå­¦`java`å`js`æååºæ¥çä½åã  
æä»¥ä»å¤©æå¸ææ´åä¸äºæä»¥å¾çç»éªåçè¿çæç¨ææ¡£ï¼æ¥åä¸ç¯æç« ï¼å¸®å©ä½ å¨ä¸å¤©ä¹åéè¿è¿ç¯æç« å¿«éå­¦ä¹ `SpringBoot`æ¡æ¶ä»¥ååç§å¼åå¿å¤çå·¥å·ä¸æä»¶ï¼ï¼ï¼  

## MVC
### ä»ä¹æ¯MVC
1. MVCä¸å±æ¶ææ¯æï¼è§å¾å± Viewãæå¡å± Serviceï¼ä¸æä¹å± Daoï¼å®ä»¬åå«å®æä¸åçåè½  
	+ View å±ï¼ç¨äºæ¥æ¶ç¨æ·æäº¤è¯·æ±çä»£ç å¨è¿éç¼å  
	+ Service å±ï¼ç³»ç»çä¸å¡é»è¾ä¸»è¦å¨è¿éå®æ  
	+ Dao å±ï¼ç´æ¥æä½æ°æ®åºçä»£ç å¨è¿éç¼å  
2. ä¸ºäºæ´å¥½çéä½åå±é´çè¦ååº¦ï¼å¨ä¸å±æ¶æç¨åºè®¾è®¡ä¸­ï¼éç¨é¢åæ½è±¡ç¼ç¨ï¼å³ä¸å±å¯¹ä¸å±çè°ç¨ï¼æ¯éè¿æ¥å£å®ç°çï¼èä¸å±å¯¹ä¸å±ççæ­£æå¡æä¾èï¼æ¯ä¸å±æ¥å£çå®ç°ç±»  
3. æå¡æ åï¼æ¥å£ï¼æ¯ç¸åçï¼æå¡æä¾èï¼å®ç°ç±»ï¼å¯ä»¥æ´æ¢ï¼è¿å°±å®ç°äºå±é´è§£è¦å  

### MVC æ¶æç¨åºçå·¥ä½æµç¨
1. ç¨æ·éè¿ View é¡µé¢åæå¡ç«¯æåºè¯·æ±ï¼å¯ä»¥æ¯è¡¨åè¯·æ±ãè¶é¾æ¥è¯·æ±ãAJAX è¯·æ±ç­  
2. æå¡ç«¯ Controller æ§å¶å¨æ¥æ¶å°è¯·æ±åå¯¹è¯·æ±è¿è¡è§£æï¼æ¾å°ç¸åºç Model å¯¹ç¨æ·è¯·æ±è¿è¡å¤ç  
3. Model å¤çåï¼å°å¤çç»æåäº¤ç» Controller  
4. Controller å¨æ¥å°å¤çç»æåï¼æ ¹æ®å¤çç»ææ¾å°è¦ä½ä¸ºåå®¢æ·ç«¯ååçååº View é¡µé¢ï¼é¡µé¢ç»æ¸²æï¼æ°æ®å¡«åï¼åï¼ååéç»å®¢æ·ç«¯  


## ä½¿ç¨xmlè¿æ¯æ³¨è§£
1. åºç¨çåºæ¬éç½®ä½¿ç¨xmlï¼æ¯å¦æ°æ®æºåèµæºæä»¶ç­  
2. ä¸å¡å¼åä½¿ç¨æ³¨è§£ï¼æ¯å¦serviceæ³¨å¥bean  
3. ä½æ¯xmlè¶æ¥è¶å¤å¯¼è´è¶æ¥è¶èè¿ï¼æç»åå±å°ä½¿ç¨å®å¨åºäºæ³¨è§£å¼å  


## æ³¨è§£
### å£°æBeanæ³¨è§£
> @Component ç»ä»¶æ²¡ææç¡®è§å®å¶è§è²ï¼ä½ç¨å¨ç±»çº§å«ä¸å£°æå½åç±»ä¸ºä¸ä¸ªä¸å¡ç»ä»¶ï¼è¢«`Spring IOC å®¹å¨`ç»´æ¤  
> @Service å¨ä¸å¡é»è¾å±ï¼Serviceï¼ç±»çº§å«è¿è¡å£°æ  
> @Registory å¨æ°æ®è®¿é®å±ï¼Daoï¼ç±»çº§å«è¿è¡å£°æ  
> @Controller å¨å±ç°å±ï¼MVCï¼ä½¿ç¨ï¼æ æ³¨å½åç±»ä¸ºä¸ä¸ªæ§å¶å¨  

### æ³¨å¥Beanæ³¨è§£
> @Autowired å®å¯ä»¥å¯¹ç±»æååéãæ¹æ³åæé å½æ°è¿è¡æ æ³¨ï¼å®æèªå¨è£éçå·¥ä½ï¼éè¿`@Autowired`çä½¿ç¨æ¥æ¶é¤setãgetæ¹æ³  
> @Inject ä½ç¨åä¸ï¼æ¯`JSR-330 æ å`  
> @Resource ä½ç¨åä¸ï¼æ¯`JSR-250 æ å`  
*ä»¥ä¸ä¸ç§æ³¨è§£å¨Setæ¹æ³æå±æ§ä¸å£°æï¼ä¸è¬æåµä¸æ´ä¹ æ¯å£°æå¨å±æ§ä¸ï¼ä»£ç ç®æ´æ¸æ°*

### éç½®ä¸è·åBeanæ³¨è§£
> @Configuration å°å½åç±»å£°æä¸ºä¸ä¸ªéç½®ç±»ï¼ç¸å½äºä¸ä¸ªxmléç½®æä»¶  
> @ComponentScan èªå¨æ«æåä¸æ æ³¨æ@Repository @Service @Controller  
> @Component æ³¨è§£çç±»å¹¶æ`Spring IOC å®¹å¨`è¿è¡å®ä¾ååç»´æ¤  
> @Bean ä½ç¨äºæ¹æ³ä¸ï¼å£°æå½åæ¹æ³çè¿åå¼æ¯ä¸ä¸ª`Beanå¯¹è±¡`ï¼ç¸å½äº`xmlæä»¶`ä¸­`<bean>`å£°æå½åæ¹æ³è¿åä¸ä¸ª`beanå¯¹è±¡`  
> @Value è·å`propertiesæä»¶`æå®ç`key/value`  
#### pom.xml
ä½ç¨æ¯æ·»å åæ ç¸å³éç½®ï¼ä¸»è¦æ¯åç§ä¾èµjarå  

### ç»åæ³¨è§£ååæ³¨è§£
æè°åæ³¨è§£å¶å®å°±æ¯å¯ä»¥æ³¨è§£å°å«çæ³¨è§£ä¸çæ³¨è§£ï¼è¢«æ³¨è§£çæ³¨è§£ç§°ä¹ä¸ºç»åæ³¨è§£ï¼ç»åæ³¨è§£å·å¤åæ³¨è§£çåè½ï¼ä¸»è¦çä½ç¨æ¯æ¶é¤éå¤æ³¨è§£  

### èªå®ä¹æ³¨è§£
ä¸ªæ§åçå®ä¹èªå·±æéè¦çåè½å¹¶å£°æä¸ä¸ªæ³¨è§£ï¼ç®åå·¥ç¨ï¼å¯ä»¥åèæç« ââââ[SPRINGBOOTèªå®ä¹æ³¨è§£](https://www.cnblogs.com/mizhiniurou/p/10890951.html)å­¦ä¹   

### å¸¸ç¨æ³¨è§£
**å¯ä»¥åèæç« ââââ[SpringBootå¸¸ç¨æ³¨è§£éå](https://blog.csdn.net/qq_53324833/article/details/121079368)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**  
#### @RestControllerã@ResponseBodyã@RequestBody
1. ç¸å½äº`@Controller + @ResponseBody`ä¸¤ä¸ªæ³¨è§£çç»åï¼è¿å`JSON`æ°æ®ä¸éè¦å¨æ¹æ³åé¢å `@ResponseBody`æ³¨è§£äºï¼
	ä½ä½¿ç¨@RestControllerè¿ä¸ªæ³¨è§£ï¼å°±ä¸è½è¿åjspãhtmlé¡µé¢ï¼è§å¾è§£æå¨æ æ³è§£æjspãhtmlé¡µé¢v
2. `@ResponseBody`è¡¨ç¤ºè¯¥æ¹æ³çè¿åç»æç´æ¥åå¥`HTTP response body`ä¸­ï¼ä¸è¬å¨å¼æ­¥è·åæ°æ®æ¶ä½¿ç¨ï¼ä¹å°±æ¯AJAXï¼ï¼
	å¨ä½¿ç¨`@RequestMapping`åï¼è¿åå¼éå¸¸è§£æä¸ºè·³è½¬è·¯å¾ï¼ä½æ¯å ä¸`@ResponseBody`åè¿åç»æä¸ä¼è¢«è§£æä¸ºè·³è½¬è·¯å¾ï¼èæ¯ç´æ¥åå¥`HTTP response body`ä¸­ï¼
	æ¯å¦å¼æ­¥è·å`JSON`æ°æ®ï¼å ä¸`@ResponseBody`åï¼ä¼ç´æ¥è¿å`JSON`æ°æ®  
3. `@RequestBody`å° HTTP è¯·æ±æ­£ææå¥æ¹æ³ä¸­ï¼ä½¿ç¨éåç HttpMessageConverter å°è¯·æ±ä½åå¥æä¸ªå¯¹è±¡  
#### @MapperScanã@Mapper
1. @Mapperæ³¨è§£ï¼
	+ ä½ç¨ï¼å¨æ¥å£ç±»ä¸æ·»å äº@Mapperï¼å¨ç¼è¯ä¹åä¼çæç¸åºçæ¥å£å®ç°ç±»  
	+ æ·»å ä½ç½®ï¼æ¥å£ç±»ä¸é¢  
	+ å¦ææ³è¦æ¯ä¸ªæ¥å£é½è¦åæå®ç°ç±»ï¼é£ä¹éè¦å¨æ¯ä¸ªæ¥å£ç±»ä¸å ä¸`@Mapper`æ³¨è§£ï¼æ¯è¾éº»ç¦ï¼è§£å³è¿ä¸ªé®é¢ç¨`@MapperScan`æ³¨è§£
2. @MapperScanæ³¨è§£ï¼
	+ ä½ç¨ï¼æå®è¦åæå®ç°ç±»çæ¥å£æå¨çåï¼ç¶ååä¸é¢çæææ¥å£å¨ç¼è¯ä¹åé½ä¼çæç¸åºçå®ç°ç±»  
	+ æ·»å ä½ç½®ï¼æ¯å¨Springbootå¯å¨ç±»ä¸é¢æ·»å   
	+ æ·»å `@MapperScan("com.winter.da")`æ³¨è§£ä»¥åï¼`com.winter.dao`åä¸é¢çæ¥å£ç±»ï¼å¨ç¼è¯ä¹åé½ä¼çæç¸åºçå®ç°ç±»  


## ä¹ æ¯å¤§äºéç½®ç®æ 
Spring Boot çç®æ æ¯å¿«éè¿è¡ï¼å¿«éåå»ºwebåºç¨ï¼å¹¶ç¬ç«æºåé¨ç½²ï¼jaråæ¹å¼ï¼waråæ¹å¼ï¼ï¼ç¸æ¯äºSpringæ¡æ¶æ¯å¨æ°éåçæ¡æ¶  


## æ ¸å¿éç½®
### ä¿®æ¹Bannerå¾æ 
ä¸»è¦æ¯éè¿ä¿®æ¹`/src/main/resources`ç®å½ä¸ç`banner.txt`æä»¶ï¼å¦ææ²¡æåé»è®¤ä½¿ç¨SpringBootåå§Banner  
å¯ä»¥[ä¸ªæ§åå¶ä½Bannerçç½ç«](http://patorjk.com/software/taag/#p=display&f=Graffiti&t=Type%20Something%20)å¶å®ç¸åºçtxtæä»¶  

### å¨å±éç½®
é»è®¤æ¯`application.properties`æè`application.yml`  
åæ ä¾èµé½éç½®å¨`pom.xml`ä¸­ï¼å¦ææ·»å äºä¾èµä»¥åæ çº¢å¯ä»¥ä½¿ç¨`Maven -> Reload project`å³å¯  
#### å¥å£ç±»ä¾é ç»åæ³¨è§£`@SpringBootApplication`
> @SpringBootConfiguration æ¬èº«æ¯ä¸ä¸ªéç½®ç±»ï¼å¯å¨ç±»å¯å¨çæ¶åä¼å è½½
> @EnableAutoConfiguration ç»åäº`@AutoConfigurationPackage`&`@Import(AutoConfigurationImportSelector.class)`  
> @AutoConfigurationPackage åºå±æ¯ä¸ä¸ª@Import(AutoConfigurationPackage.Registrar.class)ï¼å¶ä¼æå¯å¨ç±»çåä¸ç»åé½æ«æå°Springå®¹å¨ä¸­  
> @AutoConfigurationImportSelector è¯»åå¤§éçèªå¨éç½®ç±»ï¼å®æèªå¨éç½®ï¼å¶è¯»åçæ¯classpathä¸ç`META-INF/spring.factories`ä¸çéç½®æä»¶  
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

### Profileéç½®ââââåºåçäº§åå¼åç¯å¢
éè¿å¨`application.yml`ä¸­è®¾ç½®`spring.profiles.active=test/dev/prod`æ¥å¨æåæ¢ä¸åç¯å¢ï¼ä¾å¦ï¼
```
# å¼åç¯å¢éç½®æä»¶
application-dev.yml
server:
  prot: 8098

# æµè¯ç¯å¢éç½®æä»¶
application-test.yml
server:
  prot: 8097

# çäº§ç¯å¢éç½®æä»¶
application-prod.yml
server:
  prot: 8099

# ä¸»éç½®æä»¶
application.yml
spring:
  profiles:
    active: dev
```

### æ¥å¿éç½®
SpringBooté»è®¤ä½¿ç¨`LogBack`æ¥å¿ç³»ç»ï¼ä¸è¬ä¸»æµçæ¥å¿é½æ¯ç¨`log4j`æ¥å¿ç³»ç»  
#### å¦æéå¤å¯å¨Springé¡¹ç®ï¼å¯è½ä¼æç«¯å£å ç¨çæ¥é
1. æè·¯æ¯ææ­»å ç¨ç«¯å£çè¿ç¨å³å¯ï¼ä¸»è¦æ¯ä¸é¢ä¸¤ä¸ªå½ä»¤
2. ä½¿ç¨`netstat -aon|findstr "è¢«å ç¨çç«¯å£"`æè`tasklist |findstr "è¿ç¨åç§°"`æ¥è¯¢å°ç«¯å£çè¿ç¨å·  
3. ä½¿ç¨`taskkill /f /t /im "è¿ç¨åç§°"`æè`taskkill /f /t /pid "è¿ç¨PID"`ææ­»è¿ç¨å³å¯  


## äºå¡æ§å¶
### å£°æå¼äºå¡
**å¯ä»¥åèæç« ââââ[SpringBootå£°æå¼äºå¡çç®åè¿ç¨](https://blog.csdn.net/justry_deng/article/details/80828180)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**
ä¸»è¦åºç¨å¨æ°å¢ä¿®æ¹å é¤ä¸ï¼åºç¨æ³¨è§£å³å¯  


## å¨å±å¼å¸¸
### ä½¿ç¨@ControllerAdviceéå@ExceptionHandler
**å¯ä»¥åèæç« ââââ[Springbootç³»å-@ControllerAdviceä½¿ç¨](https://blog.csdn.net/wangxinyao1997/article/details/103710843)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**
æ­¤æ³¨è§£å¶å®æ¯ä¸ä¸ªå¢å¼ºç`Controller`ï¼ä½¿ç¨è¿ä¸ª`Controller`ï¼å¯å®ç°ä¸ä¸ªæ¹é¢çåè½ï¼å ä¸ºè¿æ¯SpringMVCæä¾çåè½ï¼æä»¥å¯ä»¥å¨springbootä¸­ç´æ¥ä½¿ç¨
1. å¨å±å¼å¸¸å¤ç ï¼@ExceptionHandlerï¼
2. å¨å±æ°æ®ç»å® ï¼@InitBinderï¼
3. å¨å±æ°æ®é¢å¤ç ï¼@ModelAttributeï¼
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
        resultInfo.setMsg("å¨å±å¼å¸¸æ¦æªï¼æä½å¤±è´¥ï¼");
//        if (exception instanceof ParamsException) {
//            ParamsException paramsException = (ParamsException) exception;
//            resultInfo.setMsg(paramsException.getMsg());
//            resultInfo.setCode(paramsException.getCode());
//        }
        return resultInfo;
    }
}
```


## æ°æ®æ ¡éª
### ä¸ºä»ä¹è¦è¿è¡åç«¯æ°æ®æ ¡éª
æ°æ®çæ ¡éªæ¯äº¤äºå¼ç½ç«ä¸ä¸ªä¸å¯æç¼ºçåè½ï¼åç«¯ç`jsæ ¡éª`å¯ä»¥æ¶µçå¤§é¨åçæ ¡éªèè´£ï¼å¦ç¨æ·åå¯ä¸æ§ï¼çæ¥æ ¼å¼ï¼é®ç®±æ ¼å¼æ ¡éªç­ç­å¸¸ç¨çæ ¡éªã
ä½æ¯ä¸è¬åç«¯ä¼ æ¥çæ°æ®æ¯ä¸å¯ä¿¡çï¼åç«¯æ ¡éªè¿äºï¼åç«¯ä¹åºè¯¥éæ°æ ¡éªï¼å ä¸ºä¸æé¤ç¨æ·ç»è¿æµè§å¨ç´æ¥éè¿`Httpå·¥å·`ååç«¯è¯·æ±çæåµã
æä»¥æå¡ç«¯çæ°æ®æ ¡éªä¹æ¯å¿è¦çï¼å¯ä»¥é²æ­¢èæ°æ®è½å°æ°æ®åºä¸­ï¼å¦ææ°æ®åºä¸­åºç°ä¸ä¸ªéæ³çé®ç®±æ ¼å¼ï¼ä¹ä¼è®©è¿ç»´äººåå¤´ç¼ä¸å·²ã

### å¦ä½è¿è¡åç«¯æ°æ®æ ¡éª
1. `SpringBoot`ä¸­ä¸è¬ä½¿ç¨`Spring Validation`æ¥è¿è¡åç«¯æ°æ®æ ¡éªï¼å®æ¯å¯¹`Hibernate Validation`è¿è¡äºäºæ¬¡å°è£ï¼
	å¨`SpringMVC`æ¨¡åä¸­æ·»å äºèªå¨æ ¡éªï¼å¹¶å°æ ¡éªä¿¡æ¯å°è£è¿äºç¹å®çç±»ä¸­  
2. å¨ä½¿ç¨æ¶æä»¬åªéè¦å¼å¥`spring-boot-starter-web`ä¾èµå³å¯ï¼è¯¥æ¨¡åä¼èªå¨ä¾èµ`spring-boot-starter-validation`  

### Spring Validation å¸¸ç¨æ³¨è§£
> @Nullï¼è¢«æ³¨éçåç´ å¿é¡»ä¸º`null`  
> @NotNullï¼è¢«æ³¨éçåç´ ä¸è½ä¸º`null`ï¼å¯ä»¥ä¸ºç©ºå­ç¬¦ä¸²  
> @AssertTrueï¼è¢«æ³¨éçåç´ å¿é¡»ä¸º`true`  
> @AssertFalseï¼è¢«æ³¨éçåç´ å¿é¡»ä¸º`false`  
> @Min(value)ï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªæ°å­ï¼å¶å¼å¿é¡»å¤§äºç­äºæå®çæå°å¼  
> @Max(value)ï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªæ°å­ï¼å¶å¼å¿é¡»å°äºç­äºæå®çæå¤§å¼  
> @DecimalMin(value)ï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªæ°å­ï¼å¶å¼å¿é¡»å¤§äºç­äºæå®çæå°å¼  
> @DecimalMax(value)ï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªæ°å­ï¼å¶å¼å¿é¡»å°äºç­äºæå®çæå¤§å¼  
> @Size(max,min)ï¼è¢«æ³¨éçåç´ çå¤§å°å¿é¡»å¨æå®çèå´å  
> @Digits(integer,fraction)ï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªæ°å­ï¼å¶å¼å¿é¡»å¨å¯æ¥åçèå´å  
> @Pastï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªè¿å»çæ¥æ  
> @Futureï¼è¢«æ³¨éçåç´ å¿é¡»æ¯ä¸ä¸ªå°æ¥çæ¥æ  
> @Pattern(value)ï¼è¢«æ³¨éçåç´ å¿é¡»ç¬¦åæå®çæ­£åè¡¨è¾¾å¼  
> @Emailï¼è¢«æ³¨éçåç´ å¿é¡»æ¯çµå­é®ä»¶å°å  
> @Lengthï¼è¢«æ³¨éçå­ç¬¦ä¸²çå¤§å°å¿é¡»å¨æå®çèå´å  
> @Rangeï¼è¢«æ³¨éçåç´ å¿é¡»å¨åéçèå´å  
> @URLï¼è¢«æ³¨è§£çåç´ å¿é¡»æ¯ä¸ä¸ª`URL`  
> @NotEmptyï¼ç¨å¨éåç±»ä¸ï¼ä¸è½ä¸º`null`ï¼å¹¶ä¸é¿åº¦å¿é¡»å¤§äº0  
> @NotBlankï¼åªè½ä½ç¨å¨`String`ä¸ï¼ä¸è½ä¸º`null`ï¼èä¸è°ç¨`trim()`åï¼é¿åº¦å¿é¡»å¤§äº0  

### èªå®ä¹æ³¨è§£
**å¯ä»¥åèæç« ââââ[Springèªå®ä¹æ³¨è§£(validation)](https://blog.csdn.net/ileopard/article/details/123485111)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**  

### ç¤ºä¾ä»£ç 
1. `/com/fx67ll/springboot/controller/UserController.java`å¨ä¼ åçä½ç½®æ·»å `@Vaild`æ³¨è§£è¡¨ç¤ºè¿éçåæ°éè¦æ ¡éªï¼éè¦æ³¨æJSONæ ¼å¼åè¡¨åæ ¼å¼ä¼ è¿æ¥çåæ°å¼å¸¸ä¼æäºåºå«ï¼éè¦å¨åé¢æ³¨æ
	```
    // æ·»å ç¨æ·
	@PutMapping("/adduser")
    public ResultInfo saveUser(@RequestBody @Valid User user) {
        ResultInfo resultInfo = new ResultInfo();
        userService.saveUser(user);
        return resultInfo;
    }
	```
2. å¨`Bean`æä»¶`/com/fx67ll/springboot/dao/User.java`ä¸­ç§æå­æ®µä¸ä½¿ç¨æ³¨è§£æ¥æ ¡éªï¼ä¸è´´ææä»£ç äºï¼ä»è´´é¨åéç¹ä»£ç 
	```
    @NotBlank(message = "ç¨æ·åç§°ä¸è½ä¸ºç©ºï¼")
    private String userName;
	
    @NotBlank(message = "ç¨æ·å¯ç ä¸è½ä¸ºç©ºï¼")
    @Length(min = 6, max = 20, message = "å¯ç é¿åº¦æå°å­ä½ä¸æå¤äºåä½ï¼")
    private String userPwd;
	```
3. å¨å¨å±èªå®ä¹å¼å¸¸æ¦æªä¸­`/com/fx67ll/springboot/exceptions/TestGlobalExceptionHandler.java`åç¨æ·è¿åéè¯¯ä»£ç åä¿¡æ¯
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
			resultInfo.setMsg("å¨å±å¼å¸¸æ¦æªï¼æä½å¤±è´¥ï¼");
			// å¨å±æ°æ®æ ¡éªï¼æ³¨æï¼ï¼ï¼ä½¿ç¨ json è¯·æ±ä½è°ç¨æ¥å£ï¼æ ¡éªå¼å¸¸æåº MethodArgumentNotValidException
			if (exception instanceof MethodArgumentNotValidException) {
				MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
				resultInfo.setCode(1023);
				resultInfo.setMsg(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
			}
			return resultInfo;
		}
	}
	```


## éæèµæº
é»è®¤éç½®ä¸ï¼æä»¬å¯ä»¥å¨`resources`èµæºç®å½ä¸å­æ¾webåºç¨éæèµæºæä»¶  
èªå®ä¹éæèµæºè·¯å¾ï¼å¯ä»¥éè¿å¨`spring.resources.static-locations`åé¢è¿½å ä¸ä¸ªéç½®`classpath:/ä½ èªå®ä¹çéç½®ç®å½/`ï¼ä¾å¦ï¼
```
# application.yml
spring:
  resources:
	# å¤ä¸ªç®å½ä½¿ç¨éå·éå¼
    static-loaction: classpath:/public/,classpath:/static/,classpath:/fx67ll/
```


## æååé¨ç½²
### jarå
1. ä¸è¬ç¨äºç¼åä¾èµå·¥å·å
2. æå
	+ å¨IDEA`Run/Debug Configurations`ä¸`Command line`éç½®`clean complie package -Dmaven.test.skip=true`æ§è¡æåå½ä»¤  
	+ `target`ç®å½å¾å°å¾é¨ç½²çé¡¹ç®æä»¶  
2. é¨ç½²
	+ å¨dosçªå£ä¸­ï¼æ§è¡å½ä»¤`java -jar jaråæå¨çæ¬å°ç®å½`  

### warå
1. å¨çäº§ç¯å¢ä¸­æä¸ºå¸¸è§çé¨ç½²æ¹å¼  
2. ä¿®æ¹`pom.xml`ï¼è®¾ç½®æåæ¨¡å¼ä¸ºwarå
	```
	<groupId>com.fx67ll</groupId>
    <artifactId>springboot-quickstart</artifactId>
    <version>0.1.0</version>
	<!--è®¾ç½®ä¸ºwaråæ¨¡å¼-->
    <packaging>war</packaging>
	```
3. å¿½ç¥ååµTomcat
	```
	<!--è®¾ç½®ä¸ºå¤é¨å·²æä¾ï¼è¡¨ç¤ºå¿½ç¥-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	```
4. éç½®çæçwarååç§°
	```
	<build>
	<!--è®¾ç½®warååç§°-->
        <finalName>fx67ll-springboot-quickstart-test</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
	```
5. ä¿®æ¹`Starter`ç±»ï¼æ·»å å®¹å¨å¯å¨å è½½æä»¶ï¼ç±»ä¼¼è¯»åweb.xmlæä»¶ï¼  
	+ è¿ééè¿ç»§æ¿`SpringBootServletInitiallizer`ç±»å¹¶éå`configure`æ¹æ³æ¥å®ç°  
	+ å¨é¨ç½²é¡¹ç®çæ¶åæå®å¤é¨Tomcatè¯»åé¡¹ç®å¥å£æ¹æ³
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
6. æå
	+ å¨IDEA`Run/Debug Configurations`ä¸`Command line`éç½®`clean complie package -Dmaven.test.skip=true`æ§è¡æåå½ä»¤  
	+ `target`ç®å½å¾å°å¾é¨ç½²çé¡¹ç®æä»¶  
7. é¨ç½²å¹¶è®¿é®
	+ æ¾ç½®å°å¤é¨tomcatä¸­ï¼æ§è¡binç®å½ä¸startèæ¬å³å¯  

### ç­é¨ç½²
ç­é¨ç½²ï¼å°±æ¯å¨åºç¨æ­£å¨è¿è¡çæ¶ååçº§è½¯ä»¶ï¼å´ä¸éè¦éæ°å¯å¨åºç¨ï¼ä¸»è¦åºç¨å¨å¼åè¿ç¨ä¸­  
#### ç­é¨ç½²åç
1. `spring-boot-devtools`æ¯ä¸ä¸ªä¸ºå¼åèæå¡çä¸ä¸ªæ¨¡åï¼å¶ä¸­æéè¦çåè½å°±æ¯èªå¨åºç¨ä»£ç æ´æ¹å°ææ°çAppä¸é¢å»ï¼
	åçæ¯å¨åç°ä»£ç ææ´æ¹ä¹åï¼éæ°å¯å¨åºç¨ï¼ä½æ¯éåº¦æ¯æå¨åæ­¢ååå¯å¨è¿è¦æ´å¿«ï¼æ´å¿«æçä¸æ¯èçåºæ¥çæå·¥æä½çæ¶é´  
2. å¶æ·±å±åçæ¯ä½¿ç¨äºä¸¤ä¸ª`ClassLoader`ï¼ä¸ä¸ª`Classloader`å è½½é£äºä¸ä¼æ¹åçç±»ï¼ç¬¬ä¸æ¹Jaråï¼ï¼å¦ä¸ä¸ª`ClassLoader`å è½½ä¼æ´æ¹çç±»ï¼ç§°ä¸º`restart ClassLoader`ï¼
	è¿æ ·å¨æä»£ç æ´æ¹çæ¶åï¼åæ¥ç`restart ClassLoader`è¢«ä¸¢å¼ï¼éæ°åå»ºä¸ä¸ª`restart ClassLoader`ï¼ç±äºéè¦å è½½çç±»ç¸æ¯è¾å°ï¼æä»¥å®ç°äºè¾å¿«çéå¯æ¶é´ï¼*å¤§æ¦å¨5ç§ä»¥å*  
#### devtoolsåç
1. devtoolsä¼çå¬classpathä¸çæä»¶åå¨ï¼å¹¶ä¸ä¼ç«å³éå¯åºç¨ï¼åçå¨ä¿å­æ¶æºï¼*æ³¨æï¼å ä¸ºå¶éç¨çèææºæºå¶ï¼è¯¥é¡¹éå¯æ¯å¾å¿«ç*  
2. devtoolså¯ä»¥å®ç°é¡µé¢ç­é¨ç½²ï¼å³é¡µé¢ä¿®æ¹åä¼ç«å³çæï¼è¿ä¸ªå¯ä»¥ç´æ¥å¨`application`æä»¶ä¸­éç½®`spring.thymeleaf.cache=false`æ¥å®ç° *æ³¨æï¼ä¸åçæ¨¡æ¿éç½®ä¸ä¸æ ·*
#### ç­é¨ç½²ä¸»è¦æ­¥éª¤
1. å¨`pom.xml`ä¸­æ·»å ä¾èµï¼åæ¶æ·»å `devtools`çææ å¿æä»¶  
	```
	<!--ç­é¨ç½²æä»¶devtools-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<!--è¡¨ç¤ºå½åè¿ä¸ªé¡¹ç®è¢«ç»§æ¿ä¹åï¼è¿ä¸ªä¸åä¸ä¼ é-->
		<optional>true</optional>
	</dependency>
	
	<plugin>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-maven-plugin</artifactId>
		<!--å¨åæçåºç¡ä¸æ·»å -->
		<configuration>
			<!--å¦ææ²¡æè¯¥éç½®ï¼ç­é¨ç½²æä»¶devtoolsä¸çæ-->
			<fork>true</fork>
		</configuration>
	</plugin>
	```
2. ä¿®æ¹`application.yml`å¨å±éç½®æä»¶ï¼å¨`application.yml`ä¸­éç½®`spring.devtools.restart.enable=false`ï¼æ­¤æ¶`restart`ç±»å è½½å¨è¿ä¼åå§åï¼ä½ä¸ä¼çè§æä»¶æ´æ°
	```
	spring:
	  # ç­é¨ç½²éç½®
	  devtools:
		restart:
		  enabled: true
		  # è®¾ç½®éå¯çç®å½ï¼æ·»å ç®å½çæä»¶éè¦restart
		  additional-paths: src/main/java
		  # è§£å³é¡¹ç®å¯å¨éæ°ç¼è¯åæ¥å£æ¥404çé®é¢
		  poll-interval: 3000
		  quiet-period: 1000
	```
3. ä¿®æ¹ IDEA éç½®
	+ ä¿®æ¹äºjavaç±»ä¹åï¼IDEA é»è®¤æ¯ä¸èªå¨ç¼è¯çï¼è`spring-boot-devtools`åæ¯çæµ`classpath`ä¸çæä»¶åçååæä¼éå¯åºç¨ï¼æä»¥éè¦è®¾ç½® IDEA çèªå¨ç¼è¯  
	+ è®¾ç½®èªå¨éç½® `File -> Settings -> Build -> Complier -> Build Project automatically`  
	+ ~~ä¿®æ¹`Register`å±æ§ï¼æ§è¡å¿«æ·é®`ctrl + shift + alt + /`ï¼éæ©`Register`ï¼å¾ä¸`Complier autoMake allow when app running`~~  
	+ *æ³¨æ IDEA 2021.2.3 çæ¬ä¸­æ²¡æä¸é¢çéé¡¹*ï¼è¿ç§»å°äº`File -> Settings -> Tools -> Advanced Settings -> Complier -> Allow auto-make to start......`
4. éç½®å®éè¦éå¯ä¸ä¸ï¼ç¶åæä¿®æ¹çè¯é¡¹ç®ä¼èªå¨æ´æ°ï¼ä½æ¯å¦ææ¯èªå¨è§¦åçè¯ï¼ä¼é æé¢ç¹æ´æ°ï¼å¯¹ç¡¬ä»¶æä¸å®çè´æï¼æä»¥å¯ä»¥æ¹ææå¨è§¦åæ¨¡å¼
	+ ç¹å»å³ä¸è§ `Run/Debug Configurations`  
	+ éæ©ä¸æ `Configuration -> Spring Boot -> Running Application Update Policies -> On 'Update' action`  
	+ éæ© `Update classes and resources`  
	+ å¦æææ´æ°å¯ä»¥ï¼ä½¿ç¨å¿«æ·é® `Ctrl + F10` éæ°ç¼è¯  
5. å¿«æ·é®`Ctrl + F9`ï¼ä½¿ç¨ç­é¨ç½²éæ°å¯å¨  


## ååæµè¯
### ä¾èµ
```
<!--ååæµè¯-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>
```

### Serviceä¸å¡å±ââââä¸å¡é»è¾æ¹æ³æµè¯
*éè¦æ³¨æçæ¯ï¼*
1. å¦æå¨å`mainæä»¶å¤¹`å¹³çº§ç`testæä»¶å¤¹`ä¸æ°å»ºäº`javaæä»¶å¤¹`ï¼ä½æ¯æ æ³æ°å»º`java class`æä»¶
2. é£ä¹å°±éè¦å³é®æä»¶å¤¹ `Mark Directory as -> Test Sources Root`ä¹åï¼æä»¶å¤¹åç»¿å³å¯  
```
# ç¤ºä¾ä»£ç 
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
 * Serviceä¸å¡æ¹æ³æµè¯
 *
 * Junitä¸­çRunWithæ³¨è§£ è¡¨ç¤ºè¯¥ç±»æ¯ååæµè¯çæ§è¡ç±»
 * SpringRunner æ¯ spring-test æä¾çæµè¯æ§è¡ååç±»ï¼æ¯Springååæµè¯ä¸­SpringJUnit4ClassRunnerçæ°åå­ï¼
 * SpringBootTestæ³¨è§£ æ¯æ§è¡æµè¯ç¨åºçå¼å¯¼ç±»
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Starter.class})
public class TestUserService {

    // æ¥å¿çä½¿ç¨
    private Logger logger = LoggerFactory.getLogger(TestUserService.class);

    @Resource
    private UserService userService;

    @Before
    public void before() {
        logger.info("ååæµè¯å¼å§......");
    }

    @Test
    public void testQueryUserById() {
        logger.info("æµè¯æ ¹æ®ç¨æ·idæ¥è¯¢......");

        User user = userService.queryUserById(1);
        logger.info("ç¨æ·è®°å½: {}", user.toString());
    }

    @Test
    public void testSelectUserListByParams() {
        logger.info("æµè¯æ ¹æ®åé¡µæ¡ä»¶æ¥è¯¢ç¨æ·åè¡¨......");

        UserQuery userQuery = new UserQuery();
        PageInfo<User> pageInfo = userService.selectUserListByParams(userQuery);
        logger.info(pageInfo.toString());
    }

    @After
    public void after() {
        logger.info("ååæµè¯ç»æ......");
    }
}
```

### controlleræ§å¶å±ââââæ¥å£æ¹æ³æµè¯
#### ä½¿ç¨MockMVCè¿è¡æµè¯
`MockMvc`æ¯ç±`spring-test`åæä¾ï¼å®ç°äºå¯¹`Httpè¯·æ±`çæ¨¡æï¼è½å¤ç´æ¥ä½¿ç¨ç½ç»çå½¢å¼ï¼è½¬æ¢å°`Controller`çè°ç¨ï¼ä½¿å¾æµè¯éåº¦å¿«ãä¸ä¾èµç½ç»ç¯å¢ã
åæ¶æä¾äºä¸å¥éªè¯çå·¥å·ï¼ç»æçéªè¯ååæ¹ä¾¿  
#### ä»ä¹æ¯Mock
å¨é¢åå¯¹è±¡çç¨åºè®¾è®¡ä¸­ï¼æ¨¡æå¯¹è±¡`mock object`æ¯ä»¥å¯æ§çæ¹å¼æ¨¡æçå®å¯¹è±¡è¡ä¸ºçåå¯¹è±¡ã
å¨ç¼ç¨è¿ç¨ä¸­ï¼éå¸¸éè¿æ¨¡æä¸äºè¾å¥æ°æ®ï¼æ¥éªè¯ç¨åºæ¯å¦è¾¾å°é¢æç»æ  
#### æ¥å£MockMvcBuilder
æä¾ä¸ä¸ªå¯ä¸ç`buildæ¹æ³`ï¼ç¨æ¥æé `MockMvc`ã
ä¸»è¦æä¸¤ä¸ªå®ç°ï¼`StandaloneMockMvcBuilder`å`DefaultMockMvcBuilder`ï¼åå«å¯¹åºä¸¤ç§æµè¯æ¹å¼ï¼
å³ç¬ç«å®è£åéæWebç¯å¢æµè¯ï¼å¹¶ä¸ä¼éæçæ­£ç`webç¯å¢`ï¼èæ¯éè¿ç¸åºç`Mock API`è¿è¡æ¨¡ææµè¯ï¼æ é¡»å¯å¨æå¡å¨ï¼ã
MockMvcBuildersæä¾äºå¯¹åºçåå»ºæ¹æ³`standaloneSetup`æ¹æ³å`webAppContextSetup`æ¹æ³ï¼å¨ä½¿ç¨æ¶ç´æ¥è°ç¨å³å¯ã
```
# ç¤ºä¾ä»£ç 
# PSï¼è½ç¶æç¤ºæµè¯éè¿ï¼ä½æ¯æ§å¶å°ä¸ç´æ²¡ææå°åºè¿åä¿¡æ¯çè®°å½ï¼åææç©ºçç
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

    // æ¥å¿çä½¿ç¨
    private Logger logger = LoggerFactory.getLogger(TestUserController.class);

    @Autowired
    private MockMvc mockMvc;

    /**
     * æ¨¡ææµè¯ç¨æ·åè¡¨æ¥è¯¢
     * å¶å®å°±å¨æ¨¡æçå®ç¯å¢ä¸åç«¯å¯¹åç«¯åèµ·çè¯·æ±
     */
    @Test
    public void apiTestSelectUserListByParams() throws Exception {

        logger.info("å¼å§æ¨¡æåéæ¥è¯¢ç¨æ·åè¡¨çè¯·æ±......");

        // æå»ºè¯·æ±
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/list")
                .contentType("text/html") // è®¾ç½®è¯·æ±å¤´ä¿¡æ¯
                .accept(MediaType.APPLICATION_JSON); // è®¾ç½®è¯·æ±Acceptå¤´ä¿¡æ¯

        // åéè¯·æ±
        ResultActions perform = mockMvc.perform(requestBuilder);

        // æ ¡éªè¯·æ±ç»æ
        perform.andExpect(MockMvcResultMatchers.status().isOk());

        // è·åæ§è¡å®æåè¿åçç»æ
        MvcResult mvcResult = perform.andReturn();

        // å¾å°æ§è¡åçååº
        MockHttpServletResponse response = mvcResult.getResponse();

        // æå°ç»æ
        logger.info(String.valueOf(response.getContentLength()));
        logger.info("ååºç¶æ: ", response.getStatus());
        logger.info("ååºä¿¡æ¯: ", response.getContentAsString());

        logger.info("ç»ææ¨¡æåéæ¥è¯¢ç¨æ·åè¡¨çè¯·æ±......");
    }

    @Test
    public void apiTestQueryUserByUsername() throws Exception {

        logger.info("å¼å§æ¨¡ææ ¹æ®ç¨æ·åæ¥è¯¢ç¨æ·è®°å½çè¯·æ±......");

        // æå»ºè¯·æ±å¹¶åé
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/name/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // æå°ç»æ
        logger.info("ååºç¶æ: ", mvcResult.getResponse().getStatus());
        logger.info("ååºä¿¡æ¯: ", mvcResult.getResponse().getContentAsString());

        logger.info("ç»ææ¨¡ææ ¹æ®ç¨æ·åæ¥è¯¢ç¨æ·è®°å½çè¯·æ±......");
    }
}
```


## Swagger2ææ¡£å·¥å·
### ä¾èµ
å¨`pom.xml`ä¸­æ·»å ä»¥ä¸ä»£ç 
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

### å¸¸ç¨æ³¨è§£
**å¯ä»¥åèæç« ââââ[swagger2 æ³¨è§£è¯´æ](https://blog.csdn.net/xiaojin21cen/article/details/78654652)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**  
#### @Api
*ä¸»è¦æ¯ç¨å¨è¯·æ±ç±»ä¸ï¼ç¨äºè¯´æè¯¥ç±»çä½ç¨*
```
# ç¤ºä¾
@Api(tags = "xxæ¨¡å")
```
#### @ApiOperation
*ä¸»è¦æ¯ç¨å¨è¯·æ±çæ¹æ³ä¸ï¼è¯´ææ¹æ³çä½ç¨*
```
# ç¤ºä¾
@ApiOperation(value = "xxæ¹æ³çä½ç¨", notes = "xxæ¹æ³çå¤æ³¨è¯´æ")
```
#### @ApiImplicitParamsã@ApiImplicitParam
*ä¸»è¦æ¯ç¨å¨è¯·æ±çæ¹æ³ä¸ï¼è¯´ææ¹æ³çåæ°*
```
# è¯¦ç»åæ°è¯´æ
@ApiImplicitParamsï¼ç¨å¨è¯·æ±çæ¹æ³ä¸ï¼åå«ä¸ç»åæ°è¯´æ
	@ApiImplicitParamï¼å¯¹åä¸ªåæ°çè¯´æ	    
	    nameï¼åæ°å
	    valueï¼åæ°çè¯´æãæè¿°
	    requiredï¼åæ°æ¯å¦å¿é¡»å¿å¡«
	    paramTypeï¼åæ°æ¾å¨åªä¸ªå°æ¹
	        Â· query --> è¯·æ±åæ°çè·åï¼@RequestParam
	        Â· header --> è¯·æ±åæ°çè·åï¼@RequestHeader	      
	        Â· pathï¼ç¨äºrestfulæ¥å£ï¼--> è¯·æ±åæ°çè·åï¼@PathVariable
	        Â· bodyï¼è¯·æ±ä½ï¼-->  @RequestBody User user
	        Â· formï¼æ®éè¡¨åæäº¤ï¼	   
	    dataTypeï¼åæ°ç±»åï¼é»è®¤Stringï¼å¶å®å¼dataType="Integer"	   
	    defaultValueï¼åæ°çé»è®¤å¼
	
# åä¸ªåæ°ç¤ºä¾	
@ApiImplicitParam(name = "xxx", value = "xxx", required = true, paramType = "path", dataType = "String", defaultValue = "")

# å¤ä¸ªåæ°ç¤ºä¾
@ApiImplicitParams({
	@ApiImplicitParam(name = "xxxa", value = "xxxa", required = true, paramType = "body", dataType = "String", defaultValue = ""),
	@ApiImplicitParam(name = "xxxb", value = "xxxb", required = true, paramType = "body", dataType = "String", defaultValue = ""),
})
```
#### @ApiResponsesã@ApiResponse
*ä¸»è¦æ¯ç¨å¨è¯·æ±çæ¹æ³ä¸ï¼è¯´æéè¯¯ååºçä¿¡æ¯*
```
# è¯¦ç»åæ°è¯´æ
@ApiResponsesï¼ååºç¶æçè¯´æãæ¯ä¸ªæ°ç»ï¼å¯åå«å¤ä¸ª @ApiResponse
	@ApiResponseï¼æ¯ä¸ªåæ°çè¯´æ
	    codeï¼æ°å­ï¼ä¾å¦400
	    messageï¼ä¿¡æ¯ï¼ä¾å¦"è¯·æ±åæ°æ²¡å¡«å¥½"
	    responseï¼æåºå¼å¸¸çç±»
	
# å¤ä¸ªåæ°ç¤ºä¾ï¼ä¸è¬ååºé½æ¯å¤ä¸ªcodeï¼æä»¥ä¸ååä¸ªåæ°çç¤ºä¾äº
@ApiResponses({
		@ApiResponse(code = 200, message = "è¯·æ±æå"),
		@ApiResponse(code = 578, message = "è¯·æ±åæ°éè¯¯"),
		@ApiResponse(code = 404, message = "è¯·æ±è·¯å¾æ²¡ææé¡µé¢è·³è½¬è·¯å¾ä¸å¯¹")
})

```
#### @ApiModelã@ApiModelProperty 
1. @ApiModel ç»å¸¸ç¨äºè¯·æ±çå¥åå¯¹è±¡åååºè¿åå¼å¯¹è±¡çæè¿°  
	+ å¥åæ¯å¯¹è±¡ï¼å³ @RequestBody æ¶ï¼ ç¨äºå°è£è¯·æ±ï¼åæ¬æ°æ®çåç§æ ¡éªï¼æ°æ®  
	+ è¿åå¼æ¯å¯¹è±¡ï¼å³ @ResponseBody æ¶ï¼ç¨äºè¿åå¼å¯¹è±¡çæè¿°  
2. @ApiModelProperty ç¨äºæ¯ä¸ªå±æ§ä¸é¢ï¼è¯´æå±æ§çå«ä¹
```
# ç¤ºä¾
@ApiModel(description = "ç¨æ·å®ä½ç±»")
public class User {
    @ApiModelProperty(value = "ç¨æ·å", required = true, example = "0")
    private Integer id;
    
	@ApiModelProperty(value = "ç¨æ·ID", required = true, example = "fx67ll")
    private String userName;
    
	@ApiModelProperty(value = "ç¨æ·å¯ç ", required = true, example = "xxxxxxxx")
    private String userPwd;
}
```


## åå¸å¼ç¼å­å·¥å·Ehcache
### ä»ä¹æ¯Ehcache
`EhCache`æ¯ä¸ä¸ª`çº¯Java`çè¿ç¨åç¼å­æ¡æ¶ï¼å·æå¿«éãç²¾å¹²ç­ç¹ç¹ï¼æ¯`Hibernate`ä¸­é»è®¤`CacheProvider`ã  
`Ehcache`æ¯ä¸ç§å¹¿æ³ä½¿ç¨çå¼æº`Javaåå¸å¼ç¼å­`ï¼ä¸»è¦é¢åéç¨ç¼å­ï¼`Java EE`å`è½»éçº§å®¹å¨`ã  
å®å·æåå­åç£çå­å¨ï¼ç¼å­å è½½å¨ï¼ç¼å­æ©å±ï¼ç¼å­å¼å¸¸å¤çç¨åºï¼ä¸ä¸ª`gzip`ç¼å­`servlet`è¿æ»¤å¨ï¼æ¯æ`REST API`å`SOAP API`ç­ç¹ç¹ã  

### SpringCacheç¸å³æ³¨è§£
SpringBootç¼å­å®ç°åé¨ä½¿ç¨SpringCacheå®ç°ç¼å­æ§å¶ï¼è¿ééæEhcacheå®éä¸æ¯å¯¹SpringCacheæ½è±¡çä¸ç§å®ç°
**å¯ä»¥åèæç« ââââ[Spring Cache ç®ä»](https://www.jianshu.com/p/9d3c58ecf8ff)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**  
#### @EnableCaching
å¼å¯ç¼å­åè½ï¼ä¸è¬æ¾å¨å¯å¨ç±»ä¸  
#### @CacheConfig
å½æä»¬éè¦ç¼å­çå°æ¹è¶æ¥è¶å¤ï¼ä½ å¯ä»¥ä½¿ç¨`@CacheConfig(cacheNames = {"cacheName"})`æ³¨è§£å¨`Class`ä¹ä¸æ¥ç»ä¸æå®`value`çå¼ï¼
è¿æ¶å¯çç¥`value`ï¼å¦æä½ å¨ä½ çæ¹æ³ä¾æ§åä¸äº`value`ï¼é£ä¹ä¾ç¶ä»¥æ¹æ³ç`value`å¼ä¸ºå  
#### @Cacheable
æ ¹æ®æ¹æ³å¯¹å¶è¿åç»æè¿è¡ç¼å­ï¼ä¸æ¬¡è¯·æ±æ¶ï¼å¦æç¼å­å­å¨ï¼åç´æ¥è¯»åç¼å­æ°æ®è¿åï¼å¦æç¼å­ä¸å­å¨ï¼åæ§è¡æ¹æ³ï¼å¹¶æè¿åçç»æå­å¥ç¼å­ä¸­ï¼ä¸è¬ç¨å¨æ¥è¯¢æ¹æ³ä¸  
*æ³¨æ`value`åé¢è¦ä½¿ç¨`ehcache.xml`æä»¶ä¸­æåç`cache.name`*
```
# åä¸ªåæ°ç¤ºä¾ä»£ç 
@Cacheable(value = "fx67llCache", key = "#xxx")

# å¤ä¸ªåæ°ç¤ºä¾ï¼éç¨æ¼æ¥çæ¹å¼
@Cacheable(value = "fx67llCache", key = "#xxx.xxx + '-' + #xxx.xxx + '-' + #xxx.xxx")
```
#### @CachePut
ä½¿ç¨è¯¥æ³¨è§£æ å¿çæ¹æ³ï¼æ¯æ¬¡é½ä¼æ§è¡ï¼å¹¶å°ç»æå­å¥æå®çç¼å­ä¸­ãå¶ä»æ¹æ³å¯ä»¥ç´æ¥ä»ååºçç¼å­ä¸­è¯»åç¼å­æ°æ®ï¼èä¸éè¦åå»æ¥è¯¢æ°æ®åºï¼ä¸è¬ç¨å¨æ°å¢æ¹æ³ä¸  
```
# ç¤ºä¾ä»£ç 
@CachePut(value = "fx67llCache", key = "#xxx.xxx")
```
#### @CacheEvict
ä½¿ç¨è¯¥æ³¨è§£æ å¿çæ¹æ³ï¼ä¼æ¸ç©ºæå®çç¼å­ï¼ä¸è¬ç¨å¨æ´æ°æèå é¤æ¹æ³ä¸  
```
# ç¤ºä¾ä»£ç 
@CacheEvict(value = "fx67llCache", key = "#xxx")
```
#### @Caching
è¯¥æ³¨è§£å¯ä»¥å®ç°åä¸ä¸ªæ¹æ³ä¸åæ¶ä½¿ç¨å¤ç§æ³¨è§£  

### Ehcacheçä½¿ç¨
1. å¨`pom.xml`æ·»å ä¾èµ
	```
	<!--Ehcacheå·¥å·ä¾èµ-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-cache</artifactId>
	</dependency>
	<dependency>
		<groupId>net.sf.ehcache</groupId>
		<artifactId>ehcache</artifactId>
	</dependency>
	```
2. æ·»å `ehcache.xml`æä»¶
	```
	<?xml version="1.0" encoding="UTF-8"?>
	<ehcache name="fx67llCache">
		<!--
		   diskStoreï¼ä¸ºç¼å­è·¯å¾ï¼ehcacheåä¸ºåå­åç£çä¸¤çº§ï¼æ­¤å±æ§å®ä¹ç£ççç¼å­ä½ç½®ãåæ°è§£éå¦ä¸ï¼
		   user.home â ç¨æ·ä¸»ç®å½
		   user.dir  â ç¨æ·å½åå·¥ä½ç®å½
		   java.io.tmpdir â é»è®¤ä¸´æ¶æä»¶è·¯å¾
		 -->
		<diskStore path="D:\Java\test-ehcache-cache"/>

		<!--
		   defaultCacheï¼é»è®¤ç¼å­ç­ç¥ï¼å½ehcacheæ¾ä¸å°å®ä¹çç¼å­æ¶ï¼åä½¿ç¨è¿ä¸ªç¼å­ç­ç¥ãåªè½å®ä¹ä¸ä¸ªã
		 -->

		<!--
		  name:ç¼å­åç§°ã
		  maxElementsInMemory:ç¼å­æå¤§æ°ç®
		  maxElementsOnDiskï¼ç¡¬çæå¤§ç¼å­ä¸ªæ°ã
		  eternal:å¯¹è±¡æ¯å¦æ°¸ä¹ææï¼ä¸ä½è®¾ç½®äºï¼timeoutå°ä¸èµ·ä½ç¨ã
		  overflowToDisk:æ¯å¦ä¿å­å°ç£çï¼å½ç³»ç»å½æºæ¶
		  timeToIdleSeconds:è®¾ç½®å¯¹è±¡å¨å¤±æåçåè®¸é²ç½®æ¶é´ï¼åä½ï¼ç§ï¼ãä»å½eternal=falseå¯¹è±¡ä¸æ¯æ°¸ä¹æææ¶ä½¿ç¨ï¼å¯éå±æ§ï¼é»è®¤å¼æ¯0ï¼ä¹å°±æ¯å¯é²ç½®æ¶é´æ ç©·å¤§ã
		  timeToLiveSeconds:è®¾ç½®å¯¹è±¡å¨å¤±æååè®¸å­æ´»æ¶é´ï¼åä½ï¼ç§ï¼ãæå¤§æ¶é´ä»äºåå»ºæ¶é´åå¤±ææ¶é´ä¹é´ãä»å½eternal=falseå¯¹è±¡ä¸æ¯æ°¸ä¹æææ¶ä½¿ç¨ï¼é»è®¤æ¯0.ï¼ä¹å°±æ¯å¯¹è±¡å­æ´»æ¶é´æ ç©·å¤§ã
		  diskPersistentï¼æ¯å¦ç¼å­èææºéå¯ææ°æ® Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
		  diskSpoolBufferSizeMBï¼è¿ä¸ªåæ°è®¾ç½®DiskStoreï¼ç£çç¼å­ï¼çç¼å­åºå¤§å°ãé»è®¤æ¯30MBãæ¯ä¸ªCacheé½åºè¯¥æèªå·±çä¸ä¸ªç¼å²åºã
		  diskExpiryThreadIntervalSecondsï¼ç£çå¤±æçº¿ç¨è¿è¡æ¶é´é´éï¼é»è®¤æ¯120ç§ã
		  memoryStoreEvictionPolicyï¼å½è¾¾å°maxElementsInMemoryéå¶æ¶ï¼Ehcacheå°ä¼æ ¹æ®æå®çç­ç¥å»æ¸çåå­ãé»è®¤ç­ç¥æ¯LRUï¼æè¿æå°ä½¿ç¨ï¼ãä½ å¯ä»¥è®¾ç½®ä¸ºFIFOï¼åè¿ååºï¼ææ¯LFUï¼è¾å°ä½¿ç¨ï¼ã
		  clearOnFlushï¼åå­æ°éæå¤§æ¶æ¯å¦æ¸é¤ã
		  memoryStoreEvictionPolicy:å¯éç­ç¥æï¼LRUï¼æè¿æå°ä½¿ç¨ï¼é»è®¤ç­ç¥ï¼ãFIFOï¼åè¿ååºï¼ãLFUï¼æå°è®¿é®æ¬¡æ°ï¼ã
			   FIFOï¼first in first outï¼è¿ä¸ªæ¯å¤§å®¶æççï¼åè¿ååºã
			   LFUï¼ Less Frequently Usedï¼å°±æ¯ä¸é¢ä¾å­ä¸­ä½¿ç¨çç­ç¥ï¼ç´ç½ä¸ç¹å°±æ¯è®²ä¸ç´ä»¥æ¥æå°è¢«ä½¿ç¨çãå¦ä¸é¢æè®²ï¼ç¼å­çåç´ æä¸ä¸ªhitå±æ§ï¼hitå¼æå°çå°ä¼è¢«æ¸åºç¼å­ã
			   LRUï¼Least Recently Usedï¼æè¿æå°ä½¿ç¨çï¼ç¼å­çåç´ æä¸ä¸ªæ¶é´æ³ï¼å½ç¼å­å®¹éæ»¡äºï¼èåéè¦è¾åºå°æ¹æ¥ç¼å­æ°çåç´ çæ¶åï¼é£ä¹ç°æç¼å­åç´ ä¸­æ¶é´æ³ç¦»å½åæ¶é´æè¿çåç´ å°è¢«æ¸åºç¼å­ã
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
3. å¨`application.yml`æ·»å ç¼å­éç½®
	```
	# Ehcache ç¼å­éç½®
	cache:
	  ehcache:
	    config: classpath:ehcache.xml
	```
4. å¨å¥å£ç±»æ·»å `@EnableCaching`æ³¨è§£ï¼è¡¨ç¤ºå¼å¯ç¼å­  
5. Java Bean å¯¹è±¡å®ç°åºååï¼`public class User implements Serializable`  
6. å¨éè¦ä½¿ç¨çå°æ¹ä½¿ç¨ç°å³æ³¨è§£ï¼å®ç°ç¼å­å¯ä»¥åå°ä»æ°æ®åºæ¥è¯¢çæ¬¡æ°  


## å®æ¶è°åº¦å·¥å·Quartz
**å¯ä»¥åèæç« ââââ[Quartzå®æ¶è°åº¦](https://blog.csdn.net/yesirwu/article/details/97683166)è¯¦ç»å­¦ä¹ ï¼è¿éåæä¼è¡¥ä¸è¯´æ**  
### ä»ä¹æ¯Quartz
å¨æ¥å¸¸é¡¹ç®è¿è¡ä¸­ï¼æä»¬æ»ä¼æéæ±å¨æä¸æ¶é´æ®µå¨ææ§çæ§è¡æä¸ªå¨ä½ï¼æ¯å¦æ¯å¤©å¨æä¸ªæ¶é´æ®µå¯¼åºæ¥è¡¨ï¼æèæ¯éå¤ä¹ç»è®¡ä¸æ¬¡ç°å¨å¨çº¿çç¨æ·éç­ã
å¨SpringBootä¸­æJavaèªå¸¦ç`java.util.Timer`ç±»ï¼ä¹å¯ä»¥å¨å¯å¨ç±»æ·»å `@EnableScheduling`æ³¨è§£å¼å¥å®æ¶ä»»å¡ç¯å¢

### Quartzçä½¿ç¨
1. å¨`pom.xml`æ·»å ä¾èµ
	```
	<!--Quartzå·¥å·ä¾èµ-->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-quartz</artifactId>
	</dependency>
	```
2. æ·»å `jobå`å¹¶ç¼å`jobä»»å¡`ï¼å®ç°`jobæ¥å£`ï¼å¹¶å¨`executeæ¹æ³`ä¸­å®ç°èªå·±çä¸å¡é»è¾  
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

			// è·åæ´çå¥½çæ¥ææ¶é´
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// æ¥è¯¢è§¦åå¨åç§°åè§¦åå¨å±äºåªä¸ªåç»
			TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();

			//æå°æ¥å¿
			logger.info("å½åè§¦åå¨æ¯: " + triggerKey.getName() + "ï¼å®æå±çç»å«æ¯: " + triggerKey.getGroup() +
					"----------è§¦åæ¶é´: " + simpleDateFormat.format(new Date()) +
					"-->" + "Hello fx67ll Spring Boot Quartz......");
		}
	}
	```
3. æå»ºè°åº¦éç½®ç±»ï¼åå»ºJobDetailå®ä¾å¹¶å®ä¹Triggeræ³¨åå°schedulerï¼å¯å¨schedulerå¼å¯è°åº¦  
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
		 * å·ä½çå¯ä»¥è¢«æ§è¡çè°åº¦ç¨åº
		 */
		public JobDetail jobDetailTestQuartz(){
			return JobBuilder.newJob(TestQuartzJob.class).storeDurably().build();
		}

		@Bean
		/**
		 * ç¬¬ä¸ä¸ªæµè¯è§¦åå¨ï¼ä¸»è¦æ¯éç½®åæ°æç¤ºä»ä¹æ¶åè°ç¨
		 * åºç¨åºæ¯ææ¯å¦å®æ¶åéé®ä»¶ä¹ç±»ç
		 */
		public Trigger triggerTestQuartzFirst(){
			SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					// æ¯äºç§æ§è¡ä¸æ¬¡
					.withIntervalInSeconds(1)
					// æ°¸ä¹éå¤ï¼ä¸ç´æ§è¡ä¸å»
					.repeatForever();
			return TriggerBuilder.newTrigger()
					// è®¾ç½®è§¦åå¨åç§°ååç»
					.withIdentity("triggerTestQuartzFirst","groupTestQuartz")
					.withSchedule(simpleScheduleBuilder)
					.forJob(jobDetailTestQuartz())
					.build();
		}

		@Bean
		/**
		 * ç¬¬äºä¸ªæµè¯è§¦åå¨
		 */
		public Trigger triggerTestQuartzSecond(){
			return TriggerBuilder.newTrigger()
					// è®¾ç½®è§¦åå¨åç§°ååç»
					.withIdentity("triggerTestQuartzSecond","groupTestQuartz")
					// è¿éæ¯éè¿å®ä¹è¡¨è¾¾å¼æ¥è¡¨ç¤ºæ¯5ç§æ§è¡ä¸æ¬¡ï¼åç»­åæ·±å¥ç ç©¶ä¸
					.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? *"))
					.forJob(jobDetailTestQuartz())
					.build();
		}
	}
	```



## éå½
### æä½ä»£ç ç®å½è¯´æ
|  springboot-quickstart  |  springboot-mybatis  |  springboot-mybatis-crud  |  springboot-mybatis-crud-prod  |
|  :----:  |  :----:  |  :----:  |  :----:  |
|  å¿«éå¥é¨  |  æ´åmybatis  |  æ´å¥crudæä½  |  çäº§ç¯å¢å¼å  |
#### æä½ä»£ç èµæºå°å
1. [springboot-quickstart](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-quickstart)  
2. [springboot-mybatis](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis)  
3. [springboot-mybatis-crud](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis-crud)  
4. [springboot-mybatis-crud-prod](https://github.com/fx67ll/fx67llBigData/tree/main/note/springboot/springboot_projects/springboot-mybatis-crud-prod)  

### åèèµæ
1. [åèæç¨ ââââ ä¸¤å¤©æå®SpringBootæ¡æ¶](https://www.bilibili.com/video/BV16i4y197zh)  
2. [åèææ¡£ ââââ JavaSpringBoot ä¸­ @Autowiredç¨æ³](https://blog.csdn.net/weixin_41290863/article/details/111568023)  
3. [åèææ¡£ ââââ SpringBoot - @Configurationã@Beanæ³¨è§£çä½¿ç¨è¯¦è§£ï¼éç½®ç±»çå®ç°ï¼](https://www.hangge.com/blog/cache/detail_2506.html)  
4. [åèææ¡£ ââââ ãSpring BootãSpringåºç¡ ââ ç»åæ³¨è§£ä¸åæ³¨è§£](https://blog.csdn.net/the_ZED/article/details/105456946)  
5. [åèææ¡£ ââââ @RestController å @Controller çåºå«](https://blog.csdn.net/nimoyaoww/article/details/82999057)  
6. [åèææ¡£ ââââ MapperScanæ³¨è§£è¯¦è§£](https://blog.csdn.net/weixin_44093802/article/details/115601973)  
7. [åèææ¡£ ââââ Mapper.xmlè¯¦è§£](https://blog.csdn.net/qq_36850813/article/details/80037363)  
8. [åèææ¡£ ââââ MVCä¸å±æ¶æï¼è¯¦è§£ï¼](https://blog.csdn.net/qq_48508278/article/details/122648284)  
9. [åèææ¡£ ââââ éç½®devtoolsç­é¨ç½²](https://www.cnblogs.com/charlottepl/p/14694865.html)  
10. [åèææ¡£ ââââ (åä¸)SpringBoot2.0ç­é¨ç½²Devtoolsåç](https://blog.csdn.net/IT_hejinrong/article/details/89155308)  
11. [åèææ¡£ ââââ 2021çIDEAæ²¡æcompiler.automake.allow.when.app.running](https://blog.csdn.net/qq_52978553/article/details/122376118)  
12. [åèææ¡£ ââââ SpringBootåºç¡ä¹MockMvcååæµè¯](https://blog.csdn.net/wo541075754/article/details/88983708)  
13. [åèææ¡£ ââââ Ehcacheè¯¦ç»è§£è¯»](http://www.blogjava.net/libin2722/articles/406569.html)  
14. [åèææ¡£ ââââ spring bootæ¥å¥ehcache](https://blog.csdn.net/xiongzhichao/article/details/52349121)  
15. [åèææ¡£ ââââ SpringBoot(åäº)ï¼ validationå¸¸ç¨æ³¨è§£](https://blog.csdn.net/mingyuli/article/details/120434810)  
16. [åèææ¡£ ââââ SpringBootä¹ââValidatoræ ¡éªç¸å³çæ³¨è§£](https://blog.csdn.net/weixin_49716609/article/details/116003488)  
17. [åèææ¡£ ââââ å¼ºæçSpringä¹spring validation](https://blog.csdn.net/steven2xupt/article/details/87452664)  
18. [jsonæ ¼å¼æ ¡éªå¹¶æ¾ç¤ºéè¯¯_ä½¿ç¨ Spring Validation ä¼éå°è¿è¡åæ°æ ¡éª](https://blog.csdn.net/weixin_39542850/article/details/111169911)  
 

ææ¯ [fx67ll.com](https://fx67ll.com)ï¼å¦ææ¨åç°æ¬ææä»ä¹éè¯¯ï¼æ¬¢è¿å¨è¯è®ºåºè®¨è®ºææ­£ï¼æè°¢æ¨çéè¯»ï¼  
å¦ææ¨åæ¬¢è¿ç¯æç« ï¼æ¬¢è¿è®¿é®æç [æ¬ægithubä»åºå°å](https://github.com/fx67ll/fx67llBigData/blob/main/note/springboot/springboot-quickstart.md)ï¼ä¸ºæç¹ä¸é¢Starï¼Thanks~ :)  
***è½¬åè¯·æ³¨æåèæç« å°åï¼éå¸¸æè°¢ï¼ï¼ï¼***