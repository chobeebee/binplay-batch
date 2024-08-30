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

}
