package com.crossworld.auth.handlers;

import com.crossworld.auth.data.User;
import com.crossworld.auth.output.UserOutputPayload;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;


public interface AuthRequestHandler {

    Mono<String> generateUserToken(User cwUser, HttpHeaders headers);

    Mono<UserOutputPayload> validateUserToken(String auth);
}
