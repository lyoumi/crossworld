package com.crossworld.web.scheduler;

import com.crossworld.web.processors.BaseGameCharacterProcessor;
import com.crossworld.web.services.GameCharacterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class GameEventScheduler {

    private final GameCharacterService gameCharacterService;
    private final BaseGameCharacterProcessor baseGameCharacterProcessor;

    @Scheduled(fixedDelay = 10000)
    public void scheduleGameEvent() {
        gameCharacterService.getAllGameCharacters()
                .doOnError(throwable -> log.error("Something went wrong. Error during character processing.", throwable))
                .subscribe(baseGameCharacterProcessor::processCharacterEvents,
                        throwable -> log.error("Something went wrong", throwable));
    }
}
