package com.crossworld.web.scheduler;

import com.crossworld.web.services.GameCharacterService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class GameEventScheduler {

    private final GameCharacterService gameCharacterService;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 10000)
    public void scheduleGameEvent() {
        gameCharacterService.getAllGameCharacters().doOnEach(eventPublisher::publishEvent).subscribe();
    }
}
