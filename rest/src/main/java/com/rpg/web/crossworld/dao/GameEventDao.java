package com.rpg.web.crossworld.dao;

import com.rpg.web.crossworld.entities.EventEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameEventDao extends MongoRepository<EventEntity, String> {

    EventEntity getEventEntityByGameCharacterId(String characterId);
}
