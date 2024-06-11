package com.danal.danaltask.config.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.danal.danaltask.data.StoreInfoDto;
import com.danal.danaltask.domain.StoreInfo;


public class StoreInfoProcessor implements ItemProcessor<StoreInfoDto, StoreInfo> {
	@Override
	public StoreInfo process(StoreInfoDto storeInfoDto) throws Exception {
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
	}
}
