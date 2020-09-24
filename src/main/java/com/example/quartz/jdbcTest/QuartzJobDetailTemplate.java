package com.example.quartz.jdbcTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QuartzJobDetailTemplate {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    public void reflectionTest(String name,Integer age){
        System.out.println("Username is "+name+" and age is "+age+"*****************************");
    }
}
