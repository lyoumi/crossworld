package com.crossworld.web.configuration;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.crossworld.web.errors.exceptions.MissingHeaderException;
import com.crossworld.web.errors.exceptions.ServiceNotAvailableException;
import com.crossworld.web.errors.handlers.CommonExceptionHandler;
import com.crossworld.web.errors.http.HttpErrorMessage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ExceptionHandlingConfiguration {

    private static final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> EXCEPTION_MAPPINGS =
            Map.of(
                    MissingHeaderException.class, exception ->
                            ServerResponse.badRequest()
                                    .body(fromObject(new HttpErrorMessage(1004000,
                                            BAD_REQUEST.getReasonPhrase(),
                                            exception.getMessage()))),
                    ServiceNotAvailableException.class, exception ->
                            ServerResponse.status(SERVICE_UNAVAILABLE)
                                    .body(fromObject(new HttpErrorMessage(1005030,
                                            INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                            INTERNAL_SERVER_ERROR.getReasonPhrase())))
            );

    @Bean
    @Primary
    public AbstractErrorWebExceptionHandler exceptionHandler(
            ErrorAttributes errorAttributes,
            ResourceProperties resourceProperties,
            ApplicationContext applicationContext,
            ObjectProvider<ViewResolver> viewResolvers,
            ServerCodecConfigurer serverCodecConfigurer) {

        var exceptionHandler = new CommonExceptionHandler(errorAttributes,
                resourceProperties, applicationContext, EXCEPTION_MAPPINGS);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
