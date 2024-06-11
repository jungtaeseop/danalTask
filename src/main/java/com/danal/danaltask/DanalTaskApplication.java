package com.danal.danaltask;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DanalTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanalTaskApplication.class, args);
	}

}
