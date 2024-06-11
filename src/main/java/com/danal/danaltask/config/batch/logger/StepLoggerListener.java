package com.danal.danaltask.config.batch.logger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepLoggerListener implements StepExecutionListener {
	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.info("Step {} is starting", stepExecution.getStepName());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Step {} completed successfully", stepExecution.getStepName());
		} else if (stepExecution.getStatus() == BatchStatus.FAILED) {
			log.error("Step {} failed with the following exceptions:", stepExecution.getStepName());
			for (Throwable t : stepExecution.getFailureExceptions()) {
				log.error("Exception: ", t);
			}
		}
		return stepExecution.getExitStatus();
	}
}
