package com.multithreading.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
    private final Logger logger= LoggerFactory.getLogger(AsyncConfiguration.class);
    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        logger.info("Initialize the Task Executor");
        ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("UserThread-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
