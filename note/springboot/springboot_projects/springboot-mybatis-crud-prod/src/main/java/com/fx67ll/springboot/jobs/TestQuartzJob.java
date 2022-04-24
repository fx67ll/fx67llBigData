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
