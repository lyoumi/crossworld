package com.crossworld.web.client.impl;

import static java.lang.String.format;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.crossworld.web.client.AuthWebClient;
import com.crossworld.web.exception.ServiceNotAvailableException;
import com.crossworld.web.filters.OutgoingRequestResponseLoggingFilter;
import com.crossworld.web.security.ServiceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthWebClientImpl implements AuthWebClient {
    private static final String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";
    private static final String REQUEST_ID_HEADER_NAME = "request_id";
    private static final String AUTH_FORMAT = "%s/private/auth/generate/";

    @Value("${services.auth.instance.name}")
    private String authInstanceName;

    private final LoadBalancerClient loadBalancerClient;
    private final OutgoingRequestResponseLoggingFilter loggingFilter;
    private final ServiceUser serviceUser;

    @Override
    public Mono<String> buildAuthToken(String requestId) {
        var coreBaseUrl = getAuthBaseUrl();

        String url = format(AUTH_FORMAT, coreBaseUrl);

        return buildWebClient(requestId, url)
                .post()
                .body(fromObject(serviceUser))
                .retrieve()
                .bodyToMono(String.class);
    }

    private String getAuthBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(authInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, authInstanceName)));
    }

    private WebClient buildWebClient(String requestId, String url) {
        return WebClient.builder()
                .filter(loggingFilter)
                .baseUrl(url)
                .defaultHeaders(httpHeaders -> httpHeaders.add(REQUEST_ID_HEADER_NAME, requestId))
                .build();
    }
}
