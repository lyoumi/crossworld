package com.crossworld.web.resources;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.errors.exceptions.CharacterNotFoundException;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public Mono<GameCharacter> getUsersGameCharacter(@PathVariable("user_id") String userId) {
        return gameCharacterRepository.getUsersCharacter(userId)
                .switchIfEmpty(Mono.error(
                        new CharacterNotFoundException(format("Character with user id %s not found", userId))));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<GameCharacter> createCharacter(@RequestBody GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter);

    }

    @ResponseStatus(OK)
    @PutMapping
    public Mono<GameCharacter> updateCharacter(@RequestBody GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter);

    }

    @ResponseStatus(OK)
    @GetMapping(value = "all", produces = APPLICATION_STREAM_JSON_VALUE)
    public Flux<GameCharacter> getAllGameCharacters() {
        return gameCharacterRepository.getAllGameCharacters();
    }
}
