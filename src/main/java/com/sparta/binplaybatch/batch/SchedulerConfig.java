package com.sparta.binplaybatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final Job statVideoJob;
    private final Job statAdJob;
    private final Job paymentVideoJob;
    private final Job paymentAdJob;

    @Scheduled(cron = "0 0 0 * * *") // 매일 00시에 실행
    public void performDailyJob() throws Exception {
        jobLauncher.run(statVideoJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        jobLauncher.run(statAdJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis() + 1).toJobParameters());
        jobLauncher.run(paymentVideoJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis() + 2).toJobParameters());
        jobLauncher.run(paymentAdJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis() + 3).toJobParameters());
    }
}
