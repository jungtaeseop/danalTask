package com.danal.danaltask.config.batch;

import java.util.Arrays;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
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
	public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new JobBuilder("csv-job",jobRepository)
			.listener(jobLoggerListener())
			.flow(setp(jobRepository, transactionManager))
			.end()
			.build();
	}

	@Bean
	public Step setp(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("csv-step", jobRepository)
			.<StoreInfoDto, StoreInfo>chunk(CHUNK_SIZE, transactionManager)
			.reader(itemReader())
			.processor(storeInfoProcessor())
			.writer(itemWriter())
			.taskExecutor(taskExecutor())
			.listener(stepLoggerListener())
			.build();
	}

	public FlatFileItemReader<StoreInfoDto> itemReader(){
		FlatFileItemReader<StoreInfoDto> itemReader = new FlatFileItemReader<>();

		itemReader.setResource(new FileSystemResource("src/main/resources/storeinfo/소상공인시장진흥공단_상가(상권)정보_강원_202403.csv"));
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

	private TaskExecutor taskExecutor() {
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
