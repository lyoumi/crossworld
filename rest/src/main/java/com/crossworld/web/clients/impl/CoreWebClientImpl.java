package com.crossworld.web.clients.impl;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.internal.character.GameCharacter;
import com.crossworld.web.exception.ServiceNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
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

    private static final String USER_CHARACTER_FORMAT = "%s/private/character/user/%s/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/character/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        String coreBaseUrl = getCoreBaseUrl();

        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(USER_CHARACTER_FORMAT, coreBaseUrl, userId))
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        String coreBaseUrl = getCoreBaseUrl();

        String requestId = UUID.randomUUID().toString();

        return WebClient.create(String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                .post()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .body(BodyInserters.fromObject(gameCharacter))
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }

    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));
    }
}
