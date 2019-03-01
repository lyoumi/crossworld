package com.rpg.web.crossworld.repositories;

import com.rpg.web.crossworld.data.GameCharacter;

import java.util.List;

public interface GameCharacterRepository {
    List<GameCharacter> getAllGameCharacters();

    GameCharacter save(GameCharacter gameCharacter);
}
