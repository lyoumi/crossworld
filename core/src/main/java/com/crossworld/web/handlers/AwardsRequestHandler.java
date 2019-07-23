package com.crossworld.web.handlers;

import com.crossworld.web.data.events.awards.Awards;
import reactor.core.publisher.Mono;

public interface AwardsRequestHandler {

    Mono<Awards> getAwards(String id);

    Mono<Awards> createAwards(Awards awards);

    Mono<Void> deleteAwards(String id);
}
