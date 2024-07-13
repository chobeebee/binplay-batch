package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideoId;
import com.sparta.binplaybatch.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatisticVideoRepository extends JpaRepository<StatisticVideo, StatisticVideoId> {
    List<StatisticVideo> findByVideoVideoId(Long videoId);
    Optional<StatisticVideo> findByVideoAndCreatedAt(Videos video, LocalDate createdAt);
    List<StatisticVideo> findByCreatedAt(LocalDate date);

    @Query("SELECT sv.video.videoId, v.title, SUM(sv.dailyViewCount) as totalViews " +
            "FROM StatisticVideo sv JOIN sv.video v WHERE sv.createdAt = :date " +
            "GROUP BY sv.video.videoId, v.title ORDER BY totalViews DESC")
    List<Object[]> findTop5VideosByViewsForDay(@Param("date") LocalDate date);

    @Query("SELECT sv.video.videoId, v.title, SUM(sv.dailyPlayTime) as totalPlayTime " +
            "FROM StatisticVideo sv JOIN sv.video v WHERE sv.createdAt = :date " +
            "GROUP BY sv.video.videoId, v.title ORDER BY totalPlayTime DESC")
    List<Object[]> findTop5VideosByPlayTimeForDay(@Param("date") LocalDate date);

    @Query("SELECT sv.video.videoId, v.title, SUM(sv.dailyViewCount) as totalViews " +
            "FROM StatisticVideo sv JOIN sv.video v WHERE sv.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY sv.video.videoId, v.title ORDER BY totalViews DESC")
    List<Object[]> findTop5VideosByViewsForPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT sv.video.videoId, v.title, SUM(sv.dailyPlayTime) as totalPlayTime " +
            "FROM StatisticVideo sv JOIN sv.video v WHERE sv.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY sv.video.videoId, v.title ORDER BY totalPlayTime DESC")
    List<Object[]> findTop5VideosByPlayTimeForPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
