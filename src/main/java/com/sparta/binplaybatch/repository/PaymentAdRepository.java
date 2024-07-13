package com.sparta.binplaybatch.repository;

import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.entity.VideoAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentAdRepository extends JpaRepository<PaymentAd, Long> {

    //List<PaymentVideo> findAllByCreatedAt(LocalDate date);

    Optional<PaymentAd> findByVideoAdAndCreatedAt(VideoAd videoAd, LocalDate date);

    @Query("SELECT p FROM PaymentAd p WHERE p.createdAt = :date")
    List<PaymentAd> findAllByCreatedAt(@Param("date") LocalDate date);
}
