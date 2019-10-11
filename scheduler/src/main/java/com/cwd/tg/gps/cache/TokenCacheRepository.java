package com.cwd.tg.gps.cache;

import com.cwd.tg.gps.security.UserToken;

import reactor.core.publisher.Mono;

public interface TokenCacheRepository {

    Mono<UserToken> getUserToken();

    void saveUserToken(UserToken userToken);
}
