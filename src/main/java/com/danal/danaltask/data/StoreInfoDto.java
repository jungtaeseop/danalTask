package com.danal.danaltask.data;

import com.danal.danaltask.domain.StoreInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreInfoDto {

	private String storeNumber; // 상가업소번호
	private String storeName; // 상호명
	private String branchName; // 지점명
	private String largeCategoryCode; // 상권업종대분류코드
	private String largeCategoryName; // 상권업종대분류명
	private String mediumCategoryCode; // 상권업종중분류코드
	private String mediumCategoryName; // 상권업종중분류명
	private String smallCategoryCode; // 상권업종소분류코드
	private String smallCategoryName; // 상권업종소분류명
	private String standardIndustryCode; // 표준산업분류코드
	private String standardIndustryName; // 표준산업분류명
	private String cityCode; // 시도코드
	private String cityName; // 시도명
	private String districtCode; // 시군구코드
	private String districtName; // 시군구명
	private String adminDongCode; // 행정동코드
	private String adminDongName; // 행정동명
	private String legalDongCode; // 법정동코드
	private String legalDongName; // 법정동명
	private String lotCode; // 지번코드
	private String landDivisionCode; // 대지구분코드
	private String landDivisionName; // 대지구분명
	private String lotMainNumber; // 지번본번지
	private String lotSubNumber; // 지번부번지
	private String lotAddress; // 지번주소
	private String roadNameCode; // 도로명코드
	private String roadName; // 도로명
	private String buildingMainNumber; // 건물본번지
	private String buildingSubNumber; // 건물부번지
	private String buildingManagementNumber; // 건물관리번호
	private String buildingName; // 건물명
	private String roadNameAddress; // 도로명주소
	private String oldZipCode; // 구우편번호
	private String newZipCode; // 신우편번호
	private String dongInfo; // 동정보
	private String floorInfo; // 층정보
	private String roomInfo; // 호정보
	private Double longitude; // 경도
	private Double latitude; // 위도

	@Builder
	public StoreInfoDto(String storeNumber, String storeName, String branchName, String largeCategoryCode,
		String largeCategoryName, String mediumCategoryCode, String mediumCategoryName, String smallCategoryCode,
		String smallCategoryName, String standardIndustryCode, String standardIndustryName, String cityCode,
		String cityName, String districtCode, String districtName, String adminDongCode, String adminDongName,
		String legalDongCode, String legalDongName, String lotCode, String landDivisionCode, String landDivisionName,
		String lotMainNumber, String lotSubNumber, String lotAddress, String roadNameCode, String roadName,
		String buildingMainNumber, String buildingSubNumber, String buildingManagementNumber, String buildingName,
		String roadNameAddress, String oldZipCode, String newZipCode, String dongInfo, String floorInfo,
		String roomInfo,
		Double longitude, Double latitude) {
		this.storeNumber = storeNumber;
		this.storeName = storeName;
		this.branchName = branchName;
		this.largeCategoryCode = largeCategoryCode;
		this.largeCategoryName = largeCategoryName;
		this.mediumCategoryCode = mediumCategoryCode;
		this.mediumCategoryName = mediumCategoryName;
		this.smallCategoryCode = smallCategoryCode;
		this.smallCategoryName = smallCategoryName;
		this.standardIndustryCode = standardIndustryCode;
		this.standardIndustryName = standardIndustryName;
		this.cityCode = cityCode;
		this.cityName = cityName;
		this.districtCode = districtCode;
		this.districtName = districtName;
		this.adminDongCode = adminDongCode;
		this.adminDongName = adminDongName;
		this.legalDongCode = legalDongCode;
		this.legalDongName = legalDongName;
		this.lotCode = lotCode;
		this.landDivisionCode = landDivisionCode;
		this.landDivisionName = landDivisionName;
		this.lotMainNumber = lotMainNumber;
		this.lotSubNumber = lotSubNumber;
		this.lotAddress = lotAddress;
		this.roadNameCode = roadNameCode;
		this.roadName = roadName;
		this.buildingMainNumber = buildingMainNumber;
		this.buildingSubNumber = buildingSubNumber;
		this.buildingManagementNumber = buildingManagementNumber;
		this.buildingName = buildingName;
		this.roadNameAddress = roadNameAddress;
		this.oldZipCode = oldZipCode;
		this.newZipCode = newZipCode;
		this.dongInfo = dongInfo;
		this.floorInfo = floorInfo;
		this.roomInfo = roomInfo;
		this.longitude = longitude;
		this.latitude = latitude;
	}

}
