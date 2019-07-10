package com.crossworld.web.services;

import com.crossworld.web.data.character.GameCharacter;

import reactor.core.publisher.Flux;

public interface GameCharacterService {
    Flux<GameCharacter> getAllGameCharacters();
}
