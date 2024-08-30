package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.entity.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentVideoRepository extends JpaRepository<PaymentVideo, Long> {
    //비디오, 생성날짜로 찾기
    Optional<PaymentVideo> findByVideoAndCreatedAt(Videos video, LocalDate date);
}