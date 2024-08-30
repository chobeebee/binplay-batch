package com.sparta.binplaybatch.batch.domain.statistic;

import com.sparta.binplaybatch.entity.Videos;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="statistic_video")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(StatisticVideoId.class)
public class StatisticVideo {

    @Id
    @CreatedDate
    @Column(name="created_at", updatable = false) //업데이트를 막음
    private LocalDate createdAt;

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Videos video;

    @Column(name="daily_view_count", nullable = false)
    private long dailyViewCount;

    @Column(name="daily_play_time", nullable = false)
    private int dailyPlayTime;

    public void updateDailyViewAndPlayTime(Long dailyViewCount, int dailyPlayTime) {
        this.dailyViewCount = dailyViewCount.intValue();
        this.dailyPlayTime = dailyPlayTime;
    }
}
