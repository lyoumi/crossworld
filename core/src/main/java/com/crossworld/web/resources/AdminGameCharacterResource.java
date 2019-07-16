package com.crossworld.web.resources;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("admin")
public class AdminGameCharacterResource {

    private final GameCharacterRepository gameCharacterRepository;

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "character/delete")
    public Mono<Void> deleteAllCharacters() {
        return gameCharacterRepository.deleteAll();
    }
}
