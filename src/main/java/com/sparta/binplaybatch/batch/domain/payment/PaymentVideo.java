package com.sparta.binplaybatch.batch.domain.payment;

import com.sparta.binplaybatch.entity.Videos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Getter
@Table(name="payment_video")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(PaymentVideoId.class)
public class PaymentVideo {

    @Column(name="total_amount", nullable = false)
    private Double totalAmount;

    @Id
    @CreatedDate
    @Column(name="created_at", updatable = false) //업데이트를 막음
    private LocalDate createdAt;

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Videos video;

    public void updateTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
