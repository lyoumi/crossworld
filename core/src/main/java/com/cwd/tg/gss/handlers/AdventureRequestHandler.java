package com.cwd.tg.gss.handlers;

import com.cwd.tg.gss.data.events.adventure.Adventure;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdventureRequestHandler {

    Mono<Adventure> getAdventureById(String id);

    Flux<Adventure> getAdventureByCharacterId(String characterId);

    Mono<Adventure> getActiveAdventureByCharacterId(String characterId);

    Mono<Adventure> createAdventure(@RequestBody Adventure adventure);

    Mono<Adventure> updateAdventure(@RequestBody Adventure adventure);

    Mono<Void> deleteAdventure(String id);

    Mono<Void> deleteAdventureByCharacterId(String characterId);
}
