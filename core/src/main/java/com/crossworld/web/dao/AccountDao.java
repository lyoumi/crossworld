package com.crossworld.web.dao;

import com.crossworld.web.entities.AccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountDao extends ReactiveMongoRepository<AccountEntity, String> {
}
