package com.crossworld.auth.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.auth.data.CWUser;
import com.crossworld.auth.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction routerFunction(UserRepository userRepository) {
        return route(GET("/private/auth/user/name/{name}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("name"))
                                .map(userRepository::getByUsername)
                                .flatMap(Mono::just), CWUser.class))
                .andRoute(POST("/private/auth/user/"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWUser.class)
                                .map(userRepository::save)
                                .flatMap(Mono::just), CWUser.class));
    }

}
