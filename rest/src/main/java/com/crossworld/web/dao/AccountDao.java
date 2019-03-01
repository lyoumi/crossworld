package com.crossworld.web.dao;

import com.crossworld.web.entities.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDao extends MongoRepository<AccountEntity, String> {
}
