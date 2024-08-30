package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoAdRepository extends JpaRepository<VideoAd, Long> {
    // 특정 광고의 총 조회수 합산
    @Query("SELECT SUM(va.viewCount) FROM VideoAd va WHERE va.ad.adId = :adId")
    Long findDailyViewCountByAdId(@Param("adId") Long adId);
}
