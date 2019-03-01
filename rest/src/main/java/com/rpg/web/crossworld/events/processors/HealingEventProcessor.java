package com.rpg.web.crossworld.events.processors;

import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.events.EventProcessor;
import org.springframework.stereotype.Component;

@Component("healingEventProcessor")
public class HealingEventProcessor implements EventProcessor {
    @Override
    public void processEvent(GameCharacter gameCharacter) {

    }
}
