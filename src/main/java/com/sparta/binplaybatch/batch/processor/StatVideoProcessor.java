package com.sparta.binplaybatch.batch.processor;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.entity.Videos;
import com.sparta.binplaybatch.repository.StatisticVideoRepository;
import com.sparta.binplaybatch.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StatVideoProcessor implements ItemProcessor<Videos, StatisticVideo> {

    private final StatisticVideoRepository statisticVideoRepository;
    private final StreamRepository streamRepository;

    @Override
    public StatisticVideo process(Videos video) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1); // 2일 전 시작
        LocalDateTime endDate = LocalDateTime.now();   // 1일 전 끝

        // 조회수 계산
        Long dailyViewCount = streamRepository.countVideoViewsExcludingUserAndDate(video, startDate, endDate);

        // playTime 합산 계산
        Long dailyPlayTime = streamRepository.sumVideoPlayTimeExcludingUserAndDate(video, startDate, endDate);
        if (dailyPlayTime == null) {
            dailyPlayTime = 0L;
        }

        StatisticVideo statisticVideo = new StatisticVideo();
        statisticVideo.setVideo(video);
        statisticVideo.setDailyViewCount(dailyViewCount);
        statisticVideo.setDailyPlayTime(dailyPlayTime.intValue()); // int로 변환
        statisticVideo.setCreatedAt(LocalDate.now()); // 데이터 생성일을 1일 전으로 설정

        // 저장
        statisticVideoRepository.save(statisticVideo);

        return statisticVideo;
    }
}
