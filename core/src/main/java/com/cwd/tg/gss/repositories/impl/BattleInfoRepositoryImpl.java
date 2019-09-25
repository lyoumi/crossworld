package com.cwd.tg.gss.repositories.impl;

import com.cwd.tg.gss.converters.ConverterService;
import com.cwd.tg.gss.dao.BattleInfoDao;
import com.cwd.tg.gss.data.events.battle.BattleInfo;
import com.cwd.tg.gss.repositories.BattleInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class BattleInfoRepositoryImpl implements BattleInfoRepository {

    private final BattleInfoDao battleInfoDao;
    private final ConverterService converterService;

    @Override
    public Mono<BattleInfo> saveBattleInfo(BattleInfo battleInfo) {
        return battleInfoDao
                .save(converterService.convert(battleInfo))
                .map(converterService::convert);
    }

    @Override
    public Mono<BattleInfo> getBattleInfoById(String id) {
        return battleInfoDao
                .findById(id)
                .map(converterService::convert);
    }

    @Override
    public Mono<BattleInfo> getBattleInfoByCharacterId(String characterId) {
        return battleInfoDao
                .findBattleEntityByCharacterId(characterId)
                .map(converterService::convert);
    }

    @Override
    public Mono<Void> deleteBattleInfoById(String id) {
        return battleInfoDao.deleteById(id);
    }

    @Override
    public Mono<Void> deleteBattleInfoByCharacterId(String characterId) {
        return battleInfoDao.deleteBattleEntityByCharacterId(characterId);
    }
}
