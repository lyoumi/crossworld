package com.crossworld.web.clients.impl;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.exception.ServiceNotAvailableException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static final String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";
    
    private static final String USER_CHARACTER_FORMAT = "%s/private/core/character/user/%s/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/core/character/";

    @Value("${core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));

        return WebClient.create(String.format(USER_CHARACTER_FORMAT, coreBaseUrl, userId))
                .get()
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow();

        return WebClient.create(String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                .post()
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }
}
