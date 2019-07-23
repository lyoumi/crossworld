package com.crossworld.web.handlers.impl;

import com.crossworld.web.handlers.AdminRequestHandler;
import com.crossworld.web.repositories.GameCharacterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class AdminRequestHandlerImpl implements AdminRequestHandler {

    private final GameCharacterRepository gameCharacterRepository;

    @Override
    public Mono<Void> deleteAllCharacters() {
        return gameCharacterRepository.deleteAll();
    }
}
