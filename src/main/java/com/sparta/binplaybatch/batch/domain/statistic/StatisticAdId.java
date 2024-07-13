package com.sparta.binplaybatch.batch.domain.statistic;

import com.sparta.binplaybatch.entity.VideoAd;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@EqualsAndHashCode
public class StatisticAdId implements Serializable {
    private LocalDate createdAt;
    private VideoAd videoAd;

    public StatisticAdId(LocalDate createdAt, VideoAd videoAd) {
        this.createdAt = createdAt;
        this.videoAd = videoAd;
    }
}
