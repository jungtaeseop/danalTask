package com.danal.danaltask.config.batch.logger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job {} is starting", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job {} completed successfully", jobExecution.getJobInstance().getJobName());
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			log.error("Job {} failed with the following exceptions:", jobExecution.getJobInstance().getJobName());
			for (Throwable t : jobExecution.getAllFailureExceptions()) {
				log.error("Exception: ", t);
			}
		}
	}
}
