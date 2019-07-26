package com.crossworld.auth.handlers;

import com.crossworld.auth.data.CWUser;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;


public interface AuthRequestHandler {

    Mono<JwtAuthenticationToken> generateUserToken(CWUser cwUser, HttpHeaders headers);

    Mono<JwtAuthenticationToken> validateUserToken(String auth);
}
