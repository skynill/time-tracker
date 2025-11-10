package com.skynill.timetracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@Slf4j
@EnableRetry
@SpringBootApplication
public class TimeTrackerApplication {

    public static void main(String[] args) {
        log.info("Starting time recording service...");
        SpringApplication.run(TimeTrackerApplication.class, args);
    }

}
