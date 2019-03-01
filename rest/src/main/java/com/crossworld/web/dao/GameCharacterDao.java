package com.crossworld.web.dao;

import com.crossworld.web.entities.GameCharacterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameCharacterDao extends MongoRepository<GameCharacterEntity, String> {
}
