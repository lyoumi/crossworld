package com.crossworld.web.handlers.impl;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.handlers.GameCharacterRequestHandler;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameCharacterRequestHandlerImpl implements GameCharacterRequestHandler {

    private final GameCharacterRepository gameCharacterRepository;

    @Override
    public Mono<GameCharacter> getUsersGameCharacter(String userId) {
        return gameCharacterRepository.getUsersCharacter(userId);
    }

    @Override
    public Mono<GameCharacter> createCharacter(GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter);

    }

    @Override
    public Mono<GameCharacter> updateCharacter(GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter);

    }

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        return gameCharacterRepository.getAllGameCharacters();
    }
}
