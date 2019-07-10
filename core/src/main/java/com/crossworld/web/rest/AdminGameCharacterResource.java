package com.crossworld.web.rest;

import com.crossworld.web.repositories.GameCharacterRepository;
import com.crossworld.web.repositories.GameEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("admin")
public class AdminGameCharacterResource {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameEventRepository gameEventRepository;

    @DeleteMapping(value = "event/delete")
    public Mono<Void> deleteAllEvents() {
        return gameEventRepository.deleteAll()
                .doOnSuccess(aVoid -> log.info("All events was successfully deleted"));
    }

    @DeleteMapping(value = "character/delete")
    public Mono<Void> deleteAllCharacters() {
        return gameCharacterRepository.deleteAll()
                .doOnSuccess(aVoid -> log.info("All characters was successfully deleted"));
    }
}
