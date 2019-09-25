package com.cwd.tg.gps.client;

import reactor.core.publisher.Mono;

public interface AuthWebClient {
    Mono<String> buildAuthToken(String requestId);
}
