package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.battle.Monster;
import com.cwd.tg.gss.entities.MonsterEntity;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface MonsterToMonsterEntityMapper extends Converter<Monster, MonsterEntity> {
    @Override
    MonsterEntity convert(Monster source);
}
