package com.cwd.tg.gss.repositories;

import com.cwd.tg.gss.data.events.battle.BattleInfo;
import reactor.core.publisher.Mono;

public interface BattleInfoRepository {

    Mono<BattleInfo> saveBattleInfo(BattleInfo battleInfo);

    Mono<BattleInfo> getBattleInfoById(String id);

    Mono<BattleInfo> getBattleInfoByCharacterId(String characterId);

    Mono<Void> deleteBattleInfoById(String id);

    Mono<Void> deleteBattleInfoByCharacterId(String characterId);
}
