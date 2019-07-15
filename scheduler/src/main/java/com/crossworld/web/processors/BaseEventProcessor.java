package com.crossworld.web.processors;

import com.crossworld.web.data.character.GameCharacter;

public interface BaseEventProcessor {

    void processCharacterEvents(GameCharacter gameCharacter);
}
