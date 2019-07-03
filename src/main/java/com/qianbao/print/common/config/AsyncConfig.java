package com.qianbao.print.common.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Async注解所使用的线程池和异常处理配置类
 * @author lujun
 * @time 2019年4月28日10:46:08
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    //线程池配置
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(32);
        executor.setQueueCapacity(512);
        executor.setThreadNamePrefix("PrintAsyncTask-");
        executor.initialize();
        return executor;
    }
    //@Async注解发生异常时处理
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
}
