package com.crossworld.web.repositories;

import com.crossworld.web.data.GameEvent;
import reactor.core.publisher.Mono;

public interface GameEventRepository {

    Mono<GameEvent> getCharacterEvent(String characterId);
    Mono<GameEvent> saveGameEvent(GameEvent gameEvent);

    Mono<Void> deleteAll();
}
