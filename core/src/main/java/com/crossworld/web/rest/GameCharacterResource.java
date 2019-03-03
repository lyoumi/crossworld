package com.crossworld.web.rest;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("private/core/character")
public class GameCharacterResource {

    private final GameCharacterRepository gameCharacterRepository;

    @GetMapping(value = "user/{user_id}")
    public Mono<GameCharacter> getUsersGameCharacter(
            @PathVariable("user_id") String userId) {
        return gameCharacterRepository.getUsersCharacter(userId);
    }

    @PostMapping("save")
    public Mono<GameCharacter> saveCharacter(@RequestBody GameCharacter gameCharacter) {
        return gameCharacterRepository.save(gameCharacter);
    }

    @GetMapping("all")
    public Flux<GameCharacter> getAllGameCharacters() {
        return gameCharacterRepository.getAllGameCharacters();
    }

}
