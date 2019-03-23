package com.crossworld.web.client.impl;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.exception.ServiceNotAvailableException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static final String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";

    private static final String ALL_CHARACTERS_FORMAT = "%s/private/core/character/all/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/core/character/";
    private static final String GAME_EVENT_FORMAT = "%s/private/core/character/event/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;


    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        return WebClient.create(String.format(ALL_CHARACTERS_FORMAT, coreBaseUrl))
                .get()
                .retrieve()
                .bodyToFlux(GameCharacter.class)
                .log();
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        return WebClient.create(String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                .post()
                .retrieve()
                .bodyToMono(GameCharacter.class);
    }

    @Override
    public Mono<GameEvent> getGameEventByCharacterId(String id) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        return WebClient.create(String.format(GAME_EVENT_FORMAT, coreBaseUrl))
                .get()
                .retrieve()
                .bodyToMono(GameEvent.class)
                .log();
    }

    @Override
    public Mono<GameEvent> saveGameEvent(GameEvent gameEvent) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        return WebClient.create(String.format(GAME_EVENT_FORMAT, coreBaseUrl))
                .post()
                .body(BodyInserters.fromObject(gameEvent))
                .retrieve()
                .bodyToMono(GameEvent.class)
                .log();
    }
}
