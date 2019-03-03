package com.crossworld.web.clients.impl;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.GameCharacter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CoreWebClientImpl implements CoreWebClient {

    private static final String USER_SUFFIX = "/user/%s";

    private final String baseUrl;
    private final String userGameCharcterUrl;

    public CoreWebClientImpl(@Value("${core.base}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.userGameCharcterUrl = baseUrl.concat(USER_SUFFIX);
    }

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        return WebClient.create(userGameCharcterUrl)
                .get()
                .exchange()
                .log()
                .flatMap(clientResponse -> clientResponse.bodyToMono(GameCharacter.class));
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        return WebClient.create(baseUrl)
                .post()
                .exchange()
                .log()
                .flatMap(clientResponse -> clientResponse.bodyToMono(GameCharacter.class));
    }
}
