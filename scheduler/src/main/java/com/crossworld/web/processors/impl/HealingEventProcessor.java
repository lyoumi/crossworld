package com.crossworld.web.processors.impl;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.processors.EventProcessor;

import org.springframework.stereotype.Component;

@Component("healingEventProcessor")
public class HealingEventProcessor implements EventProcessor {
    @Override
    public void processEvent(GameCharacter gameCharacter, GameEvent gameEvent) {

    }
}
