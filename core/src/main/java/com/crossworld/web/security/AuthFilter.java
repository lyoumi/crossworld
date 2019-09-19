package com.crossworld.web.security;

import com.crossworld.web.clients.AuthWebClient;
import com.crossworld.web.errors.exceptions.MissingHeaderException;
import com.crossworld.web.errors.exceptions.UnauthorizedException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthFilter implements WebFilter {

    private static final String REQUEST_ID = "request_id";
    private static final String AUTHORIZATION = "Authorization";

    private final AuthWebClient authWebClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = Optional
                .ofNullable(exchange.getRequest().getHeaders().get(AUTHORIZATION))
                .map(headers -> headers.get(0))
                .orElseThrow(() -> new UnauthorizedException("Authorization header is missing"));

        var requestId = Optional
                .ofNullable(exchange.getRequest().getHeaders().get(REQUEST_ID))
                .map(headers -> headers.get(0))
                .orElseThrow(() -> new MissingHeaderException("Request id header is missing"));

        return authWebClient.validateUserToken(token, requestId)
                .doOnSuccess(userDetails -> SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                new Authorization(userDetails, new AuthHeaders(requestId, token))))
                .then(chain.filter(exchange));
    }
}
