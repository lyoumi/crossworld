package com.crossworld.web.services;

import com.crossworld.web.data.internal.character.GameCharacter;

import reactor.core.publisher.Mono;

public interface GameControllerService {
    Mono<GameCharacter> saveCharacter(GameCharacter gameCharacter);

    Mono<GameCharacter> getUserCharacter(String userId);
}
