package com.sparta.binplaybatch.entity;

import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Table(name="video_ad")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class VideoAd {
    @Id
    @Column(name = "video_ad_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoAdId;

    @Column(name="view_count", nullable = false)
    private long viewCount;

    @Column(name="stat_is")
    private boolean statIs;

    @OneToMany(mappedBy = "videoAd")
    private List<AdViews> adView;

    @OneToMany(mappedBy = "videoAd")
    private List<StatisticAd> statisticAd;

    @OneToMany(mappedBy = "videoAd")
    private List<PaymentAd> paymentAd;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private Videos video;

    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ads ad;


    public VideoAd(Ads ad, Videos video, int viewCount, boolean statIs) {
        this.ad = ad;
        this.video = video;
        this.viewCount = viewCount;
        this.statIs = statIs;
    }

    public void updateViewCount(long dailyViewCount) {
        this.viewCount += dailyViewCount;
    }
}
