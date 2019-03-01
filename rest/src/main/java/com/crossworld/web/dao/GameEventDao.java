package com.crossworld.web.dao;

import com.crossworld.web.entities.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameEventDao extends MongoRepository<EventEntity, String> {

    EventEntity getEventEntityByGameCharacterId(String characterId);
}
