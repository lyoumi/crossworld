package com.crossworld.web.filters;

import com.crossworld.web.exceptions.MissingHeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class LoggingWebFilter implements WebFilter {

    private static final String REQUEST_ID_HEADER = "request_id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var request = exchange.getRequest();
        logRequest(request);

        final List<String> requestId = request.getHeaders().get(REQUEST_ID_HEADER);

        if (requestId == null) {
            throw new MissingHeaderException(String.format("Required header is missing: %s", REQUEST_ID_HEADER));
        } else {
            exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, requestId.toString());
        }

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> logResponse(request, exchange.getResponse()))
                .doOnError(throwable -> log.error("Unable to process request {} {} {}",
                        requestId, request.getMethod(), request.getURI(), throwable));
    }

    private void logResponse(ServerHttpRequest request, ServerHttpResponse response) {
        log.info("Outgoing response request_id: {} {} {}: status {}", response.getHeaders(),
                request.getMethod(), request.getURI(), response.getStatusCode());
    }

    private void logRequest(ServerHttpRequest request) {
        log.info("Incoming request with request_id: {} {} {}",
                request.getHeaders(), request.getMethod(), request.getURI());
    }
}
