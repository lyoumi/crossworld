package com.crossworld.web.events;

import com.crossworld.web.data.GameCharacter;

public interface EventProcessor {
    void processEvent(GameCharacter gameCharacter);
}
