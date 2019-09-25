package com.cwd.tg.gss.clients;

import com.cwd.tg.gss.security.User;
import reactor.core.publisher.Mono;

public interface AuthWebClient {
    Mono<User> validateUserToken(String token, String requestId);
}
