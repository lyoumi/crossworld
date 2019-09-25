package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.BattleEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BattleInfoDao extends ReactiveMongoRepository<BattleEntity, String> {

    Mono<BattleEntity> findBattleEntityByCharacterId(String characterId);

    Mono<Void> deleteBattleEntityByCharacterId(String characterId);
}
