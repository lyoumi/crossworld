package com.crossworld.web.events.processors;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.AdventureEventDetails;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.events.EventProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("adventureEventProcessor")
@AllArgsConstructor
public class AdventureEventProcessor implements EventProcessor {

    private final CoreWebClient coreWebClient;
    private final String FINISH_ADVENTURE_EVENT = "I'm done, man!";

    @Override
    public void processEvent(GameCharacter gameCharacter) {
        coreWebClient.getGameEventByCharacterId(gameCharacter.getId())
                .log()
                .doOnSuccess(gameEvent -> processAdventure(gameCharacter, gameEvent));
    }

    private void processAdventure(GameCharacter gameCharacter, GameEvent characterEvent) {
        var eventDetails = (AdventureEventDetails) characterEvent.getEventDetails();
        var currentEventStep = eventDetails.getStep();
        if (currentEventStep < eventDetails.getAdventureEvents().size()) {
            characterEvent.setCurrentAction(eventDetails.getAdventureEvents().get(currentEventStep));
            eventDetails.setStep(++currentEventStep);
        } else {
            gameCharacter.setHasEvent(false);
            characterEvent.setCurrentAction(FINISH_ADVENTURE_EVENT);
            gameCharacter.getGameInventory()
                    .setExperience(gameCharacter.getGameInventory().getExperience() + eventDetails.getExperience());
            gameCharacter.getGameInventory()
                    .setGold(gameCharacter.getGameInventory().getGold() + eventDetails.getGold());
        }
        coreWebClient.saveGameCharacter(gameCharacter);
    }
}
