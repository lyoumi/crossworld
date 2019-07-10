package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.GameEventDao;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.repositories.GameEventRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GameEventRepositoryImpl implements GameEventRepository {

    private final ConverterService converterService;
    private final GameEventDao gameEventDao;

    public GameEventRepositoryImpl(ConverterService converterService,
                                   GameEventDao gameEventDao) {
        this.converterService = converterService;
        this.gameEventDao = gameEventDao;
    }

    @Override
    public Mono<GameEvent> getCharacterEvent(String characterId) {
        return gameEventDao.getEventEntityByGameCharacterId(characterId).map(converterService::convert);
    }

    @Override
    public Mono<GameEvent> saveGameEvent(GameEvent gameEvent) {
        return gameEventDao.save(converterService.convert(gameEvent))
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteAll() {
        return gameEventDao.deleteAll();
    }
}
