package com.crossworld.web.dao;

import com.crossworld.web.entities.BattleEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BattleInfoDao extends ReactiveMongoRepository<BattleEntity, String> {

    Mono<BattleEntity> findBattleEntityByCharacterId(String characterId);

    Mono<Void> deleteBattleEntityByCharacterId(String characterId);
}
