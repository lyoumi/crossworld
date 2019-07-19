package com.crossworld.web.resources;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.errors.exceptions.BattleInfoNotFoundException;
import com.crossworld.web.repositories.BattleInfoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            @PathVariable(value = "id") String id) {
        return battleInfoRepository.getBattleInfoById(id)
                .switchIfEmpty(Mono.error(new BattleInfoNotFoundException(
                        format("Battle info with id %s not found", id))));
    }

    @ResponseStatus(OK)
    @GetMapping(value = "character/{character_id}")
    public Mono<BattleInfo> getBattleInfoByCharacterId(
            @PathVariable(value = "character_id") String characterId) {
        return battleInfoRepository.getBattleInfoByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new BattleInfoNotFoundException(
                        format("Battle info with character id %s not found", characterId))));

    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Mono<BattleInfo> createBattleInfo(
            @RequestBody BattleInfo battleInfo) {
        return battleInfoRepository.saveBattleInfo(battleInfo);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public Mono<Void> deleteBattleInfo(
            @PathVariable(value = "id") String id) {
        return battleInfoRepository.deleteBattleInfoById(id);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(value = "character/{character_id}")
    public Mono<Void> deleteBattleInfoByCharacterId(
            @PathVariable(value = "character_id") String characterId) {
        return battleInfoRepository.deleteBattleInfoByCharacterId(characterId);
    }

}
