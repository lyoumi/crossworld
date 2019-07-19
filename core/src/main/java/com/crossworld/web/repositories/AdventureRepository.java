package com.crossworld.web.repositories;

import com.crossworld.web.data.events.adventure.Adventure;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdventureRepository {

    Mono<Adventure> saveAdventure(Adventure adventure);

    Mono<Adventure> getAdventureById(String id);

    Flux<Adventure> getAdventureByCharacterId(String characterId);

    Mono<Adventure> getActiveAdventureByCharacterId(String characterId);

    Mono<Void> deleteAdventure(String id);

    Mono<Void> deleteAdventureByCharacterId(String characterId);
}
