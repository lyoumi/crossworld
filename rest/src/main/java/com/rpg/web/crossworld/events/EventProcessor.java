package com.rpg.web.crossworld.events;

import com.rpg.web.crossworld.data.GameCharacter;

public interface EventProcessor {
    void processEvent(GameCharacter gameCharacter);
}
