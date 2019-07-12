package com.crossworld.web.repositories;

import com.crossworld.web.data.events.battle.Monster;
import reactor.core.publisher.Mono;

public interface MonsterRepository {

    Mono<Monster> saveMonster(Monster monster);

    Mono<Monster> getMonsterById(String id);

    Mono<Void> deleteMonster(String id);
}
