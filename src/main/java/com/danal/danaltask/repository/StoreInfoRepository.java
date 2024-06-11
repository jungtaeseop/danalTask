package com.danal.danaltask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.danal.danaltask.domain.StoreInfo;

public interface StoreInfoRepository extends JpaRepository<StoreInfo, Long> {
}
