package com.rpg.web.crossworld.dao;

import com.rpg.web.crossworld.entities.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDao extends MongoRepository<AccountEntity, String> {
}
