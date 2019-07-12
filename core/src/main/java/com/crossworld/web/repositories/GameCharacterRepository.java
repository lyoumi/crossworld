package com.crossworld.web.repositories;

import com.crossworld.web.data.character.GameCharacter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameCharacterRepository {
    Flux<GameCharacter> getAllGameCharacters();

    Mono<GameCharacter> save(GameCharacter gameCharacter);

    Mono<GameCharacter> getUsersCharacter(String userId);

    Mono<Void> deleteAll();
}
