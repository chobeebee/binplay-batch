package com.sparta.binplaybatch.batch;

import com.sparta.binplaybatch.batch.listener.JobCompletionNotificationListener;
import com.sparta.binplaybatch.batch.service.BatchStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchStatConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final BatchStatisticService batchStatisticService;

    @Bean
    public Job statVideoJob(JobCompletionNotificationListener listener, Step statVideoStep) {
        return new JobBuilder("statVideoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(statVideoStep)
                .build();
    }

    @Bean
    public Step statVideoStep() {
        return new StepBuilder("statVideoStep", jobRepository)
                .tasklet(statVideoTasklet(), transactionManager).build();
    }

    @Bean
    public Tasklet statVideoTasklet() {
        return (contribution, chunkContext) -> {
            // 1일 비디오 통계
            //LocalDate date = LocalDate.now().minusDays(1);
            batchStatisticService.updateDailyViewVideo(LocalDate.now().minusDays(1));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job statAdJob(JobCompletionNotificationListener listener, Step statAdStep) {
        return new JobBuilder("statAdJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(statAdStep)
                .build();
    }

    @Bean
    public Step statAdStep() {
        return new StepBuilder("statAdStep", jobRepository)
                .tasklet(statAdTasklet(), transactionManager).build();
    }

    @Bean
    public Tasklet statAdTasklet() {
        return (contribution, chunkContext) -> {
            // 1일 광고 통계
            //LocalDate date = LocalDate.now().minusDays(1);
            batchStatisticService.updateDailyViewAd(LocalDate.now().minusDays(1));
            return RepeatStatus.FINISHED;
        };
    }
}
