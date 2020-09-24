package com.example.quartz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchedulerJob implements Serializable {
    private String jobId;

    private String jobName;

    private String jobGroup;

    /**
     * 任务状态  是否启动任务
     */
    private String jobStatus;

    /**
     * Cron 表达式
     */
    private String cronExpression;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务执行时调用哪个类的方法，全限定名
     */
    private String beanClass;

    /**
     * 任务执行的方法名
     */
    private String methodName;

    /**
     * 任务是否有状态
     */
    private String isConcurrent;

    /**
     * spring bean
     */
    private String springId;

    /**
     * 方法参数
     */
    private List<?> methodParams;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
