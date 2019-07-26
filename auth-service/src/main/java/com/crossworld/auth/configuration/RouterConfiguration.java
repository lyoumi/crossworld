package com.crossworld.auth.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.crossworld.auth.data.CWAuthority;
import com.crossworld.auth.data.CWRole;
import com.crossworld.auth.data.CWUser;
import com.crossworld.auth.handlers.AuthRequestHandler;
import com.crossworld.auth.repositories.PermissionRepository;
import com.crossworld.auth.repositories.RoleRepository;
import com.crossworld.auth.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction routerFunction(UserRepository userRepository,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            AuthRequestHandler authRequestHandler) {
        return route(GET("/private/auth/user/name/{name}"), request ->
                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("name"))
                        .map(userRepository::getByUsername)
                        .flatMap(Mono::just), CWUser.class))
                .andRoute(POST("/private/auth/user"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWUser.class)
                                .map(userRepository::save)
                                .flatMap(Mono::just), CWUser.class))
                .andRoute(PUT("/private/auth/user"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWUser.class)
                                .map(userRepository::save)
                                .flatMap(Mono::just), CWUser.class))

                .andRoute(POST("/private/auth/permission"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWAuthority.class)
                                .map(permissionRepository::save)
                                .flatMap(Mono::just), CWAuthority.class))
                .andRoute(PUT("/private/auth/permission"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWAuthority.class)
                                .map(permissionRepository::save)
                                .flatMap(Mono::just), CWAuthority.class))
                .andRoute(GET("/private/auth/permission/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .map(permissionRepository::findById)
                                .map(Optional::get)
                                .flatMap(Mono::just), CWAuthority.class))

                .andRoute(POST("/private/auth/role"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWRole.class)
                                .map(roleRepository::save)
                                .flatMap(Mono::just), CWRole.class))
                .andRoute(PUT("/private/auth/role"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWRole.class)
                                .map(roleRepository::save)
                                .flatMap(Mono::just), CWRole.class))
                .andRoute(GET("/private/auth/role/{id}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                .map(roleRepository::findById)
                                .map(Optional::get)
                                .flatMap(Mono::just), CWRole.class))

                .andRoute(POST("/private/auth/generate"), request ->
                        ServerResponse.ok().body(request.bodyToMono(CWUser.class).flatMap(user ->
                                        authRequestHandler.generateUserToken(user, request.headers().asHttpHeaders())),
                                JwtAuthenticationToken.class));
    }
}
