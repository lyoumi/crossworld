package com.crossworld.web.repositories;

import com.crossworld.web.data.events.awards.Awards;
import reactor.core.publisher.Mono;

public interface AwardsRepository {

    Mono<Awards> saveAwards(Awards awards);

    Mono<Awards> getAwardsById(String id);

    Mono<Void> deleteAwards(String id);
}
