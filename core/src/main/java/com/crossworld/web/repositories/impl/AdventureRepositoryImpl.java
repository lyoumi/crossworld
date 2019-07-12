package com.crossworld.web.repositories.impl;

import com.crossworld.web.converters.ConverterService;
import com.crossworld.web.dao.AdventureDao;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.repositories.AdventureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
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
    public Mono<Adventure> getAdventureByCharacterId(String characterId) {
        return adventureDao
                .findAdventureEntityByCharacterId(characterId)
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
