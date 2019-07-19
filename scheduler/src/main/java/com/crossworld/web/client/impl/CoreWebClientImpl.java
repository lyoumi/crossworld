package com.crossworld.web.client.impl;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;

import com.crossworld.web.client.CoreWebClient;
import com.crossworld.web.data.character.GameCharacter;
import com.crossworld.web.data.events.adventure.Adventure;
import com.crossworld.web.data.events.awards.Awards;
import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.exception.AdventureNotFoundException;
import com.crossworld.web.exception.AwardsNotFoundException;
import com.crossworld.web.exception.BattleInfoNotFoundException;
import com.crossworld.web.exception.MonsterNotFoundException;
import com.crossworld.web.exception.ServiceCommunicationException;
import com.crossworld.web.exception.ServiceNotAvailableException;
import com.crossworld.web.filters.OutgoingRequestResponseLoggingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
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
    private static final String INTERNAL_COMMUNICATION_EXCEPTION = "Internal communication exception";
    private static final String UNABLE_TO_GET_SUCCESS_RESPONSE = "Unable to get success response from {}: {}: { request_id: {}, status_code: {}}";

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
    private final OutgoingRequestResponseLoggingFilter loggingFilter;

    @Override
    public Flux<GameCharacter> getAllGameCharacters() {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ALL_CHARACTERS_FORMAT, coreBaseUrl);

        return buildWebClient(requestId, url)
                .get()
                .header(REQUEST_ID_HEADER_NAME, requestId)
                .accept(APPLICATION_STREAM_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToFlux(GameCharacter.class)
                .doOnError(exception ->
                        log.error("Unable to process response {} from service {{} : {}}: {}",
                                requestId, GET, url, exception));
    }

    @Override
    public Mono<GameCharacter> saveGameCharacter(GameCharacter gameCharacter) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(SAVE_CHARACTER_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .put()
                .body(BodyInserters.fromObject(gameCharacter))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, PUT))
                .bodyToMono(GameCharacter.class);
    }

    @Override
    public Mono<Adventure> createAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .post()
                .body(BodyInserters.fromObject(adventure))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, POST))
                .bodyToMono(Adventure.class);
    }

    @Override
    public Mono<Adventure> updateAdventure(Adventure adventure) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .put()
                .body(BodyInserters.fromObject(adventure))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, PUT))
                .bodyToMono(Adventure.class);
    }

    @Override
    public Mono<Adventure> getActiveAdventureByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(ADVENTURE_FORMAT, coreBaseUrl)
                .concat("active/character/")
                .concat(gameCharacterId);
        return buildWebClient(requestId, url)
                .get()
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND), clientResponse ->
                        Mono.error(new AdventureNotFoundException(
                                format("Active adventure with character id %s not found", gameCharacterId))))
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToMono(Adventure.class);
    }

    @Override
    public Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .post()
                .body(BodyInserters.fromObject(battleInfo))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, POST))
                .bodyToMono(BattleInfo.class);
    }

    @Override
    public Mono<BattleInfo> updateBattleInfo(BattleInfo battleInfo) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .put()
                .body(BodyInserters.fromObject(battleInfo))
                .retrieve()
                .onStatus(HttpStatus::isError,
                        clientResponse -> buildError(requestId, url, clientResponse, PUT))
                .bodyToMono(BattleInfo.class);
    }

    @Override
    public Mono<BattleInfo> getBattleInfoByCharacterId(String gameCharacterId) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl)
                .concat("character/")
                .concat(gameCharacterId);
        return buildWebClient(requestId, url)
                .get()
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND), clientResponse ->
                        Mono.error(new BattleInfoNotFoundException(
                                format("Battle info with character id %s not found", gameCharacterId))))
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToMono(BattleInfo.class);
    }

    @Override
    public Mono<Void> deleteBattleInfo(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(BATTLE_FORMAT, coreBaseUrl).concat(id);

        return buildWebClient(requestId, url)
                .delete()
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<Monster> createMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .post()
                .body(BodyInserters.fromObject(monster))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, POST))
                .bodyToMono(Monster.class);
    }

    @Override
    public Mono<Monster> updateMonster(Monster monster) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .put()
                .body(BodyInserters.fromObject(monster))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> buildError(requestId, url, clientResponse, PUT))
                .bodyToMono(Monster.class);
    }

    @Override
    public Mono<Void> deleteMonster(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl).concat(id);
        return buildWebClient(requestId, url)
                .delete()
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError,
                        clientResponse -> buildError(requestId, url, clientResponse, DELETE))
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<Monster> getMonsterById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(MONSTER_FORMAT, coreBaseUrl).concat(id);
        return buildWebClient(requestId, url)
                .get()
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND),
                        clientResponse -> Mono.error(new MonsterNotFoundException(
                                format("Monster with id %s not found", id))))
                .onStatus(HttpStatus::isError,
                        clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToMono(Monster.class);
    }

    @Override
    public Mono<Awards> createAwards(Awards awards) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(AWARDS_FORMAT, coreBaseUrl);
        return buildWebClient(requestId, url)
                .post()
                .body(BodyInserters.fromObject(awards))
                .retrieve()
                .onStatus(HttpStatus::isError,
                        clientResponse -> buildError(requestId, url, clientResponse, POST))
                .bodyToMono(Awards.class);
    }

    @Override
    public Mono<Awards> getAwardsById(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        String url = format(AWARDS_FORMAT, coreBaseUrl).concat(id);
        return buildWebClient(requestId, url)
                .get()
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(NOT_FOUND), clientResponse ->
                        Mono.error(new AwardsNotFoundException(
                                format("Awards with id %s not found", id))))
                .onStatus(HttpStatus::isError,
                        clientResponse -> buildError(requestId, url, clientResponse, GET))
                .bodyToMono(Awards.class);
    }

    @Override
    public Mono<Void> deleteAwards(String id) {
        var coreBaseUrl = getCoreBaseUrl();

        var requestId = randomUUID().toString();

        var url = format(AWARDS_FORMAT, coreBaseUrl).concat(id);
        return buildWebClient(requestId, url)
                .delete()
                .retrieve()
                .onStatus(HttpStatus::isError,
                        clientResponse -> buildError(requestId, url, clientResponse, DELETE))
                .bodyToMono(Void.class);
    }

    private WebClient buildWebClient(String requestId, String url) {
        return WebClient.builder()
                .filter(loggingFilter)
                .baseUrl(url)
                .defaultHeaders(httpHeaders -> httpHeaders.add(REQUEST_ID_HEADER_NAME, requestId))
                .build();
    }

    private String getCoreBaseUrl() {
        return Optional.ofNullable(loadBalancerClient.choose(coreInstanceName))
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .orElseThrow(() ->
                        new ServiceNotAvailableException(
                                format(SERVICE_NOT_AVAILABLE_EXCEPTION_MESSAGE, coreInstanceName)));
    }

    private Mono<? extends Throwable> buildError(String requestId, String url, ClientResponse clientResponse,
            HttpMethod post) {
        log.error(UNABLE_TO_GET_SUCCESS_RESPONSE, post, url, requestId,
                clientResponse.statusCode());
        return Mono.error(new ServiceCommunicationException(INTERNAL_COMMUNICATION_EXCEPTION));
    }
}
