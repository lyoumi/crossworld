package com.rpg.web.crossworld.events.processors;

import com.rpg.web.crossworld.data.AdventureEventDetails;
import com.rpg.web.crossworld.data.GameCharacter;
import com.rpg.web.crossworld.data.GameEvent;
import com.rpg.web.crossworld.events.EventProcessor;
import com.rpg.web.crossworld.repositories.GameCharacterRepository;
import com.rpg.web.crossworld.repositories.GameEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("adventureEventProcessor")
@AllArgsConstructor
public class AdventureEventProcessor implements EventProcessor {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameEventRepository gameEventRepository;
    private final String FINISH_ADVENTURE_EVENT = "I'm done, man!";

    @Override
    public void processEvent(GameCharacter gameCharacter) {
        var characterEvent = gameEventRepository.getCharacterEvent(gameCharacter.getId());
        processAdventure(gameCharacter, characterEvent);
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
        gameCharacterRepository.save(gameCharacter);

    }
}
