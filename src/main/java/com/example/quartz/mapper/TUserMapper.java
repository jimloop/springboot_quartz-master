package com.example.quartz.mapper;

import com.example.quartz.domain.TUser;

public interface TUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);
}