package com.crossworld.web.client;

import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.data.character.GameCharacter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CoreWebClient {

    Flux<GameCharacter> getAllGameCharacters();
    Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter);

    Mono<Adventure> createAdventure(Adventure adventure);

    Mono<Adventure> updateAdventure(Adventure adventure);

    Mono<Adventure> getActiveAdventureByCharacterId(String gameCharacterId);

    Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo);

    Mono<BattleInfo> updateBattleInfo(BattleInfo battleInfo);

    Mono<BattleInfo> getBattleInfoByCharacterId(String characterId);

    Mono<Monster> createMonster(Monster monster);
    Mono<Monster> getMonsterById(String id);

    Mono<Monster> updateMonster(Monster monster);

    Mono<Void> deleteMonster(String id);

    Mono<Awards> createAwards(Awards awards);
    Mono<Awards> getAwardsById(String id);
}
