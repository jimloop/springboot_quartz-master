package com.example.quartz.service.beanservice.Impl;

import com.example.quartz.domain.TUser;
import com.example.quartz.domain.User;
import com.example.quartz.mapper.TUserMapper;
import com.example.quartz.mapper.UserMapper;
import com.example.quartz.service.beanservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    private static Logger log= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired(required = false)
    TUserMapper userMapper;

    @Override
    @Cacheable(key = "caches[0].name+T(String).valueOf(#userId)",unless = "#result eq null")
    public TUser getUser(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public String getIdByName(String name) {
        return null;
    }

    @Override
    public User gerUserByName(String name) {
        return null;
    }

}
