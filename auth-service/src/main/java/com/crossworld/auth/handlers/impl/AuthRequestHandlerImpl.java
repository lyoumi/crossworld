package com.crossworld.auth.handlers.impl;

import com.crossworld.auth.data.CWUser;
import com.crossworld.auth.errors.exceptions.AccessDeniedException;
import com.crossworld.auth.handlers.AuthRequestHandler;
import com.crossworld.auth.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthRequestHandlerImpl implements AuthRequestHandler {

    private final UserRepository userRepository;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    @Override
    public Mono<JwtAuthenticationToken> generateUserToken(CWUser cwUser, HttpHeaders headers) {
        return Mono.just(cwUser.getUsername())
                .flatMap(username -> Mono.just(userRepository.getByUsername(username)))
                .filter(user -> user.getPassword().equals(cwUser.getPassword()))
                .switchIfEmpty(Mono.error(new AccessDeniedException("Incorrect username or password")))
                .flatMap(user -> Mono.just(new Jwt(user.getId(), Instant.now(), Instant.now().plus(30,
                                        ChronoUnit.MINUTES),
                                        headers.entrySet().stream()
                                                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b)),
                                        getExtraInfo())))
                .map(jwt -> (JwtAuthenticationToken)jwtAuthenticationConverter.convert(jwt));
    }

    @Override
    public Mono<JwtAuthenticationToken> validateUserToken(String auth) {
        return Mono.empty();
    }

    public Map<String, Object> getExtraInfo() {
        return Map.of("user", "user");
    }


}
