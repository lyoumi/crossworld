package com.crossworld.web.services.impl;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.internal.character.GameCharacter;
import com.crossworld.web.services.GameControllerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GameControllerServiceImpl implements GameControllerService {

    private final CoreWebClient coreWebClient;

    @Override
    public Mono<GameCharacter> saveCharacter(GameCharacter gameCharacter) {
        return coreWebClient.saveUserCharacter(gameCharacter);
    }

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        return coreWebClient.getUserCharacter(userId);
    }
}
