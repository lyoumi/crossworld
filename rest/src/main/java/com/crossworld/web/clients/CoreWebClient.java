package com.crossworld.web.clients;

import com.crossworld.web.data.GameCharacter;

import reactor.core.publisher.Mono;

public interface CoreWebClient {

    Mono<GameCharacter> getUserCharacter(String userId);

    Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter);
}
