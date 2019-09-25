package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.MonsterEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MonsterDao extends ReactiveMongoRepository<MonsterEntity, String> {

}
