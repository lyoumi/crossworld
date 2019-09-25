package com.cwd.tg.gss.handlers.impl;

import static java.lang.String.format;

import com.cwd.tg.gss.data.events.battle.BattleInfo;
import com.cwd.tg.gss.errors.exceptions.BattleInfoNotFoundException;
import com.cwd.tg.gss.handlers.BattleInfoRequestHandler;
import com.cwd.tg.gss.repositories.BattleInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class BattleInfoRequestHandlerImpl implements BattleInfoRequestHandler {

    private final BattleInfoRepository battleInfoRepository;

    @Override
    public Mono<BattleInfo> getBattleInfo(String id) {
        return battleInfoRepository.getBattleInfoById(id)
                .switchIfEmpty(Mono.error(new BattleInfoNotFoundException(
                        format("Battle info with id %s not found", id))));
    }

    @Override
    public Mono<BattleInfo> getBattleInfoByCharacterId(String characterId) {
        return battleInfoRepository.getBattleInfoByCharacterId(characterId)
                .switchIfEmpty(Mono.error(new BattleInfoNotFoundException(
                        format("Battle info with character id %s not found", characterId))));

    }

    @Override
    public Mono<BattleInfo> createBattleInfo(BattleInfo battleInfo) {
        return battleInfoRepository.saveBattleInfo(battleInfo);
    }

    @Override
    public Mono<Void> deleteBattleInfo(String id) {
        return battleInfoRepository.deleteBattleInfoById(id);
    }

    @Override
    public Mono<Void> deleteBattleInfoByCharacterId(String characterId) {
        return battleInfoRepository.deleteBattleInfoByCharacterId(characterId);
    }

}
