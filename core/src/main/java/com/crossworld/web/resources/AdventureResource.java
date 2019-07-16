package com.crossworld.web.resources;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.repositories.AdventureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/adventure")
public class AdventureResource {

    private final AdventureRepository adventureRepository;

    @ResponseStatus(OK)
    @GetMapping(value = "{id}")
    public Mono<Adventure> getAdventureById(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return adventureRepository
                .getAdventureById(id)
                .doOnSuccess(responseBody -> log.info("Outgoing response {} with body {}", requestId, responseBody))
                .doOnError(throwable -> log.error("Unable to get adventure { request_id: {}, adventure_id: {} }",
                        requestId, id, throwable));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "character/{character_id}")
    public Mono<Adventure> getAdventureByCharacterId(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository
                .getAdventureByCharacterId(characterId)
                .doOnSuccess(responseBody -> log.info("Outgoing response {} with body {}", requestId, responseBody))
                .doOnError(throwable -> log.error("Unable to get adventure { request_id: {}, character_id: {} }",
                        requestId, characterId, throwable));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "active/character/{character_id}")
    public Mono<Adventure> getActiveAdventureByCharacterId(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository
                .getActiveAdventureByCharacterId(characterId)
                .doOnError(throwable -> log.error("Unable to get adventure { request_id: {}, character_id: {} }",
                        requestId, characterId, throwable));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<Adventure> createAdventure(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody Adventure adventure) {
        return adventureRepository
                .saveAdventure(adventure)
                .doOnError(throwable -> log.error("Unable to create adventure { request_id: {}, request_body: {} }",
                        requestId, adventure, throwable));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteAdventure(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return adventureRepository
                .deleteAdventure(id)
                .doOnError(throwable -> log.error("Unable to get adventure { request_id: {}, adventure_id: {} }",
                        requestId, id, throwable));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "character/{character_id}")
    public Mono<Void> deleteAdventureByCharacterId(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "character_id") String characterId) {
        return adventureRepository
                .deleteAdventureByCharacterId(characterId)
                .doOnError(throwable -> log.error("Unable to get adventure { request_id: {}, character_id: {} }",
                        requestId, characterId, throwable));
    }
}
