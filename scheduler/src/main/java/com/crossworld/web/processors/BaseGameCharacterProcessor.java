package com.crossworld.web.processors;

import com.crossworld.web.data.character.GameCharacter;

public interface BaseGameCharacterProcessor {

    void processCharacterEvents(GameCharacter gameCharacter);
}
