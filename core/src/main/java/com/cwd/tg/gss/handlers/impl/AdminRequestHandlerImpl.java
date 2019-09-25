package com.cwd.tg.gss.handlers.impl;

import com.cwd.tg.gss.handlers.AdminRequestHandler;
import com.cwd.tg.gss.repositories.GameCharacterRepository;
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
