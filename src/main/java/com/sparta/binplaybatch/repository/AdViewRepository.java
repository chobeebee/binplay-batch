package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.AdViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AdViewRepository extends JpaRepository<AdViews, Long> {
    @Query("SELECT av.videoAd, COUNT(av) FROM AdViews av WHERE av.createdAt = :date GROUP BY av.videoAd")
    List<Object[]> countViews(@Param("date") LocalDate date);
}
