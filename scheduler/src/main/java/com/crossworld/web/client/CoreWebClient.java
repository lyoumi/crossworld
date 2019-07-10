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

    Mono<Adventure> saveAdventure(Adventure adventure);
    Mono<Adventure> getActiveAdventureByCharacterId(String gameCharacterId);

    Mono<BattleInfo> saveBattleInfo(BattleInfo battleInfo);
    Mono<BattleInfo> getBattleInfoByCharacterId(String characterId);

    Mono<Monster> saveMonster(Monster monster);
    Mono<Monster> getMonsterById(String id);
    Mono<Void> deleteMonster(String id);

    Mono<Awards> saveAwards(Awards awards);
    Mono<Awards> getAwardsById(String id);
}
