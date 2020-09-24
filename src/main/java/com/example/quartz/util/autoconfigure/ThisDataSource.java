package com.example.quartz.util.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据源实体
 */

@ConfigurationProperties(prefix = "spring.datasource", ignoreUnknownFields = true)
public class ThisDataSource {
    private String type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private DruidPo druid;

    public DruidPo getDruid() {
        return druid;
    }

    public void setDruid(DruidPo druid) {
        this.druid = druid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
