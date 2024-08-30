package com.sparta.binplaybatch.batch.writer;

import com.sparta.binplaybatch.batch.domain.payment.PaymentAd;
import com.sparta.binplaybatch.repository.PaymentAdRepository;
import com.sparta.binplaybatch.repository.VideoAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdPaymentWriter implements ItemWriter<PaymentAd> {

    private final PaymentAdRepository paymentAdRepository;
    private final VideoAdRepository videoAdRepository;

    @Override
    public void write(Chunk<? extends PaymentAd> paymentAds) {
        for (PaymentAd paymentAd : paymentAds) {
            paymentAdRepository.save(paymentAd);
            videoAdRepository.save(paymentAd.getVideoAd());
        }
    }
}
