package com.example.quartz.jdbcTest;

import com.example.quartz.domain.SchedulerJob;
import com.example.quartz.util.JobConstant;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class QuartzManager {

     @Autowired
     private Scheduler scheduler;

     private static final Logger logger= LoggerFactory.getLogger(QuartzManager.class);

     private static final String JOB_GROUP_NAME = "DEFAULT_JOB_GROUP_NAME";
     private static final String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGER_GROUP_NAME";

    /**
     * @Description 启动调度器
     *
     */
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 开始一个simpleSchedule()调度
     */
    public void addTemplateJob(){
        try{
            JobDataMap dataMap=new JobDataMap();
            dataMap.put("className","com.example.quartz.jdbcTest.QuartzJobDetailTemplate");
            dataMap.put("methodName","reflectionTest");
            Map paramsMap=new HashMap<>();
            paramsMap.put("name","lixf");
            paramsMap.put("age","22");
            dataMap.put("params",paramsMap);
            List<Object> params=new ArrayList<>();
            params.add("lixf");
            params.add(22);
            SchedulerJob schedulerJob=new SchedulerJob(UUID.randomUUID().toString().replaceAll("-",""),"job_01","jdbcGroup", JobConstant.STATUS_NOT_RUNNING,"0/3 * * * * ?","测试任务","com.example.quartz.jdbcTest.QuartzJobDetailTemplate","reflectionTest",JobConstant.CONCURRENT_IS,null,params, LocalDateTime.now(),LocalDateTime.now());
            //1.创建一个JobDetail实例，指定Quartz
            JobDetail jobDetail= JobBuilder.newJob(MyJob.class)
                                               .storeDurably(true)
                                               .withIdentity(schedulerJob.getJobName(),schedulerJob.getJobGroup())
                                               .build();
            jobDetail.getJobDataMap().put("schedulerJob",schedulerJob);
            //创建simpleSchedule
            //SimpleScheduleBuilder simpleScheduleBuilder=SimpleScheduleBuilder.repeatSecondlyForTotalCount(5);
            CronScheduleBuilder cronScheduleBuilder=CronScheduleBuilder.cronSchedule(schedulerJob.getCronExpression());
            //2.创建Trigger
            Trigger trigger=TriggerBuilder.newTrigger().withIdentity("trigger1","tGroup1")
                                                       .withSchedule(cronScheduleBuilder)
                                                        .build();
            TriggerKey triggerKey=TriggerKey.triggerKey("trigger1","tGroup1");
            //3.创建核心调度器 Scheduler
            if(scheduler.checkExists(triggerKey)) {
                removeJob(triggerKey,jobDetail.getKey());
            }
            //4.调度执行
            scheduler.scheduleJob(jobDetail, trigger);
               // Thread.sleep(60000);
            //scheduler.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeJobStatus(SchedulerJob schedulerJob,String status){
           if(JobConstant.STATUS_RUNNING.equals(status)){

           }
    }

    public void addJob(SchedulerJob schedulerJob){
        try{
            if(schedulerJob==null || JobConstant.STATUS_RUNNING.equals(schedulerJob.getJobStatus())) return;
            logger.debug("------开始新增任务:{}",schedulerJob.toString());
            TriggerKey triggerKey=TriggerKey.triggerKey(schedulerJob.getJobName(),schedulerJob.getJobGroup());
            CronTrigger cronTrigger=(CronTrigger) scheduler.getTrigger(triggerKey);
            if(null==cronTrigger){
                JobDetail jobDetail=JobBuilder.newJob(MyJob.class)
                                                .withIdentity(schedulerJob.getJobName(),schedulerJob.getJobGroup())
                                                .build();
                jobDetail.getJobDataMap().put("schedulerJob",schedulerJob);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void pauseJob(SchedulerJob schedulerJob){
        JobKey jobKey=JobKey.jobKey(schedulerJob.getJobName(),schedulerJob.getJobGroup());
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中找到已存在的job，并重新开启调度
     */
    public void resumeJob(String jobName,String jobGroupName){
        try{
            JobKey jobKey=new JobKey(jobName,jobGroupName);
            List< ? extends Trigger> triggers=scheduler.getTriggersOfJob(jobKey);
            if(triggers.size()>0){
                for (Trigger tg:triggers){
                    if((tg instanceof CronTrigger) || (tg instanceof SimpleTrigger)){
                        //恢复job运行
                        scheduler.resumeJob(jobKey);
                    }
                }
            }
        }catch (Exception e){
            logger.error("恢复任务");
            e.printStackTrace();
        }
    }

    /**
     * 删除定时任务
     * @param triggerKey
     * @param jobKey
     */
    public void removeJob(TriggerKey triggerKey,JobKey jobKey) {
        try {
            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
