package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.AwardsDao;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.repositories.AwardsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AwardsRepositoryImpl implements AwardsRepository {

    private final AwardsDao awardsDao;
    private final ConverterService converterService;

    @Override
    public Mono<Awards> saveAwards(Awards awards) {
        return awardsDao
                .save(converterService.convert(awards))
                .map(converterService::convert);
    }

    @Override
    public Mono<Awards> getAwardsById(String id) {
        return awardsDao
                .findById(id)
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteAwards(String id) {
        return awardsDao.deleteById(id);
    }
}
