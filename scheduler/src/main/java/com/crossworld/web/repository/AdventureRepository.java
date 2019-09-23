package com.crossworld.web.repository;

import com.crossworld.web.entity.AdventureEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AdventureRepository extends ReactiveMongoRepository<AdventureEntity, String> {
}
