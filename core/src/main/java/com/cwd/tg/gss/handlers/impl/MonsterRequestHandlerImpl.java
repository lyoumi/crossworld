package com.cwd.tg.gss.handlers.impl;

import static java.lang.String.format;

import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.errors.exceptions.MonsterNotFoundException;
import com.cwd.tg.gss.handlers.MonsterRequestHandler;
import com.cwd.tg.gss.repositories.MonsterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class MonsterRequestHandlerImpl implements MonsterRequestHandler {

    private final MonsterRepository monsterRepository;

    @Override
    public Mono<Monster> getMonster(String id) {
        return monsterRepository.getMonsterById(id)
                .switchIfEmpty(Mono.error(new MonsterNotFoundException(format("Monster with id %s not found", id))));
    }

    @Override
    public Mono<Monster> createMonster(Monster monster) {
        return monsterRepository.saveMonster(monster);
    }

    @Override
    public Mono<Monster> updateMonster(Monster monster) {
        return monsterRepository.saveMonster(monster);
    }

    @Override
    public Mono<Void> deleteMonster(String id) {
        return monsterRepository.deleteMonster(id);
    }

}
