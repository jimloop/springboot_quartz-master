package com.example.quartz.util;

import com.example.quartz.domain.SchedulerJob;
import jdk.nashorn.internal.runtime.options.Option;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JobUtils {
    private static final Logger logger= LoggerFactory.getLogger(JobUtils.class);

    /**
     * 通过反射调用schedulerJob中定义的方法
     */
    public static void invokeMethod(SchedulerJob schedulerJob){
        Object obj=null;
        Class clazz=null;
        logger.info("------------开始启动{}方法对应的任务",schedulerJob.getMethodName());
        if(StringUtils.isNoneBlank(schedulerJob.getSpringId())){
            logger.info("----------通过ApplicationContext获取对象-----------");
            obj=ApplicationContextHelper.getBean(schedulerJob.getSpringId());
        }else if(StringUtils.isNoneBlank(schedulerJob.getBeanClass())){
            try {
                logger.info("------------通过类全限定名获取对象---------------");
                clazz = Class.forName(schedulerJob.getBeanClass());
                obj=clazz.newInstance();
            }catch (Exception e) {
                logger.error("--------生成对象{}时出错",schedulerJob.getBeanClass());
            }
        }
        if(obj==null){
            logger.error("任务名称 = [" + schedulerJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
            return;
        }
        clazz=obj.getClass();
        Method method=null;
        try{
            if(schedulerJob.getMethodParams()!=null){
                 Method[] methods=clazz.getMethods();
                Optional<Method> option=Arrays.stream(methods)
                        .filter(method1 -> isSameMethod(method1,schedulerJob))
                        //.peek(x -> System.out.println("from stream: " + x))
                        //.filter(method1 -> schedulerJob.getMethodName().equals(method1.getName()))
                        //.peek(x -> System.out.println("from filter1: " + x))
                        //.filter(method1 ->  method1.getParameterCount()==schedulerJob.getMethodParams().size())
                        //.peek(x -> System.out.println("from filter2: " + x))
                        //.filter(method1 -> arrayContentsEq(schedulerJob.getMethodParams(),method1.getParameterTypes()))
                        //.peek(x -> System.out.println("from filter3: " + x))
                        .findFirst();
                method=option.orElse(null);
            }else {
                method=clazz.getMethod(schedulerJob.getMethodName(),null);
            }
        } catch (NoSuchMethodException e) {
            logger.error("任务名称 = [" + schedulerJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
        }

        if(method!=null){
            try{
                method.invoke(obj,schedulerJob.getMethodParams().toArray());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            logger.info("任务名称 = [" + schedulerJob.getJobName() + "]----------启动成功");
        }else {
            throw new RuntimeException("获取方法"+schedulerJob.getMethodName()+"失败-----------");
        }

    }

    private static boolean arrayContentsEq(List<?> a1, Object[] a2) {
        if (a1 == null) {
            return a2 == null || a2.length == 0;
        }
        if (a2 == null) {
            return a1.size() == 0;
        }
        if (a1.size() != a2.length) {
            return false;
        }
        for (int i = 0; i < a1.size(); i++) {
            logger.info("第"+i+"个元素："+a1.get(i).getClass());
            if (a1.get(i).getClass() != a2[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSameMethod(Method method1,SchedulerJob schedulerJob){
        if(schedulerJob.getMethodName().equals(method1.getName()) && method1.getParameterCount()==schedulerJob.getMethodParams().size() && arrayContentsEq(schedulerJob.getMethodParams(),method1.getParameterTypes())){
            return true;
        }else {
            return false;
        }
    }
}
