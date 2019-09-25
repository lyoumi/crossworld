package com.cwd.tg.gss.repositories;

import com.cwd.tg.gss.data.events.awards.Awards;
import reactor.core.publisher.Mono;

public interface AwardsRepository {

    Mono<Awards> saveAwards(Awards awards);

    Mono<Awards> getAwardsById(String id);

    Mono<Void> deleteAwards(String id);
}
