package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.GameCharacterDao;
import com.crossworld.web.repositories.GameCharacterRepository;
import com.crossworld.web.data.GameCharacter;
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
