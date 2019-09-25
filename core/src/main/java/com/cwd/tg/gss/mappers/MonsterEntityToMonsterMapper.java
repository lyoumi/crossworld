package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.entities.MonsterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface MonsterEntityToMonsterMapper extends Converter<MonsterEntity, Monster> {
    @Override
    Monster convert(MonsterEntity source);
}
