package com.example.quartz.service.beanservice;

import com.example.quartz.domain.TUser;
import com.example.quartz.domain.User;

public interface UserService {
    TUser getUser(String userId);
    String getIdByName(String name);
    User gerUserByName(String name);
}
