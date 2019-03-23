package com.crossworld.web.dao;

import com.crossworld.web.entities.GameCharacterEntity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GameCharacterDao extends ReactiveMongoRepository<GameCharacterEntity, String> {

    Mono<GameCharacterEntity> findByUserId(String userId);
}
