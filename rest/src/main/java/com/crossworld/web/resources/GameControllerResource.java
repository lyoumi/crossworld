package com.crossworld.web.resources;

import static java.util.UUID.randomUUID;

import com.crossworld.web.data.input.PlayerCharacterInput;
import com.crossworld.web.data.internal.GameCharacter;
import com.crossworld.web.services.GameControllerService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("private/rest/game")
@AllArgsConstructor
public class GameControllerResource {

    private final GameControllerService gameControllerService;

    @PostMapping
    public Mono<GameCharacter> createGameCharacter(@RequestBody PlayerCharacterInput playerCharacterInput) {
        return gameControllerService.saveCharacter(
                new GameCharacter(randomUUID().toString(), playerCharacterInput.getName(),
                        false, randomUUID().toString()));
    }

    @GetMapping("{user_id}")
    public Mono<GameCharacter> getUserGameCharacter(@PathVariable("user_id") String userId) {
        return gameControllerService.getUserCharacter(userId);
    }
}
