package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.entity.Streams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StreamRepository extends JpaRepository<Streams, Long> {
    //Optional<Streams> findByUserAndVideo(Users user, Videos video);
    //Optional<Streams> findByUserIdAndVideoId(Long userId, Long videoId);

    @Query("SELECT av.video, COUNT(av), SUM(av.playTime) FROM Streams av WHERE av.createdAt >= :startOfDay AND av.createdAt < :endOfDay GROUP BY av.video")
    List<Object[]> countViewsAndPlayTime(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}
