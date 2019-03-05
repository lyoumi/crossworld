package com.crossworld.web.clients.impl;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.rmi.server.ServerNotActiveException;
import java.util.Optional;

@Component
public class CoreWebClientImpl implements CoreWebClient {

    private static final String USER_CHARACTER_FORMAT = "%s/private/core/character/user/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/core/character";

    private final String saveCharacterResource;
    private final String userGameCharacterUrl;

    public CoreWebClientImpl(LoadBalancerClient loadBalancerClient,
                             @Value("${core.instance.name}") String coreInstanceName)
            throws ServerNotActiveException {
        String coreBaseUrl = Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(ServerNotActiveException::new);
        userGameCharacterUrl = String.format(USER_CHARACTER_FORMAT, coreBaseUrl);
        saveCharacterResource = String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl);
    }

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        return WebClient.create(userGameCharacterUrl.concat(userId))
                .get()
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        return WebClient.create(saveCharacterResource)
                .post()
                .retrieve()
                .bodyToMono(GameCharacter.class)
                .log();
    }
}
