package com.rpg.web.crossworld.dao;

import com.rpg.web.crossworld.entities.GameCharacterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameCharacterDao extends MongoRepository<GameCharacterEntity, String> {
}
