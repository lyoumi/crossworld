package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.MonsterDao;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.repositories.MonsterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class MonsterRepositoryImpl implements MonsterRepository {

    private final MonsterDao monsterDao;
    private final ConverterService converterService;

    @Override
    public Mono<Monster> saveMonster(Monster monster) {
        return monsterDao
                .save(converterService.convert(monster))
                .map(converterService::convert);
    }

    @Override
    public Mono<Monster> getMonsterById(String id) {
        return monsterDao
                .findById(id)
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteMonster(String id) {
        return monsterDao.deleteById(id);
    }
}
