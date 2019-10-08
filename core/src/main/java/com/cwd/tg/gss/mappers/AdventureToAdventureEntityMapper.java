package com.cwd.tg.gss.mappers;

import com.cwd.tg.gss.data.events.adventure.Adventure;
import com.cwd.tg.gss.entities.AdventureEntity;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper
public interface AdventureToAdventureEntityMapper extends Converter<Adventure, AdventureEntity> {
    @Override
    AdventureEntity convert(Adventure source);
}
