package com.crossworld.web.resources;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/character")
public class GameCharacterResource {

    private final GameCharacterRepository gameCharacterRepository;

    @ResponseStatus(OK)
    @GetMapping(value = "user/{user_id}")
    public Mono<GameCharacter> getUsersGameCharacter(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable("user_id") String userId) {
        return gameCharacterRepository.getUsersCharacter(userId)
                .doOnSuccess(character -> log.info("Outgoing response { request_id: {} } with body {}",
                                requestId, character))
                .doOnError(throwable -> log.error("Unable to get game character by user_id: { user_id: {}, request_id: {} }",
                                userId, requestId, throwable));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<GameCharacter> createCharacter(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter)
                .doOnSuccess(character -> log.info("Outgoing response { request_id: {} } with body {}",
                                requestId, character))
                .doOnError(throwable -> log.error("Unable to store game character: { body: {}, request_id: {} }",
                                gameCharacter, requestId, throwable));

    }

    @ResponseStatus(OK)
    @PutMapping
    public Mono<GameCharacter> updateCharacter(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter)
                .doOnSuccess(character -> log.info("Outgoing response { request_id: {} } with body {}",
                                requestId, character))
                .doOnError(throwable -> log.error("Unable to store game character: { body: {}, request_id: {} }",
                                gameCharacter, requestId, throwable));

    }

    @ResponseStatus(OK)
    @GetMapping(value = "all", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<GameCharacter> getAllGameCharacters(
            @RequestHeader(value = "request_id") String requestId) {
        return gameCharacterRepository.getAllGameCharacters()
                .doOnNext(gameCharacter -> log.info("Outgoing response { request_id: {} } part with body: {}",
                                requestId, gameCharacter))
                .doOnComplete(() -> log.info("Outgoing response { request_id: {} } successfully sent", requestId))
                .doOnError(throwable -> log.error("Unable to get all game characters", throwable));
    }
}
