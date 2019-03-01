package com.rpg.web.crossworld.repositories;

import com.rpg.web.crossworld.data.GameEvent;

public interface GameEventRepository {

    GameEvent getCharacterEvent(String characterId);
    GameEvent saveGameEvent(GameEvent gameEvent);
}
