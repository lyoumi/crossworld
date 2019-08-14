package com.crossworld.auth.handlers.impl;

import com.crossworld.auth.data.Authority;
import com.crossworld.auth.data.User;
import com.crossworld.auth.errors.exceptions.AccessDeniedException;
import com.crossworld.auth.errors.exceptions.TokenExpirationException;
import com.crossworld.auth.handlers.AuthRequestHandler;
import com.crossworld.auth.output.UserOutputPayload;
import com.crossworld.auth.repositories.UserRepository;
import com.crossworld.auth.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthRequestHandlerImpl implements AuthRequestHandler {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<String> generateUserToken(User cwUser, HttpHeaders headers) {
        return Mono.just(cwUser.getUsername())
                .map(userRepository::getByUsername)
                .filter(user -> user.getPassword().equals(cwUser.getPassword()))
                .switchIfEmpty(Mono.error(new AccessDeniedException("Incorrect username or password")))
                .map(jwtTokenUtil::generateToken);
    }

    @Override
    public Mono<UserOutputPayload> validateUserToken(String token) {
        return Mono.just(token)
                .filter(userToken -> !jwtTokenUtil.isTokenExpired(userToken))
                .switchIfEmpty(Mono.error(new TokenExpirationException(String.format("User token %s was expired. Please, generate new token", token))))
                .map(jwtTokenUtil::getUsernameFromToken)
                .map(userRepository::getByUsername)
                .map(cwUser ->
                    new UserOutputPayload(cwUser.getUsername(), cwUser.getRoles().stream()
                            .flatMap(role -> role.getPermissions().stream())
                            .map(Authority::getPermission)
                            .collect(Collectors.toSet())));
    }
}
