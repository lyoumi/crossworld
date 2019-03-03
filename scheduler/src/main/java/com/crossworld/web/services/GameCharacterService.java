package com.crossworld.web.services;

import com.crossworld.web.data.GameCharacter;
import reactor.core.publisher.Flux;

public interface GameCharacterService {
    Flux<GameCharacter> getAllGameCharacters();
}
