package com.crossworld.web.client.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CoreWebClientImpl implements CoreWebClient {

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        return null;
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        return null;
    }

    @Override
    public Mono<GameEvent> getGameEventByCharacterId(String id) {
        return null;
    }
}
