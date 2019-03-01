package com.crossworld.web.repositories;

import com.crossworld.web.data.GameCharacter;

import java.util.List;

public interface GameCharacterRepository {
    List<GameCharacter> getAllGameCharacters();

    GameCharacter save(GameCharacter gameCharacter);
}
