package com.crossworld.web.client.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.rmi.server.ServerNotActiveException;
import java.util.Optional;

@Component
public class CoreWebClientImpl implements CoreWebClient {

    private static final String ALL_CHARACTERS_FORMAT = "%s/private/core/character/all";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/core/character";
    private static final String GAME_EVENT_FORMAT = "%s/private/core/character/event/";

    private final String getAllCharactersResource;
    private final String saveCharacterResource;
    private final String gameEventResource;

    public CoreWebClientImpl(LoadBalancerClient loadBalancerClient,
                             @Value("${services.core.instance.name}") String coreInstanceName)
            throws ServerNotActiveException {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(ServerNotActiveException::new);
        getAllCharactersResource = String.format(ALL_CHARACTERS_FORMAT, coreBaseUrl);
        saveCharacterResource = String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl);
        gameEventResource = String.format(GAME_EVENT_FORMAT, coreBaseUrl);
    }

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        return WebClient.create(getAllCharactersResource)
                .get()
                .retrieve()
                .bodyToFlux(GameCharacter.class)
                .log();
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        return WebClient.create(saveCharacterResource)
                .post()
                .retrieve()
                .bodyToMono(GameCharacter.class);
    }

    @Override
    public Mono<GameEvent> getGameEventByCharacterId(String id) {
        return WebClient.create(gameEventResource.concat(id))
                .get()
                .retrieve()
                .bodyToMono(GameEvent.class)
                .log();
    }

    @Override
    public Mono<GameEvent> saveGameEvent(GameEvent gameEvent) {
        return WebClient.create(GAME_EVENT_FORMAT)
                .post()
                .body(BodyInserters.fromObject(gameEvent))
                .retrieve()
                .bodyToMono(GameEvent.class)
                .log();
    }
}
