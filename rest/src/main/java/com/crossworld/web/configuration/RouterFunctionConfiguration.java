package com.crossworld.web.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.web.data.input.PlayerCharacterInput;
import com.crossworld.web.data.internal.character.GameCharacter;
import com.crossworld.web.handlers.GameRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterFunctionConfiguration {

    @Bean
    public RouterFunction routerFunction(GameRequestHandler gameRequestHandler) {
        return route(GET("/private/rest/game/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .flatMap(gameRequestHandler::getUserGameCharacter), GameCharacter.class))
                .andRoute(POST("/private/rest/game"), request ->
                        ServerResponse.ok().body(request.bodyToMono(PlayerCharacterInput.class)
                                .flatMap(gameRequestHandler::createGameCharacter), GameCharacter.class));
    }

}
