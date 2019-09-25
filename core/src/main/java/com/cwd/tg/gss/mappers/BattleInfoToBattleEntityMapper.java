package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.battle.BattleInfo;
import com.cwd.tg.gss.entities.BattleEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface BattleInfoToBattleEntityMapper extends Converter<BattleInfo, BattleEntity> {
    @Override
    BattleEntity convert(BattleInfo source);
}
