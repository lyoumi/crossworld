package com.crossworld.web.services.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.services.GameCharacterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class GameCharacterServiceImpl implements GameCharacterService {

    private final CoreWebClient gameCharacterRepository;

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        return gameCharacterRepository.getAllGameCharacters();
    }
}