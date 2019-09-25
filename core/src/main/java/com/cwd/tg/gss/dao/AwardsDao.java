package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.AwardsEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AwardsDao extends ReactiveMongoRepository<AwardsEntity, String> {

}
