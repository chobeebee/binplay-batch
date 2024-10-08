package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticAdId;
import com.sparta.binplaybatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticAdRepository extends JpaRepository<StatisticAd, StatisticAdId> {
    //비디오 광고, 생성 날짜로 찾기
    Optional<StatisticAd> findByVideoAdAndCreatedAt(VideoAd videoAd, LocalDate createdAt);
}
