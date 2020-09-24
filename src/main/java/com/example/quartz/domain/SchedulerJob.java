package com.example.quartz.domain;

import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.command.BaseModel;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SchedulerJob")
public class SchedulerJob extends BaseModel implements Serializable {

    @Column(name = "jobId",type = MySqlTypeConstant.INT,length = 11,isKey = true,isAutoIncrement = true)
    private String jobId;

    @Column(name = "jobName",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String jobName;

    @Column(name = "jobGroup",type = MySqlTypeConstant.VARCHAR,length = 128)
    private String jobGroup;

    /**
     * 任务状态  是否启动任务
     */
    @Column(name = "jobStatus",type = MySqlTypeConstant.VARCHAR,length = 25)
    private String jobStatus;

    /**
     * Cron 表达式
     */
    @Column(name = "cronExpression",type = MySqlTypeConstant.VARCHAR,length = 25)
    private String cronExpression;

    /**
     * 描述
     */
    @Column(name = "description",type = MySqlTypeConstant.VARCHAR,length = 256)
    private String description;

    /**
     * 任务执行时调用哪个类的方法，全限定名
     */
    @Column(name = "beanClass",type = MySqlTypeConstant.VARCHAR,length = 200)
    private String beanClass;

    /**
     * 任务执行的方法名
     */
    @Column(name = "methodName",type = MySqlTypeConstant.VARCHAR,length = 100)
    private String methodName;

    /**
     * 任务是否有状态
     */
    @Column(name = "isConcurrent",type = MySqlTypeConstant.VARCHAR,length = 2)
    private String isConcurrent;

    /**
     * spring bean
     */
    @Column(name = "springId",type = MySqlTypeConstant.VARCHAR,length = 100)
    private String springId;

    /**
     * 方法参数
     */
    @Column(name = "methodParams",type = MySqlTypeConstant.TEXT,length = 500)
    private List<?> methodParams;

    @Column(name = "createTime",type = MySqlTypeConstant.DATETIME)
    private LocalDateTime createTime;

    @Column(name = "updateTime",type = MySqlTypeConstant.DATETIME)
    private LocalDateTime updateTime;

}
