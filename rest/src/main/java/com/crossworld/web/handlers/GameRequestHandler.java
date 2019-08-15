package com.crossworld.web.handlers;

import com.crossworld.web.data.input.PlayerCharacterInput;
import com.crossworld.web.data.internal.character.GameCharacter;
import reactor.core.publisher.Mono;

public interface GameRequestHandler {

    Mono<GameCharacter> createGameCharacter(PlayerCharacterInput playerCharacterInput);

    Mono<GameCharacter> getUserGameCharacter(String userId);
}
