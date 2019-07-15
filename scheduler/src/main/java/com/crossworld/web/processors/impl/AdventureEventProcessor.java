package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.adventure.AdventureStatus;
import com.crossworld.web.processors.EventProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("adventureEventProcessor")
@AllArgsConstructor
public class AdventureEventProcessor implements EventProcessor {

    private final CoreWebClient coreWebClient;
    private final String FINISH_ADVENTURE_EVENT = "I'm done, man!";

    @Override
    public void processEvent(GameCharacter gameCharacter) {
        processAdventure(gameCharacter);
    }

    private void processAdventure(GameCharacter gameCharacter) {
        if (!gameCharacter.isFighting()) {
            coreWebClient.getActiveAdventureByCharacterId(gameCharacter.getId())
                    .doOnSuccess(adventure -> {
                        var currentEventStep = adventure.getStep();
                        if (currentEventStep < adventure.getAdventureEvents().size()) {
                            gameCharacter.setCurrentAction(adventure.getAdventureEvents().get(currentEventStep));
                            adventure.setStep(++currentEventStep);
                        } else {
                            finishAdventure(gameCharacter, adventure);
                        }
                    })
                    .doOnSuccess(coreWebClient::updateAdventure)
                    .then(coreWebClient.saveGameCharacter(gameCharacter))
                    .subscribe();
        }
    }

    private void finishAdventure(GameCharacter gameCharacter, Adventure adventure) {
        gameCharacter.setInAdventure(false);
        gameCharacter.setCurrentAction(FINISH_ADVENTURE_EVENT);
        adventure.setStatus(AdventureStatus.CLOSED);

        coreWebClient.getAwardsById(adventure.getAwardsId())
                .doOnSuccess(awards -> {
                    gameCharacter.getProgress()
                            .setCurrentExp(gameCharacter.getProgress().getCurrentExp() + awards.getExperience());
                    gameCharacter.getGameInventory()
                            .setGold(gameCharacter.getGameInventory().getGold() + awards.getGold());
                })
                .doOnSuccess(awards -> coreWebClient.deleteAwards(awards.getId()))
                .subscribe();
    }
}
