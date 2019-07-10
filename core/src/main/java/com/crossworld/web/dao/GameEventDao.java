package com.crossworld.web.dao;

import com.crossworld.web.entities.EventEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GameEventDao extends ReactiveMongoRepository<EventEntity, String> {

    Mono<EventEntity> getEventEntityByGameCharacterId(String characterId);
}
