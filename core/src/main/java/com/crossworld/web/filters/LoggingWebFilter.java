package com.crossworld.web.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logRequest(exchange.getRequest());
        return chain.filter(exchange).doOnSuccess(aVoid -> logResponse(exchange.getRequest(), exchange.getResponse()));
    }

    private void logResponse(ServerHttpRequest request, ServerHttpResponse response) {
        log.info("Outgoing response request_id: {} {} {}: status {}", request.getHeaders().get("request_id"),
                request.getMethod(), request.getURI(), response.getStatusCode());
    }

    private void logRequest(ServerHttpRequest request) {
        log.info("Incoming request with request_id: {} {} {}: request body: {}",
                request.getHeaders().get("request_id"),
                request.getMethod(), request.getURI(), request.getBody());
    }
}
