package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.AdventureEventDetails;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.processors.EventProcessor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("adventureEventProcessor")
@AllArgsConstructor
public class AdventureEventProcessor implements EventProcessor {

    private final CoreWebClient coreWebClient;
    private final String FINISH_ADVENTURE_EVENT = "I'm done, man!";

    @Override
    public void processEvent(GameCharacter gameCharacter, GameEvent gameEvent) {
        processAdventure(gameCharacter, gameEvent);
    }

    private void processAdventure(GameCharacter gameCharacter, GameEvent gameEvent) {
        var eventDetails = (AdventureEventDetails) gameEvent.getEventDetails();
        var currentEventStep = eventDetails.getStep();
        if (currentEventStep < eventDetails.getAdventureEvents().size()) {
            gameEvent.setCurrentAction(eventDetails.getAdventureEvents().get(currentEventStep));
            eventDetails.setStep(++currentEventStep);
        } else {
            gameCharacter.setHasEvent(false);
            gameEvent.setCurrentAction(FINISH_ADVENTURE_EVENT);
            gameCharacter.getGameInventory()
                    .setExperience(gameCharacter.getGameInventory().getExperience() + eventDetails.getExperience());
            gameCharacter.getGameInventory()
                    .setGold(gameCharacter.getGameInventory().getGold() + eventDetails.getGold());
        }
        coreWebClient.saveGameCharacter(gameCharacter);
    }
}
