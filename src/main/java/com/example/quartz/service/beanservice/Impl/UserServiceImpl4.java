package com.example.quartz.service.beanservice.Impl;

import com.example.quartz.domain.TUser;
import com.example.quartz.domain.User;
import com.example.quartz.mapper.TUserMapper;
import com.example.quartz.service.beanservice.UserService;
import com.example.quartz.util.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service(value = "userServiceImpl4")
public class UserServiceImpl4 implements UserService {

    @Autowired
    TUserMapper userMapper;
    @Autowired
    RedisCacheUtil redisCacheUtil;

    @Value("${timeOut}")
    private long timeOut;

    @Override
    public TUser getUser(String userId) {
        String key="user"+userId;
        TUser user=(TUser)redisCacheUtil.getValueOfObject(key);
        String keySign=key+"_sign";
        String valueSign=redisCacheUtil.getValue(keySign);
        if(user==null || "".equals(user.getId())){
            user=userMapper.selectByPrimaryKey(userId);
            redisCacheUtil.set(key,user);
            redisCacheUtil.set(keySign,"1",timeOut*(new Random().nextInt(10)+1));
            return user;
        }
        if(valueSign!=null){
            return user;
        }else {
            //设置标记的失效时间
            Long times=timeOut*(new Random().nextInt(10)+1);
            System.out.println("tt:"+times);
            redisCacheUtil.set(keySign,"1",times);
            //异步处理缓存更新   应对于高并发的情况，会产生脏读的情况
        }

        return null;
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
