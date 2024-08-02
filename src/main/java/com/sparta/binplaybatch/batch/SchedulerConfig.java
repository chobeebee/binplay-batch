package com.sparta.binplaybatch.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static com.sparta.binplaybatch.batch.listener.CustomJobListener.*;

@Configuration
@EnableScheduling
//@RequiredArgsConstructor
//@Component
public class SchedulerConfig {
    private final JobLauncher jobLauncher;
    private final Job statVideoJob;
    private final Job statAdJob;
    private final Job paymentVideoJob;
    private final Job paymentAdJob;
    private final TaskExecutor taskExecutor;

    public SchedulerConfig(JobLauncher jobLauncher, Job statVideoJob, Job statAdJob, Job paymentVideoJob, Job paymentAdJob, @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor) {
        this.jobLauncher = jobLauncher;
        this.statVideoJob = statVideoJob;
        this.statAdJob = statAdJob;
        this.paymentVideoJob = paymentVideoJob;
        this.paymentAdJob = paymentAdJob;
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(cron = "0 10 0 * * *") //매일 자정 10분에 실행
    public void runJobs() {

        try {
            System.out.println("\n[순차] 잡 실행 시작");

            JobExecution sequentialExecution1 = runJob(statVideoJob, "순차");
            Objects.requireNonNull(sequentialExecution1).getJobParameters();
            JobExecution sequentialExecution2 = runJob(statAdJob, "순차");
            Objects.requireNonNull(sequentialExecution2).getJobParameters();
            JobExecution sequentialExecution3 = runJob(paymentVideoJob, "순차");
            Objects.requireNonNull(sequentialExecution3).getJobParameters();
            JobExecution sequentialExecution4 = runJob(paymentAdJob, "순차");
            Objects.requireNonNull(sequentialExecution4).getJobParameters();

            while (sequentialExecution1.isRunning() || sequentialExecution2.isRunning()|| sequentialExecution3.isRunning()|| sequentialExecution4.isRunning()) {
                Thread.sleep(100);
            }

            System.out.println("[병렬] 잡 실행 시작\n");

            // 첫 번째 단계 병렬 실행
            runParallelJobs(Arrays.asList(statVideoJob, statAdJob));

            // 두 번째 단계 병렬 실행
            runParallelJobs(Arrays.asList(paymentVideoJob, paymentAdJob));

            printSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runParallelJobs(List<Job> jobs) throws InterruptedException {
        List<Runnable> tasks = new ArrayList<>();
        for (Job job : jobs) {
            tasks.add(() -> runJob(job, "병렬"));
        }

        CountDownLatch latch = new CountDownLatch(tasks.size());

        for (Runnable task : tasks) {
            taskExecutor.execute(() -> {
                task.run();
                latch.countDown();
            });
        }

        latch.await();
    }

    private JobExecution runJob(Job job, String mode) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("mode", mode)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);
            while (jobExecution.isRunning()) {
                Thread.sleep(100);
            }
            return jobExecution;
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void printSummary() {
        System.out.println("\n=== 배치 Job 병렬처리 테스트 Summary ===");
        if (sequentialJobCount > 0) {
            System.out.println("순차 총 소요시간: " + sequentialTotalTime + " ms");
        }
        if (parallelStartTime > 0 && parallelEndTime > 0) {
            long parallelTotalTime = parallelEndTime - parallelStartTime;
            System.out.println("병렬 총 소요시간: " + parallelTotalTime + " ms");
        }
        System.out.println("순차와 병렬의 총 소요시간 차이: " + (parallelEndTime - parallelStartTime - sequentialTotalTime) * (-1) + " ms");
    }
}
