package com.rpg.web.crossworld.scheduler;

import com.rpg.web.crossworld.services.GameEventSchedulerService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class GameEventScheduler {

    private final GameEventSchedulerService gameEventSchedulerService;

    public GameEventScheduler(GameEventSchedulerService gameEventSchedulerService) {
        this.gameEventSchedulerService = gameEventSchedulerService;
    }

    @Scheduled(fixedDelay = 10000)
    public void scheduleGameEvent() {
        gameEventSchedulerService.scheduleGameEvent();
    }
}
