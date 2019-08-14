package com.crossworld.web.clients.impl;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.crossworld.web.clients.AuthWebClient;
import com.crossworld.web.errors.exceptions.InternalCommunicationException;
import com.crossworld.web.errors.exceptions.ServiceNotAvailableException;
import com.crossworld.web.errors.exceptions.TokenValidationException;
import com.crossworld.web.errors.http.HttpErrorMessage;
import com.crossworld.web.filters.OutgoingRequestResponseLoggingFilter;
import com.crossworld.web.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthWebClientImpl implements AuthWebClient {

    private static final String VALIDATE_TOKEN_URL_FORMAT = "%s/private/auth/validate";

    private static final String REQUEST_ID_HEADER_NAME = "request_id";

    @Value("${services.auth.instance.name}")
    private String authInstanceName;

    private final LoadBalancerClient loadBalancerClient;
    private final OutgoingRequestResponseLoggingFilter loggingFilter;

    @Override
    public Mono<User> validateUserToken(String token, String requestId) {
        var authBaseUrl = getAuthBaseUrl();

        return buildWebClient(requestId, token, format(VALIDATE_TOKEN_URL_FORMAT, authBaseUrl))
                .get()
                .retrieve()
                .onStatus(HttpStatus.FORBIDDEN::equals,
                        clientResponse -> Mono.just(new TokenValidationException("Unable to validate user token")))
                .bodyToMono(User.class);
    }

    private WebClient buildWebClient(String requestId, String token, String url) {
        return WebClient.builder()
                .baseUrl(url)
                .filter(loggingFilter)
                .defaultHeaders(httpHeaders -> httpHeaders.putAll(
                        Map.of(AUTHORIZATION, Collections.singletonList(token),
                                REQUEST_ID_HEADER_NAME, Collections.singletonList(requestId))))
                .build();
    }

    //TODO: FTCW-8 - Replace with feign clients
    private String getAuthBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(authInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format("Service {%s} currently is not available.", authInstanceName)));
    }

    private Mono<? extends Throwable> mapErrorResponses(ClientResponse clientResponse) {
        return Mono.error(new InternalCommunicationException(format("Internal communication failure: status %s body %s",
                clientResponse.statusCode(), clientResponse.bodyToMono(HttpErrorMessage.class))));
    }
}
