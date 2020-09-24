package com.example.quartz.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class ApplicationContextHelper implements ApplicationContextAware , DisposableBean {
    private static volatile ApplicationContext context=null;
    private static Logger logger= LoggerFactory.getLogger(ApplicationContextHelper.class);

    public ApplicationContextHelper(){
    }

    public static ApplicationContext getContext(){
         assertContextInjected();
         return context;
    }

    public static <T> T getBean(String name){
        assertContextInjected();
        return (T)context.getBean(name);
    }

    public static <T> T getBean(String name,Class<T> requiredType){
        assertContextInjected();
        return context.getBean(name,requiredType);
    }

    public static <T> T getBean(Class<T> requiredType){
        assertContextInjected();
        return context.getBean(requiredType);
    }

    public static <T> T getBeanByClassName(String className){
        assertContextInjected();
        Class cls=null;
        try{
            cls=Class.forName(className);
        }catch (ClassNotFoundException e){
            logger.error("类:"+className+"不存在！");
            throw new RuntimeException("类:"+className+"不存在！",e);
        }
        return (T)context.getBean(cls);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("注入ApplicationContext到ApplicationContextHelper:{}",applicationContext);
        if(context!=null) {
            logger.warn("ApplicationContextHelper中的Context被覆盖，原有context为:{}",context);
        }
        context = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        clearContext();
    }

    private static void clearContext(){
        logger.debug("清空ApplicationContextHelper中的ApplicationContext:{}",context);
        context=null;
    }

    private static void assertContextInjected(){
        Validate.validState(context !=null,"context属性未注入，请在applicationContext.xml中定义ContextHelper",new Object[0]);
    }
}
