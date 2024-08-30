package com.sparta.binplaybatch.batch.writer;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticAd;
import com.sparta.binplaybatch.repository.StatisticAdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatAdWriter implements ItemWriter<StatisticAd> {

    private final StatisticAdRepository statisticAdRepository;

    @Override
    public void write(Chunk<? extends StatisticAd> items) {
        statisticAdRepository.saveAll(items);
    }
}
