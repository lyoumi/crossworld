package com.cwd.tg.gss.repositories.impl;

import com.cwd.tg.gss.converters.ConverterService;
import com.cwd.tg.gss.dao.AdventureDao;
import com.cwd.tg.gss.data.events.adventure.Adventure;
import com.cwd.tg.gss.data.events.adventure.AdventureStatus;
import com.cwd.tg.gss.repositories.AdventureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AdventureRepositoryImpl implements AdventureRepository {

    private final AdventureDao adventureDao;
    private final ConverterService converterService;

    @Override
    public Mono<Adventure> saveAdventure(Adventure adventure) {
        return adventureDao
                .save(converterService.convert(adventure))
                .map(converterService::convert);
    }

    @Override
    public Mono<Adventure> getAdventureById(String id) {
        return adventureDao
                .findById(id)
                .map(converterService::convert);
    }

    @Override
    public Flux<Adventure> getAdventureByCharacterId(String characterId) {
        return adventureDao
                .findAdventureEntityByCharacterId(characterId)
                .map(converterService::convert);
    }

    @Override
    public Mono<Adventure> getActiveAdventureByCharacterId(String characterId) {
        return adventureDao
                .findAdventureEntityByStatusAndCharacterId(AdventureStatus.IN_PROGRESS.name(), characterId)
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteAdventure(String id) {
        return adventureDao.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAdventureByCharacterId(String characterId) {
        return adventureDao.deleteAdventureEntityByCharacterId(characterId);
    }
}
