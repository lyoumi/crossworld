package com.crossworld.web.events.processors;

import com.crossworld.web.data.BattleEventDetails;
import com.crossworld.web.data.CharacterStats;
import com.crossworld.web.data.EventStatus;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.data.GameInventory;
import com.crossworld.web.events.EventProcessor;
import com.crossworld.web.repositories.GameCharacterRepository;
import com.crossworld.web.repositories.GameEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("battleEventProcessor")
@AllArgsConstructor
public class BattleEventProcessor implements EventProcessor {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameEventRepository gameEventRepository;

    @Override
    public void processEvent(GameCharacter gameCharacter) {
        GameEvent gameEvent = gameEventRepository.getCharacterEvent(gameCharacter.getId());
        processFight(gameEvent, gameCharacter);
    }

    private void processFight(GameEvent gameEvent, GameCharacter gameCharacter) {
        var eventDetails = (BattleEventDetails) gameEvent.getEventDetails();
        var characterStats = gameCharacter.getStats();
        var gameInventory = gameCharacter.getGameInventory();

        boolean canFight = canFight(gameEvent, gameCharacter, eventDetails, characterStats, gameInventory);

        if (canFight) {
            var monster = eventDetails.getMonster();
            characterStats.setHitPoints(characterStats.getHitPoints() - monster.getAttack());
            monster.setHitPoints(monster.getHitPoints() - characterStats.getAttack());
            if (monster.getHitPoints() < 1) {
                collectAwards(gameInventory, eventDetails);
                gameCharacter.setHasEvent(false);
            }
        }

        gameCharacterRepository.save(gameCharacter);
    }

    private void collectAwards(GameInventory gameInventory, BattleEventDetails eventDetails) {
        gameInventory.setGold(gameInventory.getGold() + eventDetails.getGold());
        gameInventory.setExperience(gameInventory.getExperience() + eventDetails.getExperience());
    }

    private boolean canFight(GameEvent gameEvent,
                             GameCharacter gameCharacter,
                             BattleEventDetails eventDetails,
                             CharacterStats characterStats,
                             GameInventory gameInventory) {
        boolean canFight = true;
        if (eventDetails.getMonster().getAttack() > characterStats.getHitPoints()) {
            if (gameInventory.getHealingHitPointItems() > 0) {
                gameInventory.setHealingHitPointItems(gameInventory.getHealingHitPointItems() - 1);
                characterStats.setHitPoints(characterStats.getHitPoints() + 42);
            } else {
                gameCharacter.setHasEvent(false);
                gameEvent.setEventStatus(EventStatus.CLOSED);
                canFight = false;
            }
        }
        return canFight;
    }
}
