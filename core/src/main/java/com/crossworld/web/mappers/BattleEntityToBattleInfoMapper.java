package com.crossworld.web.mappers;

import com.crossworld.web.data.events.battle.BattleInfo;
import com.crossworld.web.entities.BattleEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface BattleEntityToBattleInfoMapper extends Converter<BattleEntity, BattleInfo> {
    @Override
    BattleInfo convert(BattleEntity source);
}
