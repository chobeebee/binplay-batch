package com.sparta.binplaybatch.batch.domain.payment;

import com.sparta.binplaybatch.entity.VideoAd;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Getter
@Table(name="payment_ad")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(PaymentAdId.class)
public class PaymentAd {

    @Column(name="total_amount", nullable = false)
    private Double totalAmount;

    @Id
    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDate createdAt;

    @Id
    @ManyToOne
    @JoinColumn(name = "video_ad_id", nullable = false)
    private VideoAd videoAd;

    public void updateTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
