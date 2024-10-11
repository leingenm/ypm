package com.ypm.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        var corePoolSize = Runtime.getRuntime().availableProcessors();

        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(corePoolSize * 2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncThread-");

        // executor.setTaskDecorator(runnable -> {
        //     SecurityContext context = SecurityContextHolder.getContext();
        //     return () -> {
        //         try {
        //             SecurityContextHolder.setContext(context);
        //             runnable.run();
        //         } finally {
        //             SecurityContextHolder.clearContext();
        //         }
        //     };
        // });

        executor.initialize();

        // Wrapping with DelegatingSecurityContextExecutor to propagate security context
        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
