package com.cwd.tg.gss.repositories;

import com.cwd.tg.gss.data.events.battle.Monster;
import reactor.core.publisher.Mono;

public interface MonsterRepository {

    Mono<Monster> saveMonster(Monster monster);

    Mono<Monster> getMonsterById(String id);

    Mono<Void> deleteMonster(String id);
}
