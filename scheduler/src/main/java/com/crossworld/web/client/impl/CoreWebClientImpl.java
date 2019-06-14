package com.crossworld.web.client.impl;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.data.GameEvent;
import com.crossworld.web.exception.ServiceNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static final String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";
    private static final String REQUEST_ID_HEADER_NAME = "request_id";

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
        
        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(ALL_CHARACTERS_FORMAT, coreBaseUrl))
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(GameCharacter.class)
                .doOnNext(body -> log.info("Incoming response {} from {}: {{}}",
                        requestId, coreInstanceName, body))
                .doOnComplete(() -> log.info("Incoming response {} from {} was successfully received",
                        requestId, coreInstanceName))
                .doOnError(exception ->
                        log.error("Unable to process response {} from server: {}", requestId, exception));
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                .post()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromObject(gameCharacter))
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

        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(GAME_EVENT_FORMAT, coreBaseUrl))
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GameEvent.class);
    }

    @Override
    public Mono<GameEvent> saveGameEvent(GameEvent gameEvent) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(GAME_EVENT_FORMAT, coreBaseUrl))
                .post()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .body(BodyInserters.fromObject(gameEvent))
                .retrieve()
                .bodyToMono(GameEvent.class);
    }
}
