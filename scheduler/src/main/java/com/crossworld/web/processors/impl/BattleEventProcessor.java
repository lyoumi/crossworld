package com.crossworld.web.processors.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.character.CharacterStats;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.character.GameInventory;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.processors.EventProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
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
                .subscribe(battleInfo ->
                        processBattle(battleInfo, gameCharacter)
                        .subscribe(gc ->
                                coreWebClient
                                        .saveGameCharacter(gc)
                                        .subscribeOn(Schedulers.parallel())
                                        .subscribe())
                );
    }

    private Mono<GameCharacter> processBattle(BattleInfo battleInfo, GameCharacter gameCharacter) {
        return coreWebClient.getMonsterById(battleInfo.getMonsterId())
                .doOnSuccess(monster -> {
                    var characterStats = gameCharacter.getStats();
                    var gameInventory = gameCharacter.getGameInventory();
                    if (canFight(gameCharacter, characterStats, gameInventory, monster)) {
                        characterStats.setHitPoints(characterStats.getHitPoints() - monster.getAttack());
                        monster.setHitPoints(monster.getHitPoints() - characterStats.getAttack());
                        if (monster.getHitPoints() < 1) {
                            removeMonster(gameCharacter, monster.getId(), battleInfo.getId());
                            coreWebClient.getAwardsById(battleInfo.getAwardsId())
                                    .subscribeOn(Schedulers.parallel())
                                    .subscribe(awards -> collectAwards(gameCharacter, awards),
                                            exception -> log.error("Something went wrong", exception),
                                            () -> coreWebClient.deleteAwards(battleInfo.getAwardsId())
                                                    .subscribeOn(Schedulers.parallel())
                                                    .subscribe());
                        } else {
                            coreWebClient.updateMonster(monster).subscribe();
                        }
                    } else {
                        removeMonster(gameCharacter, monster.getId(), battleInfo.getId());
                    }
                })
        .thenReturn(gameCharacter);
    }

    private void removeMonster(GameCharacter gameCharacter, String monsterId, String battleId) {
        gameCharacter.setFighting(false);
        coreWebClient.deleteBattleInfo(battleId).subscribeOn(Schedulers.parallel()).subscribe();
        coreWebClient.deleteMonster(monsterId).subscribeOn(Schedulers.parallel()).subscribe();
    }

    private void collectAwards(GameCharacter gameCharacter, Awards awards) {
        gameCharacter.getGameInventory().setGold(gameCharacter.getGameInventory().getGold() + awards.getGold());
        gameCharacter.getProgress().setCurrentExp(gameCharacter.getProgress().getCurrentExp() + awards.getExperience());
    }

    private boolean canFight(
            GameCharacter gameCharacter,
            CharacterStats characterStats,
            GameInventory gameInventory,
            Monster monster) {
        var canFight = true;
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
