package com.rpg.web.crossworld.services.impl;

import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.repositories.GameCharacterRepository;
import com.rpg.web.crossworld.services.GameCharacterService;
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
