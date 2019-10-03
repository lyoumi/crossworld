package com.cwd.tg.gps.scheduler;

import com.cwd.tg.gps.processors.BaseEventProcessor;
import com.cwd.tg.gps.services.GameCharacterService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
@Configuration
@EnableScheduling
@AllArgsConstructor
public class GameEventScheduler {

    private final GameCharacterService gameCharacterService;
    private final BaseEventProcessor baseEventProcessor;

    @Scheduled(cron = "5 * * * * ?")
    public void scheduleGameEvent() {
        gameCharacterService.getAllGameCharacters()
                .subscribeOn(Schedulers.elastic())
                .doOnError(
                        throwable -> log.error("Something went wrong. Error during character processing.", throwable))
                .window(100)
                .delayElements(Duration.ofSeconds(10))
                .subscribe(gcm -> gcm.subscribe(baseEventProcessor::processCharacterEvents,
                        throwable -> log.error("Something went wrong", throwable)),
                        throwable -> log.error("Something went wrong", throwable));
    }
}
