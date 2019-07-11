package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.character.CharacterStats;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.character.GameInventory;
import com.crossworld.web.processors.EventProcessor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component("battleEventProcessor")
@AllArgsConstructor
public class BattleEventProcessor implements EventProcessor {

    private final CoreWebClient coreWebClient;

    @Override
    public void processEvent(GameCharacter gameCharacter) {
        processFight(gameCharacter);
    }

    private void processFight(GameCharacter gameCharacter) {
        coreWebClient.getBattleInfoByCharacterId(gameCharacter.getId())
                .subscribe(battleInfo -> processBattle(battleInfo, gameCharacter));

        coreWebClient.saveGameCharacter(gameCharacter).subscribe();
    }

    private void processBattle(BattleInfo battleInfo, GameCharacter gameCharacter) {
        coreWebClient.getMonsterById(battleInfo.getMonsterId())
                .subscribe(monster -> {
                    var characterStats = gameCharacter.getStats();
                    final GameInventory gameInventory = gameCharacter.getGameInventory();
                    if (canFight(gameCharacter, characterStats, gameInventory, monster)) {
                        characterStats.setHitPoints(characterStats.getHitPoints() - monster.getAttack());
                        monster.setHitPoints(monster.getHitPoints() - characterStats.getAttack());
                        if (monster.getHitPoints() < 1) {
                            gameCharacter.setFighting(false);
                            coreWebClient.deleteMonster(monster.getId()).subscribe(
                                    aVoid -> coreWebClient.getAwardsById(battleInfo.getAwardsId())
                                            .subscribe(awards -> collectAwards(gameInventory, awards))
                            );
                        }
                    } else {
                        gameCharacter.setFighting(false);
                        coreWebClient.deleteMonster(monster.getId());
                    }
                });
    }

    private void collectAwards(GameInventory gameInventory, Awards awards) {
        gameInventory.setGold(gameInventory.getGold() + awards.getGold());
        gameInventory.setExperience(gameInventory.getExperience() + awards.getExperience());
    }

    private boolean canFight(
            GameCharacter gameCharacter,
            CharacterStats characterStats,
            GameInventory gameInventory,
            Monster monster) {
        boolean canFight = true;
        if (monster.getAttack() > characterStats.getHitPoints()) {
            if (gameInventory.getHealingHitPointItems() > 0) {
                gameInventory.setHealingHitPointItems(gameInventory.getHealingHitPointItems() - 1);
                characterStats.setHitPoints(characterStats.getHitPoints() + 42);
            } else {
                gameCharacter.setInAdventure(false);
                canFight = false;
            }
        }
        return canFight;
    }
}
