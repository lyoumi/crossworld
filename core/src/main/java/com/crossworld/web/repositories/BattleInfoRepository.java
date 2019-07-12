package com.crossworld.web.repositories;

import com.crossworld.web.data.events.battle.BattleInfo;
import reactor.core.publisher.Mono;

public interface BattleInfoRepository {

    Mono<BattleInfo> saveBattleInfo(BattleInfo battleInfo);

    Mono<BattleInfo> getBattleInfoById(String id);

    Mono<BattleInfo> getBattleInfoByCharacterId(String characterId);

    Mono<Void> deleteBattleInfoById(String id);

    Mono<Void> deleteBattleInfoByCharacterId(String characterId);
}
