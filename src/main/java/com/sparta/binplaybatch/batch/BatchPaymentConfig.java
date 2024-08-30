package com.sparta.binplaybatch.batch;

import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.batch.listener.CustomJobListener;
import com.sparta.binplaybatch.batch.listener.CustomStepListener;
import com.sparta.binplaybatch.batch.processor.AdPaymentProcessor;
import com.sparta.binplaybatch.batch.processor.VideoPaymentProcessor;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchPaymentConfig {
    private final CustomJobListener customJobListener;
    private final CustomStepListener customStepListener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final VideoPaymentProcessor videoPaymentProcessor;
    private final AdPaymentProcessor adPaymentProcessor;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job paymentVideoJob(Step paymentVideoStep) {
        return new JobBuilder("paymentVideoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(customJobListener)
                .start(paymentVideoStep)
                .build();
    }

    @Bean
    public Step paymentVideoStep(@Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        return new StepBuilder("paymentVideoStep", jobRepository)
                .<StatisticVideo, PaymentVideo>chunk(500, transactionManager)
                .reader(videoPaymentReader())
                .processor(videoPaymentProcessor)
                .writer(videoPaymentsJpaItemWriter())
                .listener(customStepListener)
                .taskExecutor(taskExecutor)
                .build();
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
    public Step paymentAdStep(@Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        return new StepBuilder("paymentAdStep", jobRepository)
                .<StatisticAd, PaymentAd>chunk(500, transactionManager)
                .reader(adPaymentReader())
                .processor(adPaymentProcessor)
                .writer(adPaymentsJpaItemWriter())
                .listener(customStepListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public JpaPagingItemReader<StatisticVideo> videoPaymentReader() {
        return new JpaPagingItemReaderBuilder<StatisticVideo>()
                .name("videoPaymentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT v FROM StatisticVideo v")
                .pageSize(10)
                .saveState(false)
                .build();
    }

    @Bean
    public JpaItemWriter<PaymentVideo> videoPaymentsJpaItemWriter() {
        JpaItemWriter<PaymentVideo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public JpaPagingItemReader<StatisticAd> adPaymentReader() {
        return new JpaPagingItemReaderBuilder<StatisticAd>()
                .name("adPaymentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT v FROM StatisticAd v")
                .pageSize(10)
                .saveState(false)
                .build();
    }

    @Bean
    public JpaItemWriter<PaymentAd> adPaymentsJpaItemWriter() {
        JpaItemWriter<PaymentAd> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
