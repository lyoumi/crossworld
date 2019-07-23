package com.crossworld.web.handlers;

import reactor.core.publisher.Mono;

public interface AdminRequestHandler {

    Mono<Void> deleteAllCharacters();
}
