package com.crossworld.web.dao;

import com.crossworld.web.entities.MonsterEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MonsterDao extends ReactiveMongoRepository<MonsterEntity, String> {

}
