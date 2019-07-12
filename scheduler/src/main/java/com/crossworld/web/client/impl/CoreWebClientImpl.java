package com.crossworld.web.client.impl;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.data.character.GameCharacter;
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

    private static final String SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE = "Service {%s} currently is not available.";
    private static final String REQUEST_ID_HEADER_NAME = "request_id";

    private static final String ALL_CHARACTERS_FORMAT = "%s/private/character/all/";
    private static final String SAVE_CHARACTER_FORMAT = "%s/private/character/";
    private static final String ADVENTURE_FORMAT = "%s/private/adventure/";
    private static final String BATTLE_FORMAT = "%s/private/battle/";
    private static final String MONSTER_FORMAT = "%s/private/monster/";
    private static final String AWARDS_FORMAT = "%s/private/awards/";

    @Value("${services.core.instance.name}")
    private String coreInstanceName;

    private final LoadBalancerClient loadBalancerClient;

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(ALL_CHARACTERS_FORMAT, coreBaseUrl))
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error(
                            "Unable to get success response: { request_id: {}, status_code: {}} from service {{} : {}}",
                            requestId, clientResponse.statusCode(), coreInstanceName, coreBaseUrl);
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToFlux(GameCharacter.class)
                .doOnNext(body -> log.info("Incoming response {} from {}: {{}}",
                        requestId, coreInstanceName, body))
                .doOnComplete(() -> log.info("Incoming response {} from {{} : {}} was successfully received",
                        requestId, coreInstanceName, coreBaseUrl))
                .doOnError(exception ->
                        log.error("Unable to process response {} from service {{} : {}}: {}",
                                requestId, coreInstanceName, coreBaseUrl, exception));
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(SAVE_CHARACTER_FORMAT, coreBaseUrl))
                .put()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromObject(gameCharacter))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error(
                            "Unable to get success response from service {{} : {}}: { request_id: {}, status_code: {}}",
                            coreInstanceName, coreBaseUrl, requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(GameCharacter.class);
    }

    @Override
    public Mono<Adventure> createAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(ADVENTURE_FORMAT, coreBaseUrl))
                .post()
                .body(BodyInserters.fromObject(adventure))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Adventure.class)
                .doOnSuccess(storedAdventure -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, storedAdventure));
    }

    @Override
    public Mono<Adventure> updateAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(ADVENTURE_FORMAT, coreBaseUrl))
                .put()
                .body(BodyInserters.fromObject(adventure))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Adventure.class)
                .doOnSuccess(storedAdventure -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, storedAdventure));
    }

    @Override
    public Mono<Adventure> getActiveAdventureByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                    String.format(ADVENTURE_FORMAT, coreBaseUrl)
                        .concat("character/")
                        .concat(gameCharacterId))
                .get()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Adventure.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(BATTLE_FORMAT, coreBaseUrl))
                .post()
                .body(BodyInserters.fromObject(battleInfo))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(BattleInfo.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<BattleInfo> updateBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(BATTLE_FORMAT, coreBaseUrl))
                .put()
                .body(BodyInserters.fromObject(battleInfo))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(BattleInfo.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<BattleInfo> getBattleInfoByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                String.format(BATTLE_FORMAT, coreBaseUrl)
                        .concat("character/")
                        .concat(gameCharacterId))
                .get()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(BattleInfo.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Monster> createMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(MONSTER_FORMAT, coreBaseUrl))
                .post()
                .body(BodyInserters.fromObject(monster))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Monster.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Monster> updateMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(String.format(MONSTER_FORMAT, coreBaseUrl))
                .put()
                .body(BodyInserters.fromObject(monster))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Monster.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Void> deleteMonster(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                String.format(MONSTER_FORMAT, coreBaseUrl).concat(id))
                .delete()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Void.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}}",
                        coreInstanceName, coreBaseUrl));
    }

    @Override
    public Mono<Monster> getMonsterById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                String.format(MONSTER_FORMAT, coreBaseUrl).concat(id))
                .get()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Monster.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Awards> createAwards(Awards awards) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                String.format(AWARDS_FORMAT, coreBaseUrl))
                .post()
                .body(BodyInserters.fromObject(awards))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Awards.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    @Override
    public Mono<Awards> getAwardsById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        return WebClient.create(
                String.format(AWARDS_FORMAT, coreBaseUrl).concat(id))
                .get()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    log.error("Unable to get success response: { request_id: {}, status_code: {}}",
                            requestId, clientResponse.statusCode());
                    return Mono.error(new ServiceCommunicationException("Internal communication exception"));
                })
                .bodyToMono(Awards.class)
                .doOnSuccess(responseBody -> log.info("Incoming response from service {{} : {}} with body {}",
                        coreInstanceName, coreBaseUrl, responseBody));
    }

    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                String.format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));
    }
}
