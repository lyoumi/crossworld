package com.cwd.tg.gss.security;

import com.cwd.tg.gss.errors.exceptions.TokenExpirationException;
import com.cwd.tg.gss.errors.exceptions.TokenValidationException;
import com.cwd.tg.gss.errors.exceptions.UnauthorizedTokenException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class SecurityTokenBasedAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication)
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .cast(Authorization.class)
                .flatMap(this::authenticateToken)
                .map(user ->
                        new Authorization(user, (AuthHeaders) authentication.getCredentials()));
    }

    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(new TokenValidationException("Invalid Credentials"));
    }

    private Mono<User> authenticateToken(Authorization authenticationToken) {
        AuthHeaders authHeaders = authenticationToken.getCredentials();
        return Optional.of(authHeaders)
                .map(headers -> Mono.just(headers.getAuthToken())
                        .filter(token -> !jwtTokenUtil.isTokenExpired(token))
                        .switchIfEmpty(Mono.error(new TokenExpirationException("Token expired")))
                        .map(jwtTokenUtil::getUserFromToken)
                        .doOnSuccess(user -> log
                                .info("Authenticated user " + user.getUsername() + ", setting security context")))
                .orElseThrow(() -> new UnauthorizedTokenException("Authorization is missing"));
    }
}
