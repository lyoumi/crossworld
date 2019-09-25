package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.AdventureEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdventureDao extends ReactiveMongoRepository<AdventureEntity, String> {

    Flux<AdventureEntity> findAdventureEntityByCharacterId(String characterId);

    Mono<AdventureEntity> findAdventureEntityByStatusAndCharacterId(String status, String characterId);

    Mono<Void> deleteAdventureEntityByCharacterId(String characterId);

}
