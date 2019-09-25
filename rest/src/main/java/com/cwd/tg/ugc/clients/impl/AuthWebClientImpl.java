package com.cwd.tg.ugc.clients.impl;

import static java.lang.String.format;

import com.cwd.tg.ugc.clients.AuthWebClient;
import com.cwd.tg.ugc.errors.exceptions.InternalCommunicationException;
import com.cwd.tg.ugc.errors.exceptions.ServiceNotAvailableException;
import com.cwd.tg.ugc.errors.exceptions.TokenValidationException;
import com.cwd.tg.ugc.errors.exceptions.UnauthorizedTokenException;
import com.cwd.tg.ugc.errors.http.HttpErrorMessage;
import com.cwd.tg.ugc.filters.OutgoingRequestResponseLoggingFilter;
import com.cwd.tg.ugc.security.User;
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

    private static final String AUTHORIZATION = "Authorization";
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
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                        clientResponse -> Mono.just(new UnauthorizedTokenException("Unable to authorize user token")))
                .onStatus(HttpStatus::is5xxServerError, this::mapErrorResponses)
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
