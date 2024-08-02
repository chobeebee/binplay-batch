package com.sparta.binplaybatch.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Thread {} started processing step {}", Thread.currentThread().getName(), stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Thread {} started processing step {}", Thread.currentThread().getName(), stepExecution.getStepName());
        return stepExecution.getExitStatus();
    }
}
