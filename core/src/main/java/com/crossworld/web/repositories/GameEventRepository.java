package com.crossworld.web.repositories;

import com.crossworld.web.data.GameEvent;

public interface GameEventRepository {

    GameEvent getCharacterEvent(String characterId);
    GameEvent saveGameEvent(GameEvent gameEvent);
}
