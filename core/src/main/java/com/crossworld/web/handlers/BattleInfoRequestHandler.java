package com.crossworld.web.handlers;

import com.crossworld.web.data.events.battle.BattleInfo;
import reactor.core.publisher.Mono;

public interface BattleInfoRequestHandler {

    Mono<BattleInfo> getBattleInfo(String id);

    Mono<BattleInfo> getBattleInfoByCharacterId(String characterId);

    Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo);

    Mono<Void> deleteBattleInfo(String id);

    Mono<Void> deleteBattleInfoByCharacterId(String characterId);
}
