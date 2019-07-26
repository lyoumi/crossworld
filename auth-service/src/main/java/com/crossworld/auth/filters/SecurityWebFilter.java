package com.crossworld.auth.filters;

import com.crossworld.auth.handlers.AuthRequestHandler;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Order(1)
@AllArgsConstructor
@Component
public class SecurityWebFilter implements WebFilter {

    private final AuthRequestHandler authRequestHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(exchange.getRequest())
                .map(HttpMessage::getHeaders)
                .map(httpHeaders -> httpHeaders.get("Authorization"))
                .map(headerValues -> headerValues.get(0))
                .flatMap(authRequestHandler::validateUserToken)
                .doOnSuccess(token -> SecurityContextHolder.getContext().setAuthentication(token))
                .then(chain.filter(exchange));
    }
}
