package com.cwd.tg.gss.handlers;

import com.cwd.tg.gss.data.character.GameCharacter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameCharacterRequestHandler {

    Mono<GameCharacter> getUsersGameCharacter(String userId);

    Mono<GameCharacter> createCharacter(GameCharacter gameCharacter);

    Mono<GameCharacter> updateCharacter(GameCharacter gameCharacter);

    Flux<GameCharacter> getAllGameCharacters();
}
