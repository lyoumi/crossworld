package com.crossworld.web.client;

import reactor.core.publisher.Mono;

public interface AuthWebClient {
    Mono<String> buildAuthToken(String requestId);
}
