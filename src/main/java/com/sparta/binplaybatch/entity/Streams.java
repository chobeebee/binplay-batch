package com.sparta.binplaybatch.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "streams")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Streams {
    @Id
    @Column(name = "stream_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long streamId;

    @Column(name = "play_time")
    private int playTime;

    @Column(name = "paused_at")
    private int pausedAt;

    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Videos video;

}
