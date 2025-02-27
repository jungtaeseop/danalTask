package com.danal.danaltask.config.batch;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.danal.danaltask.config.batch.logger.JobLoggerListener;
import com.danal.danaltask.config.batch.logger.StepLoggerListener;
import com.danal.danaltask.data.StoreInfoDto;
import com.danal.danaltask.domain.StoreInfo;
import com.danal.danaltask.repository.StoreInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchFileConfiguration {
	private static final String[] FIELD_NAMES = {
		"storeNumber", "storeName", "branchName", "largeCategoryCode", "largeCategoryName",
		"mediumCategoryCode", "mediumCategoryName", "smallCategoryCode", "smallCategoryName",
		"standardIndustryCode", "standardIndustryName", "cityCode", "cityName", "districtCode",
		"districtName", "adminDongCode", "adminDongName", "legalDongCode", "legalDongName",
		"lotCode", "landDivisionCode", "landDivisionName", "lotMainNumber", "lotSubNumber",
		"lotAddress", "roadNameCode", "roadName", "buildingMainNumber", "buildingSubNumber",
		"buildingManagementNumber", "buildingName", "roadNameAddress", "oldZipCode", "newZipCode",
		"dongInfo", "floorInfo", "roomInfo", "longitude", "latitude"
	};
	private static final String DELIMITER = ",";
	private static final int CHUNK_SIZE = 10;


	private final StoreInfoRepository storeInfoRepository;

	@Bean(name="csvJob")
	public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
		return new JobBuilder("csv-job",jobRepository)
			.listener(jobLoggerListener())
			.flow(setp(jobRepository, transactionManager))
			.end()
			.build();
	}

	@Bean
	public Step setp(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {
		return new StepBuilder("csv-step", jobRepository)
			.<StoreInfoDto, StoreInfo>chunk(CHUNK_SIZE, transactionManager)
			.reader(multiResourceReader())
			.processor(storeInfoProcessor())
			.writer(itemWriter())
			.taskExecutor(taskExecutor())
			.listener(stepLoggerListener())
			.build();
	}


	@Bean
	public MultiResourceItemReader<StoreInfoDto> multiResourceReader() throws IOException {
		MultiResourceItemReader<StoreInfoDto> reader = new MultiResourceItemReader<>();
		reader.setDelegate(itemReader());
		reader.setResources(new PathMatchingResourcePatternResolver()
			.getResources("classpath:storeinfo/*.csv"));
		return reader;
	}

	public FlatFileItemReader<StoreInfoDto> itemReader() {
		FlatFileItemReader<StoreInfoDto> itemReader = new FlatFileItemReader<>();
		itemReader.setName("csv-reader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());

		return itemReader;
	}

	private LineMapper<StoreInfoDto> lineMapper() {
		DefaultLineMapper<StoreInfoDto> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = createTokenizer();
		BeanWrapperFieldSetMapper<StoreInfoDto> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setTargetType(StoreInfoDto.class);

		lineMapper.setFieldSetMapper(mapper);
		lineMapper.setLineTokenizer(tokenizer);
		return lineMapper;
	}

	private DelimitedLineTokenizer createTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(DELIMITER);
		tokenizer.setNames(FIELD_NAMES);
		tokenizer.setStrict(false);
		return tokenizer;
	}

	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
		asyncTaskExecutor.setConcurrencyLimit(10);
		return asyncTaskExecutor;
	}

	@Bean
	public StoreInfoProcessor storeInfoProcessor() {
		return new StoreInfoProcessor();
	}

	@Bean
	public RepositoryItemWriter<StoreInfo> itemWriter() {
		RepositoryItemWriter<StoreInfo> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(storeInfoRepository);
		itemWriter.setMethodName("save");
		return itemWriter;
	}

	@Bean
	public JobExecutionListener jobLoggerListener() {
		return new JobLoggerListener();
	}

	@Bean
	public StepExecutionListener stepLoggerListener() {
		return new StepLoggerListener();
	}
}
