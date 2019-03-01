package com.rpg.web.crossworld.services.impl;

import com.rpg.web.crossworld.services.GameCharacterService;
import com.rpg.web.crossworld.services.GameEventSchedulerService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class GameEventSchedulerServiceImpl implements GameEventSchedulerService {

    private final GameCharacterService gameCharacterService;
    private final ApplicationEventPublisher eventPublisher;

    public GameEventSchedulerServiceImpl(GameCharacterService gameCharacterService,
                                         ApplicationEventPublisher eventPublisher) {
        this.gameCharacterService = gameCharacterService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void scheduleGameEvent() {
        var gameCharacters = gameCharacterService.getAllGameCharacters();
        gameCharacters.parallelStream().forEach(eventPublisher::publishEvent);
    }
}
