package com.crossworld.web.resources.impl;

import static java.util.UUID.randomUUID;

import com.crossworld.web.data.input.PlayerCharacterInput;
import com.crossworld.web.data.internal.character.CharacterProgress;
import com.crossworld.web.data.internal.character.CharacterStats;
import com.crossworld.web.data.internal.character.GameCharacter;
import com.crossworld.web.data.internal.character.GameInventory;
import com.crossworld.web.resources.GameRequestHandler;
import com.crossworld.web.services.GameControllerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameRequestHandlerImpl implements GameRequestHandler {

    private final GameControllerService gameControllerService;

    @Override
    public Mono<GameCharacter> createGameCharacter(PlayerCharacterInput playerCharacterInput) {
        return gameControllerService.saveCharacter(
                new GameCharacter(randomUUID().toString(), playerCharacterInput.getName(),
                        false, false, false, "",
                        new CharacterProgress(0, 1, 100),
                        new CharacterStats(10, 10, 10, 200, 100, 30),
                        new GameInventory(), randomUUID().toString()));
    }

    @Override
    public Mono<GameCharacter> getUserGameCharacter(String userId) {
        return gameControllerService.getUserCharacter(userId);
    }
}
