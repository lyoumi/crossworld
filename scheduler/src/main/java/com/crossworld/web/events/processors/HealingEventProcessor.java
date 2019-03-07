package com.crossworld.web.events.processors;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.events.EventProcessor;
import org.springframework.stereotype.Component;

@Component("healingEventProcessor")
public class HealingEventProcessor implements EventProcessor {
    @Override
    public void processEvent(GameCharacter gameCharacter, GameEvent gameEvent) {

    }
}
