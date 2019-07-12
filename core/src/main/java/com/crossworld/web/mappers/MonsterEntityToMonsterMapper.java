package com.crossworld.web.mappers;

import com.crossworld.web.data.events.battle.Monster;
import com.crossworld.web.entities.MonsterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface MonsterEntityToMonsterMapper extends Converter<MonsterEntity, Monster> {
    @Override
    Monster convert(MonsterEntity source);
}
