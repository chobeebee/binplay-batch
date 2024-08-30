package com.sparta.binplaybatch.batch.processor;

import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.entity.Videos;
import com.sparta.binplaybatch.repository.PaymentVideoRepository;
import com.sparta.binplaybatch.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class VideoPaymentProcessor implements ItemProcessor<StatisticVideo, PaymentVideo> {

    private final PaymentVideoRepository paymentVideoRepository;
    private final VideoRepository videoRepository;
    private final LocalDate today = LocalDate.now();

    @Override
    public PaymentVideo process(StatisticVideo stat) throws Exception {
        Videos video = stat.getVideo();
        long totalViewCount = video.getViewCount() + stat.getDailyViewCount();
        double totalAmount = Math.floor(calculateVideoAmount(totalViewCount));

        PaymentVideo paymentVideo = paymentVideoRepository.findByVideoAndCreatedAt(stat.getVideo(), today)
                .orElseGet(() -> PaymentVideo.builder()
                        .video(stat.getVideo())
                        .createdAt(today)
                        .totalAmount(0.0)
                        .build());

        paymentVideo.updateTotalAmount(totalAmount);

        // 비디오 총 조회수 업데이트
        video.updateViewCount(totalViewCount);
        videoRepository.save(video);

        return paymentVideo;
    }

    // 비디오 단가
    private double calculateVideoAmount(long viewCount) {
        if (viewCount < 100000) return viewCount * 1;
        else if (viewCount < 500000) return viewCount * 1.1;
        else if (viewCount < 1000000) return viewCount * 1.3;
        else return viewCount * 1.5;
    }
}
