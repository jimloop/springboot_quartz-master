package com.example.quartz.mapper;

import com.example.quartz.domain.User;
import org.springframework.stereotype.Repository;

public interface UserMapper {
    User selectByPrimaryKey(Long userId);
    Long getIdByName(String name);
}
