package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.Streams;
import com.sparta.binplaybatch.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StreamRepository extends JpaRepository<Streams, Long> {
    // 지정된 Video와 날짜 범위에 해당하는 Streams 갯수
    @Query("SELECT COUNT(v) FROM Streams v WHERE v.video = :video AND v.createdAt >= :startOfDate AND v.createdAt < :endOfDate")
    long countVideoViewsExcludingUserAndDate(@Param("video") Videos video, @Param("startOfDate") LocalDateTime startDate, @Param("endOfDate") LocalDateTime endDate);

    // 지정된 Video와 날짜 범위에 해당하는 Streams의 총 재생 시간 합산
    @Query("SELECT SUM(v.playTime) FROM Streams v WHERE v.video = :video AND v.createdAt >= :startOfDate AND v.createdAt < :endOfDate")
    Long sumVideoPlayTimeExcludingUserAndDate(@Param("video") Videos video, @Param("startOfDate") LocalDateTime startDate, @Param("endOfDate") LocalDateTime endDate);
}
