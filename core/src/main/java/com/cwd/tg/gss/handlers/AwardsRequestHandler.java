package com.cwd.tg.gss.handlers;

import com.cwd.tg.gss.data.events.awards.Awards;
import reactor.core.publisher.Mono;

public interface AwardsRequestHandler {

    Mono<Awards> getAwards(String id);

    Mono<Awards> createAwards(Awards awards);

    Mono<Void> deleteAwards(String id);
}
