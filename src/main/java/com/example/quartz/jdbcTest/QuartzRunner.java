package com.example.quartz.jdbcTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QuartzRunner implements CommandLineRunner {

    @Autowired
    QuartzManager quartzManager;
    @Override
    public void run(String... args) throws Exception {
        quartzManager.startJobs();
    }
}
