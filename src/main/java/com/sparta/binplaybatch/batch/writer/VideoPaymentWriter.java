package com.sparta.binplaybatch.batch.writer;

import com.sparta.binplaybatch.batch.domain.payment.PaymentVideo;
import com.sparta.binplaybatch.repository.PaymentVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoPaymentWriter implements ItemWriter<PaymentVideo> {

    private final PaymentVideoRepository paymentVideoRepository;

    @Override
    public void write(Chunk<? extends PaymentVideo> paymentVideos) throws Exception {
        paymentVideoRepository.saveAll(paymentVideos);
    }
}
