package com.sparta.binplaybatch.batch.writer;

import com.sparta.binplaybatch.batch.domain.statistic.StatisticVideo;
import com.sparta.binplaybatch.repository.StatisticVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatVideoWriter implements ItemWriter<StatisticVideo> {

    private final StatisticVideoRepository statisticVideoRepository;

    @Override
    public void write(Chunk<? extends StatisticVideo> items) {
        statisticVideoRepository.saveAll(items);
    }
}
