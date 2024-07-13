package com.sparta.binplaybatch.batch.service;

import com.sparta.binplaybatch.batch.domain.payment.DailyTotalAmount;
import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.entity.VideoAd;
import com.sparta.binplaybatch.entity.Videos;
import com.sparta.binplaybatch.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchPaymentService {

    private final StatisticVideoRepository statisticVideoRepository;
    private final StatisticAdRepository statisticAdRepository;
    private final PaymentVideoRepository paymentVideoRepository;
    private final PaymentAdRepository paymentAdRepository;
    private final VideoRepository videoRepository;
    private final VideoAdRepository videoAdRepository;

    // 1일 비디오 정산
    public void calculateVideoPmt(LocalDate date) {

        List<StatisticVideo> stats = statisticVideoRepository.findByCreatedAt(date);

        for (StatisticVideo stat : stats) {

            Videos video = stat.getVideo();
            long totalViewCount = video.getViewCount() + stat.getDailyViewCount();
            double totalAmount = Math.floor(calculateVideoAmount(totalViewCount));

            PaymentVideo paymentVideo = paymentVideoRepository.findByVideoAndCreatedAt(stat.getVideo(), date)
                    .orElseGet(() -> PaymentVideo.builder()
                            .video(stat.getVideo())
                            .createdAt(date)
                            .totalAmount(0.0)
                            .build());

            paymentVideo.updateTotalAmount(totalAmount);
            paymentVideoRepository.save(paymentVideo);

            // 비디오 총 조회수 업데이트
            video.updateViewCount(totalViewCount);
            videoRepository.save(video);
        }
    }

    // 1일 광고 정산
    public void calculateAdPmt(LocalDate date) {

        List<StatisticAd> stats = statisticAdRepository.findByCreatedAt(date);

        for (StatisticAd stat : stats) {

            VideoAd videoAd = stat.getVideoAd();
            long totalViewCount = stat.getVideoAd().getViewCount() + stat.getDailyViewCount();
            double totalAmount = Math.floor(calculateAdAmount(totalViewCount));

            PaymentAd paymentAd = paymentAdRepository.findByVideoAdAndCreatedAt(stat.getVideoAd(), date)
                    .orElseGet(() -> PaymentAd.builder()
                            .videoAd(stat.getVideoAd())
                            .createdAt(date)
                            .totalAmount(0.0)
                            .build());

            paymentAd.updateTotalAmount(totalAmount);
            paymentAdRepository.save(paymentAd);

            // 광고 총 조회수 업데이트
            videoAd.updateViewCount(totalViewCount);
            videoAdRepository.save(videoAd);
        }
    }

    // 총 금액, 광고 정산, 영상 정산
    public DailyTotalAmount getDailyTotalPayment(LocalDate date) {
        double totalVideoAmount = paymentVideoRepository.findAllByCreatedAt(date).stream()
                .mapToDouble(payment -> Math.floor(payment.getTotalAmount()))
                .sum();

        double totalAdAmount = paymentAdRepository.findAllByCreatedAt(date).stream()
                .mapToDouble(payment -> Math.floor(payment.getTotalAmount()))
                .sum();

        double totalAmount = totalVideoAmount + totalAdAmount;

        return DailyTotalAmount.builder()
                .totalVideoAmount(totalVideoAmount)
                .totalAdAmount(totalAdAmount)
                .totalAmount(totalAmount)
                .build();
    }

    // 비디오 단가
    private double calculateVideoAmount(long viewCount) {
        if (viewCount < 100000) return viewCount * 1;
        else if (viewCount < 500000) return viewCount * 1.1;
        else if (viewCount < 1000000) return viewCount * 1.3;
        else return viewCount * 1.5;
    }

    // 광고 단가
    private double calculateAdAmount(long viewCount) {
        if (viewCount < 100000) return viewCount * 10;
        else if (viewCount < 500000) return viewCount * 12;
        else if (viewCount < 1000000) return viewCount * 15;
        else return viewCount * 20;
    }
}
