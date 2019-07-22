package com.crossworld.web.scheduler;

import com.crossworld.web.processors.BaseEventProcessor;
import com.crossworld.web.services.GameCharacterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class GameEventScheduler {

    private final GameCharacterService gameCharacterService;
    private final BaseEventProcessor baseEventProcessor;

    @Scheduled(fixedDelay = 10000)
    public void scheduleGameEvent() {
        gameCharacterService.getAllGameCharacters()
                .subscribeOn(Schedulers.elastic())
                .doOnError(throwable -> log.error("Something went wrong. Error during character processing.", throwable))
                .subscribe(baseEventProcessor::processCharacterEvents,
                        throwable -> log.error("Something went wrong", throwable));
    }
}
