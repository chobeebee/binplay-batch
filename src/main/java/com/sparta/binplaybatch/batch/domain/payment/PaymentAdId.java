package com.sparta.binplaybatch.batch.domain.payment;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@EqualsAndHashCode
public class PaymentAdId  implements Serializable {
    private LocalDate createdAt;
    private Long videoAd;
}
