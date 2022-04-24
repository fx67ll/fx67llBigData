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

//    @Bean
//    /**
//     * 第一个测试触发器，主要是配置参数提示什么时候调用
//     * 应用场景有比如定时发送邮件之类的
//     */
//    public Trigger triggerTestQuartzFirst(){
//        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                // 每五秒执行一次
//                .withIntervalInSeconds(1)
//                // 永久重复，一直执行下去
//                .repeatForever();
//        return TriggerBuilder.newTrigger()
//                // 设置触发器名称和分组
//                .withIdentity("triggerTestQuartzFirst","groupTestQuartz")
//                .withSchedule(simpleScheduleBuilder)
//                .forJob(jobDetailTestQuartz())
//                .build();
//    }
//
//    @Bean
//    /**
//     * 第二个测试触发器
//     */
//    public Trigger triggerTestQuartzSecond(){
//        return TriggerBuilder.newTrigger()
//                // 设置触发器名称和分组
//                .withIdentity("triggerTestQuartzSecond","groupTestQuartz")
//                // 这里是通过定义表达式来表示每5秒执行一次，后续再深入研究下
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ? *"))
//                .forJob(jobDetailTestQuartz())
//                .build();
//    }
}
