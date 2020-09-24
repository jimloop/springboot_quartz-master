package com.example.quartz.demo.Job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class RAMQuartz {
    private static Logger _log= LoggerFactory.getLogger(RAMQuartz.class);

    public static void main(String[] args)throws SchedulerException {
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=schedulerFactory.getScheduler();
        JobDetail jobDetail= JobBuilder.newJob(RAMJob.class).withDescription("This is a RAM job")
                .withIdentity("ramJob","ramGroup").build();
        long time=System.currentTimeMillis()+3*1000L;//3秒后启动任务
        Date startTime=new Date(time);
        //创建触发器Trigger
        Trigger trigger=TriggerBuilder.newTrigger()
                                       .withDescription("creact Trigger")
                                       .withIdentity("ramTrigger","ramGroup")
                                       .startAt(startTime)
                                       .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                                       .build();
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();
        _log.info("启动时间：-----------"+new Date());

    }
}
