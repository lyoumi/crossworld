package com.crossworld.web.dao;

import com.crossworld.web.entities.AdventureEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AdventureDao extends ReactiveMongoRepository<AdventureEntity, String> {

    Mono<AdventureEntity> findAdventureEntityByCharacterId(String characterId);

    Mono<AdventureEntity> findAdventureEntityByStatusAndCharacterId(String status, String characterId);

    Mono<Void> deleteAdventureEntityByCharacterId(String characterId);

}
