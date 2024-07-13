package com.sparta.binplaybatch.entity;

import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "videos")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Videos{
    @Id
    @Column(name = "video_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "views_count", nullable = false)
    private long viewCount;

    @Column(name = "video_length", nullable = false)
    private int videoLength;

    @OneToMany(mappedBy = "video")
    private List<Streams> streams = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    private List<VideoAd> videoAd = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    private List<StatisticVideo> statisticVideo;

    @OneToMany(mappedBy = "video")
    private List<PaymentVideo> paymentVideo = new ArrayList<>();

    public Videos(String title, String description, int videoLength) {
        this.title = title;
        this.description = description;
        this.videoLength = videoLength;
    }

    public void updateViewCount(long dailyViewCount) {
        this.viewCount += dailyViewCount;
    }

}
