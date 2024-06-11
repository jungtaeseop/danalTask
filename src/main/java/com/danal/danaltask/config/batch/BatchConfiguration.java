package com.danal.danaltask.config.batch;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import com.danal.danaltask.config.batch.logger.JobLoggerListener;
import com.danal.danaltask.config.batch.logger.StepLoggerListener;
import com.danal.danaltask.data.StoreInfoDto;
import com.danal.danaltask.domain.StoreInfo;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
	private static final int CHUNK_SIZE = 10;
	private final EntityManagerFactory entityManagerFactory;

	@Value("${file.input.path}")
	private String inputPath;

	@Bean
	public Job storeInfoJob(JobRepository jobRepository, Step storeInfoStep) {
		return new JobBuilder("storeInfoJob", jobRepository)
			.listener(jobLoggerListener1())
			.start(storeInfoStep)
			.build();
	}

	@Bean
	public Step storeInfoStep(JobRepository jobRepository,
		PlatformTransactionManager platformTransactionManager) {

		return new StepBuilder("storeInfoStep", jobRepository)
			.<StoreInfoDto, StoreInfo>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(multiResourceReader())
			.processor(storeInfoItemProcessor())
			.writer(storeInfoItemWriter())
			.listener(stepLoggerListener1())
			.build();
	}

	@Bean
	public ItemProcessor<StoreInfoDto, StoreInfo> storeInfoItemProcessor() {
		return storeInfoDto -> {
			log.info("Processing storeInfo: {}", storeInfoDto);
			return StoreInfo.builder()
				.storeNumber(storeInfoDto.getStoreNumber())
				.storeName(storeInfoDto.getStoreName())
				.branchName(storeInfoDto.getBranchName())
				.largeCategoryCode(storeInfoDto.getLargeCategoryCode())
				.largeCategoryName(storeInfoDto.getLargeCategoryName())
				.mediumCategoryCode(storeInfoDto.getMediumCategoryCode())
				.mediumCategoryName(storeInfoDto.getMediumCategoryName())
				.smallCategoryCode(storeInfoDto.getSmallCategoryCode())
				.smallCategoryName(storeInfoDto.getSmallCategoryName())
				.standardIndustryCode(storeInfoDto.getStandardIndustryCode())
				.standardIndustryName(storeInfoDto.getStandardIndustryName())
				.cityCode(storeInfoDto.getCityCode())
				.cityName(storeInfoDto.getCityName())
				.districtCode(storeInfoDto.getDistrictCode())
				.districtName(storeInfoDto.getDistrictName())
				.adminDongCode(storeInfoDto.getAdminDongCode())
				.adminDongName(storeInfoDto.getAdminDongName())
				.legalDongCode(storeInfoDto.getLegalDongCode())
				.legalDongName(storeInfoDto.getLegalDongName())
				.lotCode(storeInfoDto.getLotCode())
				.landDivisionCode(storeInfoDto.getLandDivisionCode())
				.landDivisionName(storeInfoDto.getLandDivisionName())
				.lotMainNumber(storeInfoDto.getLotMainNumber())
				.lotSubNumber(storeInfoDto.getLotSubNumber())
				.lotAddress(storeInfoDto.getLotAddress())
				.roadNameCode(storeInfoDto.getRoadNameCode())
				.roadName(storeInfoDto.getRoadName())
				.buildingMainNumber(storeInfoDto.getBuildingMainNumber())
				.buildingSubNumber(storeInfoDto.getBuildingSubNumber())
				.buildingManagementNumber(storeInfoDto.getBuildingManagementNumber())
				.buildingName(storeInfoDto.getBuildingName())
				.roadNameAddress(storeInfoDto.getRoadNameAddress())
				.oldZipCode(storeInfoDto.getOldZipCode())
				.newZipCode(storeInfoDto.getNewZipCode())
				.dongInfo(storeInfoDto.getDongInfo())
				.floorInfo(storeInfoDto.getFloorInfo())
				.roomInfo(storeInfoDto.getRoomInfo())
				.longitude(storeInfoDto.getLongitude())
				.latitude(storeInfoDto.getLatitude())
				.build();
		};
	}

	@Bean
	public JpaItemWriter<StoreInfo> storeInfoItemWriter() {
		return new JpaItemWriterBuilder<StoreInfo>()
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<StoreInfoDto> csvFileReader() {
		FlatFileItemReader<StoreInfoDto> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1); // Skip header row if needed
		reader.setLineMapper(csvLineMapper());
		return reader;
	}

	@Bean
	@StepScope
	public MultiResourceItemReader<StoreInfoDto> multiResourceReader() {
		Resource[] files = csvFileResource();
		MultiResourceItemReader<StoreInfoDto> resourceItemReader = new MultiResourceItemReader<>();
		resourceItemReader.setResources(files);
		resourceItemReader.setDelegate(csvFileReader());
		return resourceItemReader;
	}

	@Bean
	public DefaultLineMapper<StoreInfoDto> csvLineMapper() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("storeNumber","storeName","branchName","largeCategoryCode","largeCategoryName","mediumCategoryCode","mediumCategoryName","smallCategoryCode","smallCategoryName","standardIndustryCode","standardIndustryName","cityCode","cityName","districtCode","districtName","adminDongCode","adminDongName","legalDongCode","legalDongName","lotCode","landDivisionCode","landDivisionName","lotMainNumber","lotSubNumber","lotAddress","roadNameCode","roadName","buildingMainNumber","buildingSubNumber","buildingManagementNumber","buildingName","roadNameAddress","oldZipCode","newZipCode","dongInfo","floorInfo","roomInfo","longitude","latitude");

		BeanWrapperFieldSetMapper<StoreInfoDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(StoreInfoDto.class);

		DefaultLineMapper<StoreInfoDto> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);

		return lineMapper;
	}

	@Bean
	public Resource[] csvFileResource()  {
		try {
			Resource[] resources = new PathMatchingResourcePatternResolver().getResources(inputPath);
			if (resources.length == 0) {
				throw new FileNotFoundException("지정된 폴더에서 CSV 파일을 찾을 수 없습니다.");
			}
			return resources;
		}catch (IOException e) {
			throw new RuntimeException("리소스를 로드하는 데 실패했습니다.", e);
		}
	}

	@Bean
	public JobExecutionListener jobLoggerListener1() {
		return new JobLoggerListener();
	}

	@Bean
	public StepExecutionListener stepLoggerListener1() {
		return new StepLoggerListener();
	}
}