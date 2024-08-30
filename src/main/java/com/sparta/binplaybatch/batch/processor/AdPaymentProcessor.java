package com.sparta.binplaybatch.batch.processor;

import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.entity.VideoAd;
import com.sparta.binplaybatch.repository.PaymentAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdPaymentProcessor implements ItemProcessor<StatisticAd, PaymentAd> {

    private final PaymentAdRepository paymentAdRepository;
    private final LocalDate today = LocalDate.now();

    @Override
    public PaymentAd process(StatisticAd stat) {
        VideoAd videoAd = stat.getVideoAd();
        long totalViewCount = stat.getVideoAd().getViewCount() + stat.getDailyViewCount();
        double totalAmount = Math.floor(calculateAdAmount(totalViewCount));

        PaymentAd paymentAd = paymentAdRepository.findByVideoAdAndCreatedAt(stat.getVideoAd(), today)
                .orElseGet(() -> PaymentAd.builder()
                        .videoAd(stat.getVideoAd())
                        .createdAt(today)
                        .totalAmount(0.0)
                        .build());

        paymentAd.updateTotalAmount(totalAmount);

        // 광고 총 조회수 업데이트
        videoAd.updateViewCount(totalViewCount);

        return paymentAd;
    }

    // 광고 단가
    private double calculateAdAmount(long viewCount) {
        if (viewCount < 100000) return viewCount * 10;
        else if (viewCount < 500000) return viewCount * 12;
        else if (viewCount < 1000000) return viewCount * 15;
        else return viewCount * 20;
    }
}
