package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.GameCharacterEntity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GameCharacterDao extends ReactiveMongoRepository<GameCharacterEntity, String> {

    Mono<GameCharacterEntity> findByUserId(String userId);
}
