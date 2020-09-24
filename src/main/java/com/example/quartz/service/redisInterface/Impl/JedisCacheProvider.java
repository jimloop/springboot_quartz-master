package com.example.quartz.service.redisInterface.Impl;

import com.example.quartz.service.redisInterface.ICacheProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JedisCacheProvider implements ICacheProvider {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void setString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
