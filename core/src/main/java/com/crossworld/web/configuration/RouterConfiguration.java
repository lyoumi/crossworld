package com.crossworld.web.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.web.handlers.GameCharacterHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> router(GameCharacterHandler gameCharacterHandler) {
        return route(GET("/private/core/character/user/{user_id}"), gameCharacterHandler::getUsersGameCharacter)
                .andRoute(POST("/private/core/character"), gameCharacterHandler::saveCharacter)
                .andRoute(GET("/private/core/character/all"), gameCharacterHandler::getAllGameCharacters);

    }
}
