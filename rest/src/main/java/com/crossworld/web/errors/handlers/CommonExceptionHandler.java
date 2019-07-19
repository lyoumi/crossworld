package com.crossworld.web.errors.handlers;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.crossworld.web.errors.http.HttpErrorMessage;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CommonExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionMappings;

    public CommonExceptionHandler(ErrorAttributes errorAttributes,
            ResourceProperties resourceProperties,
            ApplicationContext applicationContext,
            Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionMappings) {
        super(errorAttributes, resourceProperties, applicationContext);
        this.exceptionMappings = exceptionMappings;
    }

    @Override
    public RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> renderErrorResponse(request, errorAttributes));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request, ErrorAttributes errorAttributes) {
        var exception = errorAttributes.getError(request);
        return Optional.ofNullable(exceptionMappings.get(exception.getClass()))
                .map(throwableMonoFunction -> throwableMonoFunction.apply(exception))
                .orElse(ServerResponse.status(INTERNAL_SERVER_ERROR)
                        .body(fromObject(new HttpErrorMessage(100500,
                                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                INTERNAL_SERVER_ERROR.getReasonPhrase()))));
    }

}
