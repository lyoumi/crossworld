package com.crossworld.web.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.web.handlers.GameEventHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> router(GameEventHandler gameEventHandler) {
        return route(POST("/private/rest/game"), gameEventHandler::createGameCharacter)
                .andRoute(GET("/private/rest/game/{user_id}"), gameEventHandler::getGatUserCharacter);
    }
}
