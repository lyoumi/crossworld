package com.crossworld.web.processors;

import com.crossworld.web.data.character.GameCharacter;

public interface EventProcessor {
    void processEvent(GameCharacter gameCharacter);
}
