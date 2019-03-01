package com.crossworld.web.listners;

import com.crossworld.web.data.GameCharacter;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncGameCharacterProcessor {

    @Async
    @EventListener
    public void processCharacterEvents(GameCharacter gameCharacter) {
        if (!gameCharacter.isHasEvent()) {
            createCharacterEvent(gameCharacter.getId());
        }
        progressCharacterEvent(gameCharacter);
    }

    private void createCharacterEvent(String id) {

    }

    private void progressCharacterEvent(GameCharacter gameCharacter) {

    }
}
