package com.crossworld.auth.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.auth.data.CWAuthority;
import com.crossworld.auth.data.CWRole;
import com.crossworld.auth.data.CWUser;
import com.crossworld.auth.handlers.AuthRequestHandler;
import com.crossworld.auth.handlers.UserRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction routerFunction(
            UserRequestHandler userRequestHandler,
            AuthRequestHandler authRequestHandler) {
        return
                //temporary routes
                route(GET("/private/auth/user/name/{name}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("name"))
                                .flatMap(userRequestHandler::getUserByName), CWUser.class))
                        .andRoute(GET("/private/auth/user/all"), request ->
                                ServerResponse.ok().body(userRequestHandler.findAllUsers(), CWUser.class))
                        .andRoute(POST("/private/auth/user"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWUser.class)
                                        .flatMap(userRequestHandler::createUser), CWUser.class))
                        .andRoute(PUT("/private/auth/user"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWUser.class)
                                        .flatMap(userRequestHandler::updateUser), CWUser.class))
                        .andRoute(PUT("/private/auth/user/disable/{id}"), request ->
                                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::disableUser), CWUser.class))
                        .andRoute(DELETE("/private/auth/user/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteUserById)))

                        .andRoute(POST("/private/auth/permission"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWAuthority.class)
                                        .flatMap(userRequestHandler::createAuthority), CWAuthority.class))
                        .andRoute(DELETE("/private/auth/permission/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteAuthorityById)))

                        .andRoute(POST("/private/auth/role"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWRole.class)
                                        .flatMap(userRequestHandler::createRole), CWRole.class))
                        .andRoute(PUT("/private/auth/role"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWRole.class)
                                        .flatMap(userRequestHandler::updateRole), CWRole.class))
                        .andRoute(GET("/private/auth/role/{id}"), request ->
                                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::getRoleById), CWRole.class))
                        .andRoute(DELETE("/private/auth/role/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteRoleById)))

                        .andRoute(POST("/private/auth/generate"), request ->
                                ServerResponse.ok().body(request.bodyToMono(CWUser.class).flatMap(user ->
                                                authRequestHandler.generateUserToken(user, request.headers().asHttpHeaders())),
                                        JwtAuthenticationToken.class));
    }
}
