package com.cwd.tg.gps.processors;

import com.cwd.tg.gps.data.character.GameCharacter;

public interface BaseEventProcessor {

    void processCharacterEvents(GameCharacter gameCharacter);
}
