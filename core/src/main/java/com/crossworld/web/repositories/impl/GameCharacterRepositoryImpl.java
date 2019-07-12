package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.GameCharacterDao;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class GameCharacterRepositoryImpl implements GameCharacterRepository {

    private final ConverterService converterService;
    private final GameCharacterDao gameCharacterDao;

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        return gameCharacterDao.findAll()
                .map(converterService::convert);
    }

    @Override
    public Mono<GameCharacter> save(GameCharacter gameCharacter) {
        return gameCharacterDao
                .save(converterService.convert(gameCharacter))
                .map(converterService::convert);
    }

    @Override
    public Mono<GameCharacter> getUsersCharacter(String userId) {
        return gameCharacterDao
                .findByUserId(userId)
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteAll() {
        return gameCharacterDao.deleteAll();
    }
}
