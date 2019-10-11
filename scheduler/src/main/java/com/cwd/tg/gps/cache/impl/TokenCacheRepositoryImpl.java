package com.cwd.tg.gps.cache.impl;

import com.cwd.tg.gps.cache.TokenCacheRepository;
import com.cwd.tg.gps.security.UserToken;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TokenCacheRepositoryImpl implements TokenCacheRepository {

    private static final String ID = "cwd_srvc_usr_tkn";

    private final ReactiveRedisOperations<String, UserToken> reactiveRedisTemplate;

    @Override
    public Mono<UserToken> getUserToken() {
        return reactiveRedisTemplate.opsForValue().get(ID);
    }

    @Override
    public void saveUserToken(UserToken userToken) {
        reactiveRedisTemplate.opsForValue().set(ID, userToken).subscribe();
    }
}
