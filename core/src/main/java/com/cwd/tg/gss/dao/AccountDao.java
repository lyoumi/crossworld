package com.cwd.tg.gss.dao;

import com.cwd.tg.gss.entities.AccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountDao extends ReactiveMongoRepository<AccountEntity, String> {
}
