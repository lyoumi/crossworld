package com.crossworld.web.client.impl;

import static com.crossworld.web.logging.LoggingTemplates.INCOMING_RESPONSE;
import static com.crossworld.web.logging.LoggingTemplates.UNABLE_TO_GET_SUCCESS_RESPONSE;
import static com.crossworld.web.logging.LoggingTemplates.UNABLE_TO_READ_RESPONSE;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.exception.MessageNotReadableException;
import com.crossworld.web.exception.ServiceCommunicationException;
import com.crossworld.web.exception.ServiceNotAvailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoreWebClientImpl implements CoreWebClient {

    private static String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";
    private static String INTERNAL_COMMUNICATION_EXCEPTION = "Internal communication exception";

    private static String REQUEST_ID_HEADER_NAME = "request_id";

    private static String ALL_CHARACTERS_FORMAT = "%s/private/character/all/";
    private static String SAVE_CHARACTER_FORMAT = "%s/private/character/";
    private static String ADVENTURE_FORMAT = "%s/private/adventure/";
    private static String BATTLE_FORMAT = "%s/private/battle/";
    private static String MONSTER_FORMAT = "%s/private/monster/";
    private static String AWARDS_FORMAT = "%s/private/awards/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ALL_CHARACTERS_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, GET, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToFlux(GameCharacter.class)
                .doOnNext(body -> log.info(INCOMING_RESPONSE, requestId, GET, url, body))
                .doOnError(exception ->
                        log.error("Unable to process response {} from service {{} : {}}: {}",
                                requestId, GET, url, exception));
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(SAVE_CHARACTER_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .put()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .body(BodyInserters.fromObject(gameCharacter))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, PUT, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(GameCharacter.class);
    }

    @Override
    public Mono<Adventure> createAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .post()
                .body(BodyInserters.fromObject(adventure))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, POST, url, requestId,
                                    clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Adventure.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, POST, url))))
                .doOnSuccess(storedAdventure -> log.info(INCOMING_RESPONSE, requestId, POST, url, storedAdventure));
    }

    @Override
    public Mono<Adventure> updateAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .put()
                .body(BodyInserters.fromObject(adventure))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, PUT, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Adventure.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, PUT, url))))
                .doOnSuccess(storedAdventure -> log.info(INCOMING_RESPONSE, requestId, PUT, url, storedAdventure));
    }

    @Override
    public Mono<Adventure> getActiveAdventureByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl)
                .concat("character/")
                .concat(gameCharacterId);
        return WebClient.create(url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, GET, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Adventure.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, GET, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, GET, url, responseBody));
    }

    @Override
    public Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .post()
                .body(BodyInserters.fromObject(battleInfo))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, POST, url, requestId,
                                    clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(BattleInfo.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, POST, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, POST, url, responseBody));
    }

    @Override
    public Mono<BattleInfo> updateBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .put()
                .body(BodyInserters.fromObject(battleInfo))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, PUT, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(BattleInfo.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, PUT, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, PUT, url, responseBody));
    }

    @Override
    public Mono<BattleInfo> getBattleInfoByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl)
                .concat("character/")
                .concat(gameCharacterId);
        return WebClient.create(url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, GET, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(BattleInfo.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, GET, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, GET, url, responseBody));
    }

    @Override
    public Mono<Monster> createMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .post()
                .body(BodyInserters.fromObject(monster))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, POST, url, requestId,
                                    clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Monster.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, POST, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, POST, url, responseBody));
    }

    @Override
    public Mono<Monster> updateMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .put()
                .body(BodyInserters.fromObject(monster))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, PUT, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Monster.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, PUT, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, PUT, url, responseBody));
    }

    @Override
    public Mono<Void> deleteMonster(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl).concat(id);
        return WebClient.create(url)
                .delete()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, DELETE, url, requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                })
                .bodyToMono(Void.class)
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, DELETE, url, null));
    }

    @Override
    public Mono<Monster> getMonsterById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl).concat(id);
        return WebClient.create(url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, GET, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Monster.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, GET, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, GET, url, responseBody));
    }

    @Override
    public Mono<Awards> createAwards(Awards awards) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(AWARDS_FORMAT, coreBaseUrl);
        return WebClient.create(url)
                .post()
                .body(BodyInserters.fromObject(awards))
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, POST, url, requestId,
                                    clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Awards.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, POST, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, POST, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Awards> getAwardsById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(AWARDS_FORMAT, coreBaseUrl).concat(id);
        return WebClient.create(url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.is5xxServerError() || httpStatus.is4xxClientError(),
                        clientResponse -> {
                            log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, GET, url, requestId, clientResponse.statusCode());
                            return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
                        })
                .bodyToMono(Awards.class)
                .switchIfEmpty(Mono.error(new MessageNotReadableException(
                        format(UNABLE_TO_READ_RESPONSE, requestId, GET, url))))
                .doOnSuccess(responseBody -> log.info(INCOMING_RESPONSE, requestId, GET, coreBaseUrl, responseBody));
    }

    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));
    }
}
