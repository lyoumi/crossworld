package com.crossworld.web.handlers;

import com.crossworld.web.data.events.battle.Monster;
import reactor.core.publisher.Mono;

public interface MonsterRequestHandler {

    Mono<Monster> getMonster(String id);

    Mono<Monster> createMonster(Monster monster);

    Mono<Monster> updateMonster(Monster monster);

    Mono<Void> deleteMonster(String id);
}
