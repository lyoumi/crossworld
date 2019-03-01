package com.rpg.web.crossworld.repositories.impl;

import com.rpg.web.crossworld.converters.ConverterService;
import com.rpg.web.crossworld.dao.GameEventDao;
import com.rpg.web.crossworld.data.GameEvent;
import com.rpg.web.crossworld.repositories.GameEventRepository;
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
