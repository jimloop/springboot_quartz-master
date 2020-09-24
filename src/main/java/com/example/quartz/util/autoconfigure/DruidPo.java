package com.example.quartz.util.autoconfigure;

import lombok.Data;

@Data
public class DruidPo {
    private int initialSize;
    private int minIdle;
    private int maxActive;
}
