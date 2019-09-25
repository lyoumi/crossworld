package com.cwd.tg.gss.repositories.impl;

import com.cwd.tg.gss.converters.ConverterService;
import com.cwd.tg.gss.dao.MonsterDao;
import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.repositories.MonsterRepository;
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
