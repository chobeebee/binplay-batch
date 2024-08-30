package com.sparta.binplaybatch.batch.processor;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.entity.VideoAd;
import com.sparta.binplaybatch.repository.StatisticAdRepository;
import com.sparta.binplaybatch.repository.VideoAdRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatAdProcessor implements ItemProcessor<VideoAd, StatisticAd> {

    private final VideoAdRepository videoAdRepository;
    private final StatisticAdRepository statisticAdRepository;

    @Override
    @Transactional
    public StatisticAd process(VideoAd videoAd) {
        Long dailyViewCount = videoAdRepository.findDailyViewCountByAdId(videoAd.getAd().getAdId());

        // 비디오 광고 엔티티 병합
        final VideoAd mergedVideoAd = videoAdRepository.findById(videoAd.getVideoAdId())
                .orElseThrow(() -> new IllegalStateException("VideoAd not found"));

        StatisticAd statisticAd = statisticAdRepository.findByVideoAdAndCreatedAt(mergedVideoAd, LocalDate.now())
                .orElseGet(() -> StatisticAd.builder()
                        .createdAt(LocalDate.now())
                        .videoAd(mergedVideoAd)
                        .dailyViewCount(0L)
                        .build());

        statisticAd.updateDailyViewCount(dailyViewCount);
        return statisticAd;
    }
}
