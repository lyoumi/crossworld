package com.crossworld.web.client;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CoreWebClient {

    Flux<GameCharacter> getAllGameCharacters();
    Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter);
    Mono<GameEvent> getGameEventByCharacterId(String id);
}
