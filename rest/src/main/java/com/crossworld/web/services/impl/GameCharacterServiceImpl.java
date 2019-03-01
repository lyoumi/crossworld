package com.crossworld.web.services.impl;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.repositories.GameCharacterRepository;
import com.crossworld.web.services.GameCharacterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameCharacterServiceImpl implements GameCharacterService {

    private final GameCharacterRepository gameCharacterRepository;

    public GameCharacterServiceImpl(GameCharacterRepository gameCharacterRepository) {
        this.gameCharacterRepository = gameCharacterRepository;
    }

    @Override
    public List<GameCharacter> getAllGameCharacters() {
        return gameCharacterRepository.getAllGameCharacters();
    }
}
