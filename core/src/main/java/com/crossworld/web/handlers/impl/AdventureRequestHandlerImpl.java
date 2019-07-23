package com.crossworld.web.handlers.impl;

import static java.lang.String.format;

import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.errors.exceptions.AdventureNotFoundException;
import com.crossworld.web.handlers.AdventureRequestHandler;
import com.crossworld.web.repositories.AdventureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AdventureRequestHandlerImpl implements AdventureRequestHandler {

    private final AdventureRepository adventureRepository;

    @Override
    public Mono<Adventure> getAdventureById(String id) {
        return adventureRepository.getAdventureById(id)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(format("Adventure with id %s not found", id))));
    }

    @Override
    public Flux<Adventure> getAdventureByCharacterId(String characterId) {
        return adventureRepository.getAdventureByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(
                        format("Adventure with character id %s not found", characterId))));
    }

    @Override
    public Mono<Adventure> getActiveAdventureByCharacterId(String characterId) {
        return adventureRepository.getActiveAdventureByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new AdventureNotFoundException(
                        format("Active adventure with character id %s not found", characterId))));
    }

    @Override
    public Mono<Adventure> createAdventure(@RequestBody Adventure adventure) {
        return adventureRepository.saveAdventure(adventure);
    }

    @Override
    public Mono<Adventure> updateAdventure(@RequestBody Adventure adventure) {
        return adventureRepository.saveAdventure(adventure);
    }

    @Override
    public Mono<Void> deleteAdventure(String id) {
        return adventureRepository.deleteAdventure(id);
    }

    @Override
    public Mono<Void> deleteAdventureByCharacterId(String characterId) {
        return adventureRepository.deleteAdventureByCharacterId(characterId);
    }
}
