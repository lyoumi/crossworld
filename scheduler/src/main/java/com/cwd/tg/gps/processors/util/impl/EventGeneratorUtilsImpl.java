package com.cwd.tg.gps.processors.util.impl;

import com.cwd.tg.gps.data.events.adventure.Adventure;
import com.cwd.tg.gps.data.events.adventure.AdventureStatus;
import com.cwd.tg.gps.processors.util.EventGeneratorUtils;
import com.cwd.tg.gps.repository.AdventureRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Component
public class EventGeneratorUtilsImpl implements EventGeneratorUtils {

    private static final Random RANDOM = new Random();
    private static final int START_ADVENTURE_STEP_NUMBER = 0;

    private final AdventureRepository adventureRepository;

    @Override
    public Mono<Adventure> generateAdventure(String characterId, String awardsId) {
        return adventureRepository.findAll()
                .collectList()
                .map(adventureEntities -> adventureEntities.get(RANDOM.nextInt(adventureEntities.size())))
                .map(adventureEntity -> new Adventure(UUID.randomUUID().toString(), characterId,
                        adventureEntity.getDescription(), awardsId, AdventureStatus.IN_PROGRESS,
                        adventureEntity.getEvents(), START_ADVENTURE_STEP_NUMBER));
    }
}
