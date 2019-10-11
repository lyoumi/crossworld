package com.cwd.tg.gps.security.impl;

import com.cwd.tg.gps.cache.TokenCacheRepository;
import com.cwd.tg.gps.client.AuthWebClient;
import com.cwd.tg.gps.exception.TokenValidationException;
import com.cwd.tg.gps.security.JwtTokenUtil;
import com.cwd.tg.gps.security.SecurityService;
import com.cwd.tg.gps.security.UserToken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthWebClient authWebClient;
    private final TokenCacheRepository tokenCacheRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<String> getUserToken(String requestId) {
        return tokenCacheRepository.getUserToken()
                .filter(userToken -> !jwtTokenUtil.isTokenExpired(userToken.getToken()))
                .onErrorResume(TokenValidationException.class, e -> authWebClient.generateUserToken(requestId))
                .switchIfEmpty(authWebClient.generateUserToken(requestId)
                        .doOnSuccess(tokenCacheRepository::saveUserToken))
                .map(UserToken::getToken);
    }
}
