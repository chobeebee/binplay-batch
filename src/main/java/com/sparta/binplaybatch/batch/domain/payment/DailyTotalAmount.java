package com.sparta.binplaybatch.batch.domain.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyTotalAmount {
    private Double totalVideoAmount;
    private Double totalAdAmount;
    private Double totalAmount;
}
