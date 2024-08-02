package com.sparta.binplaybatch.batch;

import com.sparta.binplaybatch.batch.listener.CustomJobListener;
import com.sparta.binplaybatch.batch.listener.CustomStepListener;
import com.sparta.binplaybatch.batch.service.BatchPaymentService;
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
public class BatchPaymentConfig {
    private final CustomJobListener customJobListener;
    private final CustomStepListener customStepListener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BatchPaymentService batchPaymentService;

    @Bean
    public Job paymentVideoJob(Step paymentVideoStep) {
        return new JobBuilder("paymentVideoJob", jobRepository)
                //.incrementer(new RunIdIncrementer())
                .preventRestart()
                .listener(customJobListener)
                .start(paymentVideoStep)
                .build();
    }

    @Bean
    public Step paymentVideoStep() {
        return new StepBuilder("paymentVideoStep", jobRepository)
                .tasklet(paymentVideoTasklet(), platformTransactionManager).listener(customStepListener).build();
    }

    @Bean
    public Tasklet paymentVideoTasklet() {
        return (contribution, chunkContext) -> {
            // 1일 비디오 정산
            batchPaymentService.calculateVideoPmt(LocalDate.now().minusDays(1));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job paymentAdJob(Step paymentAdStep) {
        return new JobBuilder("paymentAdJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(customJobListener)
                .start(paymentAdStep)
                .build();
    }

    @Bean
    public Step paymentAdStep() {
        return new StepBuilder("paymentAdStep", jobRepository)
                .tasklet(paymentAdTasklet(), platformTransactionManager).listener(customStepListener).build();
    }

    @Bean
    public Tasklet paymentAdTasklet() {
        return (contribution, chunkContext) -> {
            // 1일 광고 정산
            batchPaymentService.calculateAdPmt(LocalDate.now().minusDays(1));
            return RepeatStatus.FINISHED;
        };
    }
}
