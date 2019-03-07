package com.crossworld.web.events;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;

public interface EventProcessor {
    void processEvent(GameCharacter gameCharacter, GameEvent gameEvent);
}
