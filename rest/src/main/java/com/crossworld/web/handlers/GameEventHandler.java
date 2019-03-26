package com.crossworld.web.handlers;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.crossworld.web.data.internal.GameCharacter;
import com.crossworld.web.services.GameControllerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameEventHandler {

    private final GameControllerService gameControllerService;

    public Mono<ServerResponse> createGameCharacter(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(
                        serverRequest.bodyToMono(GameCharacter.class)
                                .doOnSuccess(playerCharacterInput ->
                                        gameControllerService
                                                .saveCharacter(
                                                        new GameCharacter(
                                                                randomUUID().toString(),
                                                                playerCharacterInput.getName(),
                                                                false,
                                                                randomUUID().toString()))),
                        GameCharacter.class
                ).log();
    }

    public Mono<ServerResponse> getGatUserCharacter(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(
                        gameControllerService
                                .getUserCharacter(serverRequest.pathVariable("user_id")),
                        GameCharacter.class
                ).log();
    }
}
