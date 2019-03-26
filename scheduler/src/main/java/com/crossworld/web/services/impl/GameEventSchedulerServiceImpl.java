package com.crossworld.web.services.impl;

import com.crossworld.web.services.GameCharacterService;
import com.crossworld.web.services.GameEventSchedulerService;

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
        gameCharacterService.getAllGameCharacters()
                .log()
                .subscribe(eventPublisher::publishEvent);
    }
}
