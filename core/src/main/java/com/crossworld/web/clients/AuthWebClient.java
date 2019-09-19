package com.crossworld.web.clients;

import com.crossworld.web.security.User;
import reactor.core.publisher.Mono;

public interface AuthWebClient {
    Mono<User> validateUserToken(String token, String requestId);
}
