package com.crossworld.web.resources;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.repositories.BattleInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("private/battle")
public class BattleInfoResource {

    private final BattleInfoRepository battleInfoRepository;

    @ResponseStatus(OK)
    @GetMapping(value = "{id}")
    public Mono<BattleInfo> getBattleInfo(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return battleInfoRepository
                .getBattleInfoById(id)
                .doOnError(throwable -> log.error("Unable to get battle info { request_id: {}, battle_id: {} }",
                        requestId, id, throwable));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "character/{character_id}")
    public Mono<BattleInfo> getBattleInfoByCharacterId(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "character_id") String characterId) {
        return battleInfoRepository
                .getBattleInfoByCharacterId(characterId)
                .doOnError(throwable -> log.error("Unable to get battle info { request_id: {}, character_id: {} }",
                        requestId, characterId, throwable));
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<BattleInfo> createBattleInfo(
            @RequestHeader(value = "request_id") String requestId,
            @RequestBody BattleInfo battleInfo) {
        return battleInfoRepository
                .saveBattleInfo(battleInfo)
                .doOnError(throwable -> log.error("Unable to create battle info { request_id: {}, request_body: {} }",
                        requestId, battleInfo, throwable));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteBattleInfo(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "id") String id) {
        return battleInfoRepository
                .deleteBattleInfoById(id)
                .doOnError(throwable -> log.error("Unable to delete battle info { request_id: {}, battle_id: {} }",
                        requestId, id, throwable));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "character/{character_id}")
    public Mono<Void> deleteBattleInfoByCharacterId(
            @RequestHeader(value = "request_id") String requestId,
            @PathVariable(value = "character_id") String characterId) {
        return battleInfoRepository
                .deleteBattleInfoByCharacterId(characterId)
                .doOnError(throwable -> log.error("Unable to delete battle info { request_id: {}, character_id: {} }",
                        requestId, characterId, throwable));
    }

}
