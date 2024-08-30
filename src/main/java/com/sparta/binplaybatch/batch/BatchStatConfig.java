package com.sparta.binplaybatch.batch;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.batch.listener.CustomJobListener;
import com.sparta.binplaybatch.batch.listener.CustomStepListener;
import com.sparta.binplaybatch.batch.processor.StatAdProcessor;
import com.sparta.binplaybatch.batch.processor.StatVideoProcessor;
import com.sparta.binplaybatch.entity.VideoAd;
import com.sparta.binplaybatch.entity.Videos;
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
public class BatchStatConfig {
    private final CustomJobListener customJobListener;
    private final CustomStepListener customStepListener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StatVideoProcessor statVideoProcessor;
    private final StatAdProcessor statAdProcessor;
    private final EntityManagerFactory entityManagerFactory;


    //240801 chunk
    @Bean
    public Job statVideoJob(Step statVideoStep) {
        return new JobBuilder("statVideoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(customJobListener)
                .start(statVideoStep)
                .build();
    }

    @Bean
    public Step statVideoStep(@Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        return new StepBuilder("statVideoStep", jobRepository)
                .<Videos, StatisticVideo>chunk(500, transactionManager)
                .reader(videoUpdateReader())
                .processor(statVideoProcessor)
                .writer(videoStatsJpaItemWriter())
                .listener(customStepListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job statAdJob(Step statAdStep) {
        return new JobBuilder("statAdJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(customJobListener)
                .start(statAdStep)
                .build();
    }

    @Bean
    public Step statAdStep(@Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        return new StepBuilder("statAdStep", jobRepository)
                .<VideoAd, StatisticAd>chunk(500, transactionManager)
                .reader(adUpdateReader())
                .processor(statAdProcessor)
                .writer(adStatsJpaItemWriter())
                .listener(customStepListener)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public JpaPagingItemReader<Videos> videoUpdateReader() {
        return new JpaPagingItemReaderBuilder<Videos>()
                .name("videoUpdateReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT v FROM Videos v")
                .pageSize(10)
                .saveState(false)
                .build();
    }

    @Bean
    public JpaItemWriter<StatisticVideo> videoStatsJpaItemWriter() {
        JpaItemWriter<StatisticVideo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public JpaPagingItemReader<VideoAd> adUpdateReader() {
        return new JpaPagingItemReaderBuilder<VideoAd>()
                .name("videoUpdateReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT v FROM VideoAd v")
                .pageSize(10)
                .saveState(false)
                .build();
    }

    @Bean
    public JpaItemWriter<StatisticAd> adStatsJpaItemWriter() {
        JpaItemWriter<StatisticAd> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
