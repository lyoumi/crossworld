package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.GameEventDao;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.repositories.GameEventRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public GameEvent getCharacterEvent(String characterId) {
        return Optional.ofNullable(gameEventDao.getEventEntityByGameCharacterId(characterId))
                .map(converterService::convert)
                .orElse(null);
    }

    @Override
    public GameEvent saveGameEvent(GameEvent gameEvent) {
        return converterService.convert(gameEventDao.save(converterService.convert(gameEvent)));
    }
}
