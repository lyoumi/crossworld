package com.crossworld.web.clients.impl;

import static com.crossworld.web.security.SecurityUtils.getAuthHeaders;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.crossworld.web.clients.CoreWebClient;
import com.crossworld.web.data.internal.character.GameCharacter;
import com.crossworld.web.errors.exceptions.GameCharacterNotFoundException;
import com.crossworld.web.errors.exceptions.InternalCommunicationException;
import com.crossworld.web.errors.exceptions.ServiceNotAvailableException;
import com.crossworld.web.errors.http.HttpErrorMessage;
import com.crossworld.web.filters.OutgoingRequestResponseLoggingFilter;
import com.crossworld.web.security.AuthHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static final String REQUEST_ID = "request_id";
    private static final String AUTHORIZATION = "Authorization";

    private static final String GET_CHARACTER_FORMAT = "%s/private/character/user/%s/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/character/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;
    private final OutgoingRequestResponseLoggingFilter loggingFilter;

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        String coreBaseUrl = getCoreBaseUrl();

        final String url = format(GET_CHARACTER_FORMAT, coreBaseUrl, userId);
        return getAuthHeaders()
                .flatMap(authHeaders ->
                        buildWebClient(url, authHeaders)
                                .get()
                                .retrieve()
                                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND),
                                        clientResponse -> Mono.error(
                                                new GameCharacterNotFoundException(
                                                        format("Unable to get response by user id %s", userId))))
                                .onStatus(HttpStatus::isError, this::mapErrorResponses)
                                .bodyToMono(GameCharacter.class));
    }

    @Override
    public Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter) {
        String coreBaseUrl = getCoreBaseUrl();

        final String url = format(SAVE_CHARACTER_FORMAT, coreBaseUrl);

        return getAuthHeaders()
                .flatMap(authHeaders ->
                        buildWebClient(url, authHeaders)
                                .post()
                                .body(BodyInserters.fromObject(gameCharacter))
                                .retrieve()
                                .onStatus(HttpStatus::isError, this::mapErrorResponses)
                                .bodyToMono(GameCharacter.class));
    }

    private WebClient buildWebClient(String url, AuthHeaders authHeaders) {

        return WebClient.builder()
                .baseUrl(url)
                .filter(loggingFilter)
                .defaultHeaders(httpHeaders ->
                        httpHeaders.putAll(
                                Map.of(
                                        REQUEST_ID, singletonList(authHeaders.getRequestId()),
                                        AUTHORIZATION, singletonList(authHeaders.getAuthToken()))))
                .build();
    }

    //TODO: FTCW-8 - Replace with feign clients
    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format("Service {%s} currently is not available.", coreInstanceName)));
    }

    private Mono<? extends Throwable> mapErrorResponses(ClientResponse clientResponse) {
        return Mono.error(new InternalCommunicationException(format("Internal communication failure: status %s body %s",
                clientResponse.statusCode(), clientResponse.bodyToMono(HttpErrorMessage.class))));
    }
}
