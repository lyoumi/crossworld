package com.cwd.tg.gss.handlers;

import reactor.core.publisher.Mono;

public interface AdminRequestHandler {

    Mono<Void> deleteAllCharacters();
}
