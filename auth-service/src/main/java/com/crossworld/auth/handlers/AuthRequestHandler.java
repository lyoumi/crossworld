package com.crossworld.auth.handlers;

import com.crossworld.auth.data.CWUser;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;


public interface AuthRequestHandler {

    Mono<String> generateUserToken(CWUser cwUser, HttpHeaders headers);

    Mono<CWUser> validateUserToken(String auth);
}
