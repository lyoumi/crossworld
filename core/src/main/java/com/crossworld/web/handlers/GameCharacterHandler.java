package com.crossworld.web.handlers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

import com.crossworld.web.data.GameCharacter;
import com.crossworld.web.repositories.GameCharacterRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameCharacterHandler {

    private final GameCharacterRepository gameCharacterRepository;

    public Mono<ServerResponse> getUsersGameCharacter(ServerRequest serverRequest) {
        String userId = serverRequest.pathVariable("user_id");
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(gameCharacterRepository.getUsersCharacter(userId), GameCharacter.class)
                .log()
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> saveCharacter(ServerRequest serverRequest) {
        return  ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(
                        serverRequest
                                .bodyToMono(GameCharacter.class)
                                .doOnSuccess(gameCharacterRepository::save),
                        GameCharacter.class)
                .log();
    }

    public Mono<ServerResponse> getAllGameCharacters(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_STREAM_JSON)
                .body(gameCharacterRepository.getAllGameCharacters(), GameCharacter.class)
                .log();
    }
}
