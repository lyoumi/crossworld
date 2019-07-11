package com.crossworld.web.processors.impl;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.processors.EventProcessor;
import org.springframework.stereotype.Component;

@Component("healingEventProcessor")
public class HealingEventProcessor implements EventProcessor {
    @Override
    public void processEvent(GameCharacter gameCharacter) {

    }
}
