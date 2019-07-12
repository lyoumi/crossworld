package com.crossworld.web.dao;

import com.crossworld.web.entities.AwardsEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AwardsDao extends ReactiveMongoRepository<AwardsEntity, String> {

}
