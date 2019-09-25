package com.cwd.tg.gss.repositories;

import com.cwd.tg.gss.data.character.GameCharacter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameCharacterRepository {
    Flux<GameCharacter> getAllGameCharacters();

    Mono<GameCharacter> save(GameCharacter gameCharacter);

    Mono<GameCharacter> getUsersCharacter(String userId);

    Mono<Void> deleteAll();
}
