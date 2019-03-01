package com.rpg.web.crossworld.repositories.impl;

import com.rpg.web.crossworld.converters.ConverterService;
import com.rpg.web.crossworld.dao.GameCharacterDao;
import com.rpg.web.crossworld.repositories.GameCharacterRepository;
import com.rpg.web.crossworld.data.GameCharacter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameCharacterRepositoryImpl implements GameCharacterRepository {

    private final ConverterService converterService;
    private final GameCharacterDao gameCharacterDao;

    public GameCharacterRepositoryImpl(ConverterService converterService, GameCharacterDao gameCharacterDao) {
        this.converterService = converterService;
        this.gameCharacterDao = gameCharacterDao;
    }

    @Override
    public List<GameCharacter> getAllGameCharacters() {
        return gameCharacterDao.findAll()
                .stream()
                .map(converterService::convert)
                .collect(Collectors.toList());
    }

    @Override
    public GameCharacter save(GameCharacter gameCharacter) {
        return converterService.convert(gameCharacterDao.save(converterService.convert(gameCharacter)));
    }
}
