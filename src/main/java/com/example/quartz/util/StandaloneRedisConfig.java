package com.example.quartz.util;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@ConditionalOnProperty(name = {"spring.redis.host"})
public class StandaloneRedisConfig {
    String host;
    int port;
}
