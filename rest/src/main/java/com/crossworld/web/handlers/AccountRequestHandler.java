package com.crossworld.web.handlers;

import com.crossworld.web.security.User;
import reactor.core.publisher.Mono;

public interface AccountRequestHandler {
    Mono<User> getUserDetails();
}
