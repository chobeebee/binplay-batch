package com.sparta.binplaybatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
public class BatchConfig {

    @Bean(name = "batchTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 기본 스레드 수
        executor.setMaxPoolSize(10); // 최대 스레드 수
        executor.setQueueCapacity(1000); // 대기 큐 크기
        executor.setThreadNamePrefix("batchTaskExecutor-");
        executor.initialize();

        // 실행 중인 스레드 수를 주기적으로 로그에 기록
        /*new Thread(() -> {
            while (true) {
                log.info("Active Threads: {}", executor.getActiveCount());
                log.info("Pool Size: {}", executor.getPoolSize());
                log.info("Queue Size: {}", executor.getThreadPoolExecutor().getQueue().size());
                try {
                    Thread.sleep(1000); // 10초마다 로그 기록
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); break;
                }
            }}).start();*/

        return executor;
    }
}
