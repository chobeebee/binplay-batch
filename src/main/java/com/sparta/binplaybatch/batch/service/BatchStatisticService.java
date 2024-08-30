/*
package com.sparta.binplaybatch.batch.service;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.entity.VideoAd;
import com.sparta.binplaybatch.entity.Videos;
import com.sparta.binplaybatch.repository.AdViewRepository;
import com.sparta.binplaybatch.repository.StatisticAdRepository;
import com.sparta.binplaybatch.repository.StatisticVideoRepository;
import com.sparta.binplaybatch.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchStatisticService {

    private final StatisticVideoRepository statisticVideoRepository;
    private final StatisticAdRepository statisticAdRepository;
    private final AdViewRepository adViewRepository;
    private final StreamRepository streamRepository;

    //1일 비디오 통계
    @Transactional
    public void updateDailyViewVideo(LocalDate date) {
        //LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Object[]> results = streamRepository.countViewsAndPlayTime(startOfDay, endOfDay);

        for (Object[] result : results) {
            Videos video = (Videos) result[0];
            Long dailyViewCount = (Long) result[1];
            Long dailyPlayTimeLong = (Long) result[2];
            int dailyPlayTime = dailyPlayTimeLong.intValue();

            StatisticVideo statisticVideo = statisticVideoRepository.findByVideoAndCreatedAt(video, date)
                    .orElseGet(() -> StatisticVideo.builder()
                            .video(video)
                            .createdAt(date)
                            .dailyViewCount(0)
                            .dailyPlayTime(0)
                            .build());

            statisticVideo.updateDailyViewAndPlayTime(dailyViewCount, dailyPlayTime);
            statisticVideoRepository.save(statisticVideo);
        }
    }

    //1일 광고 통계
    public void updateDailyViewAd(LocalDate date) {
        //LocalDate today = LocalDate.now();
        List<Object[]> results = adViewRepository.countViews(date);

        for (Object[] result : results) {
            VideoAd videoAd = (VideoAd) result[0];
            //Long videoAd = (Long) result[0];
            Long dailyViewCount = (Long) result[1];

            StatisticAd statisticAd = statisticAdRepository.findByVideoAdAndCreatedAt(videoAd, date)
                    .orElseGet(() -> StatisticAd.builder()
                            .createdAt(date)
                            .videoAd(videoAd)
                            .dailyViewCount(0)
                            .build());

            statisticAd.updateDailyViewCount(dailyViewCount);
            statisticAdRepository.save(statisticAd);
        }
    }
}
*/
