package com.cwd.tg.gss.handlers.impl;

import com.cwd.tg.gss.data.character.GameCharacter;
import com.cwd.tg.gss.errors.exceptions.CharacterNotFoundException;
import com.cwd.tg.gss.handlers.GameCharacterRequestHandler;
import com.cwd.tg.gss.repositories.GameCharacterRepository;

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
        return gameCharacterRepository.getUsersCharacter(userId)
                .switchIfEmpty(Mono.error(
                        new CharacterNotFoundException(
                                String.format("Character with user id %s not found", userId))));
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
