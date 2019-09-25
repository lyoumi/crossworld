package com.cwd.tg.gss.handlers.impl;

import static java.lang.String.format;

import com.cwd.tg.gss.data.events.awards.Awards;
import com.cwd.tg.gss.errors.exceptions.AwardsNotFoundException;
import com.cwd.tg.gss.handlers.AwardsRequestHandler;
import com.cwd.tg.gss.repositories.AwardsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AwardsRequestHandlerImpl implements AwardsRequestHandler {

    private final AwardsRepository awardsRepository;

    @Override
    public Mono<Awards> getAwards(String id) {
        return awardsRepository.getAwardsById(id)
                .switchIfEmpty(Mono.error(new AwardsNotFoundException(format("Awards with id %s not found", id))));
    }

    @Override
    public Mono<Awards> createAwards(Awards awards) {
        return awardsRepository.saveAwards(awards);
    }

    @Override
    public Mono<Void> deleteAwards(String id) {
        return awardsRepository.deleteAwards(id);
    }
}
