package com.example.quartz.demo;

import com.example.quartz.DemoApplication;
import com.example.quartz.service.redisInterface.ICacheProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class DemoApplicationTests {

    @Autowired
    ICacheProvider jedisCacheProvider;

    @Test
    public void contextLoads() {
        jedisCacheProvider.setString("name","terrylmay");
        System.out.println(jedisCacheProvider.getString("name"));
        Assert.assertEquals("terrylmay",jedisCacheProvider.getString("name"));
    }

}
